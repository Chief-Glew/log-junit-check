package org.glew.logging.check;

import org.glew.logging.check.annotation.Logs;
import org.glew.logging.check.annotation.ManyLogs;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class LogCheckExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        var annotations = getAnnotations(invocationContext);
        if (annotations.isEmpty()){
            invocation.proceed();
            return;
        }
        var assertions = new ArrayList<ObserverAndAssertions>();
        for (var annotation: annotations) {
            var filters = new ArrayList<Predicate<LogLine>>();
            if (annotation.forClass() != Logs.class) {
                filters.add(ll -> ll.line().contains(annotation.forClass().getSimpleName()));
            }
            for (var p : annotation.matching()) {
                filters.add(p.getConstructor().newInstance());
            }
            var observer = new FilteredListLogObserver(filters);

            LogObservable.getInstance().subscribe(observer);
            assertions.add(new ObserverAndAssertions(observer, getAssertions(annotation.shouldContain(), annotation.shouldNotContain())));
        }
        try {
            invocation.proceed();
        } finally {
            assertions.forEach(a -> LogObservable.getInstance().unsubscribe(a.observer()));
        }

        assertions.forEach(a -> a.observer().logLines().forEach(a.assertions()));
    }

    private static ArrayList<Logs> getAnnotations(ReflectiveInvocationContext<Method> invocationContext) {
        var annotation = invocationContext.getExecutable().getAnnotation(Logs.class);
        var manyAnnotation = invocationContext.getExecutable().getAnnotation(ManyLogs.class);
        var annotations = new ArrayList<Logs>();
        if (annotation != null){
            annotations.add(annotation);
        }
        if (manyAnnotation != null){
            annotations.addAll(Arrays.asList(manyAnnotation.value()));
        }
        return annotations;
    }

    private Consumer<LogLine> getAssertions(String[] contains, String[] notContains){
        return ll -> {
            var assertions = new ArrayList<Executable>();
            for (var val: notContains){
                assertions.add(() -> assertFalse(ll.line().contains(val),
                        "Expecting log line [%s] not to contain [%s] but it did!".formatted(ll.line(), val)));
            }
            for (var val: contains){
                assertions.add(() -> assertTrue(ll.line().contains(val),
                        "Expecting log line [%s] to contain [%s] but it didn't!".formatted(ll.line(), val)));
            }
            assertAll(assertions);
        };
    }

    record ObserverAndAssertions(FilteredListLogObserver observer, Consumer<LogLine> assertions){}
}
