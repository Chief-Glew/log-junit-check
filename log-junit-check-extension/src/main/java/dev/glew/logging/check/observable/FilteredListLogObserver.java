package dev.glew.logging.check.observable;

import dev.glew.logging.check.LogLine;

import java.util.List;
import java.util.function.Predicate;

public class FilteredListLogObserver implements LogObserver {

    private final List<Predicate<LogLine>> filters;
    private final LogObserver delegate;

    public FilteredListLogObserver(List<Predicate<LogLine>> filters, LogObserver delegate) {
        this.filters = filters;
        this.delegate = delegate;
    }

    @Override
    public void accept(LogLine logLine) {
        if (filters.stream().allMatch(f -> f.test(logLine))){
            delegate.accept(logLine);
        }
    }
}
