package dev.glew.logging.check;

import dev.glew.logging.check.annotation.Logs;
import dev.glew.logging.check.annotation.ManyLogs;
import dev.glew.logging.check.observable.FilteredListLogObserver;
import dev.glew.logging.check.observable.LogObservable;
import dev.glew.logging.check.observable.LogObservableFactory;
import dev.glew.logging.check.observable.LogObserver;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class LogCheckExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        var annotations = getAnnotations(invocationContext);
        if (annotations.isEmpty()) {
            invocation.proceed();
            return;
        }
        var assertions = new ArrayList<Assertions>();
        for (var annotation : annotations) {
            assertions.add(new Assertions(
                    getFilters(annotation),
                    getAssertions(annotation.eachLineShouldContain(), annotation.shouldNotContain()),
                    getObservable(annotation),
                    annotation.failOnNoLogs(),
                    List.of(annotation.shouldContain())));
        }
        try {
            invocation.proceed();
        } finally {
            assertions.forEach(Assertions::unsubscribe);
        }

        assertions.forEach(Assertions::check);
    }

    private static LogObservable getObservable(Logs annotation) {
        return LogObservableFactory.getInstance().get(annotation);
    }

    private static ArrayList<Predicate<LogLine>> getFilters(Logs annotation) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        var filters = new ArrayList<Predicate<LogLine>>();
        filters.add(ll -> ll.line().contains(annotation.forClass().getSimpleName()));
        for (var p : annotation.matching()) {
            filters.add(p.getConstructor().newInstance());
        }
        return filters;
    }

    private static ArrayList<Logs> getAnnotations(ReflectiveInvocationContext<Method> invocationContext) {
        var annotation = invocationContext.getExecutable().getAnnotation(Logs.class);
        var manyAnnotation = invocationContext.getExecutable().getAnnotation(ManyLogs.class);
        var annotations = new ArrayList<Logs>();
        if (annotation != null) {
            annotations.add(annotation);
        }
        if (manyAnnotation != null) {
            annotations.addAll(Arrays.asList(manyAnnotation.value()));
        }
        return annotations;
    }

    private Consumer<LogLine> getAssertions(String[] contains, String[] notContains) {
        return ll -> {
            var assertions = new ArrayList<Executable>();
            for (var val : notContains) {
                assertions.add(() -> assertFalse(ll.line().contains(val),
                        "Expecting log line [%s] not to contain [%s] but it did!".formatted(ll.line(), val)));
            }
            for (var val : contains) {
                assertions.add(() -> assertTrue(ll.line().contains(val),
                        "Expecting log line [%s] to contain [%s] but it didn't!".formatted(ll.line(), val)));
            }
            assertAll(assertions);
        };
    }

    static class Assertions {
        final LogObserver observer;
        final List<LogLine> logLines;
        final Consumer<LogLine> assertions;
        final LogObservable logObservable;
        final boolean failOnNoLogs;
        final List<String> containsChecks;


        Assertions(List<Predicate<LogLine>> filters, Consumer<LogLine> assertions, LogObservable logObservable, boolean failOnNoLogs, List<String> containsChecks) {
            this.failOnNoLogs = failOnNoLogs;
            this.containsChecks = containsChecks;
            this.logLines = new ArrayList<>();
            this.observer = new FilteredListLogObserver(filters, logLines::add);
            this.assertions = assertions;
            this.logObservable = logObservable;
            this.logObservable.subscribe(observer);
        }


        void unsubscribe() {
            logObservable.unsubscribe(observer);
        }

        void check() {
            if (failOnNoLogs){
                assertFalse(logLines.isEmpty(),
                        "There were no logs recorded for this test!");
            }
            logLines.forEach(assertions);
            var output = logLines.stream().map(LogLine::line).collect(Collectors.joining("\n"));
            containsChecks.forEach(str -> assertTrue(output.contains(str),
                    "expecting log output to contain %s but it was not present!"));
        }
    }
}
