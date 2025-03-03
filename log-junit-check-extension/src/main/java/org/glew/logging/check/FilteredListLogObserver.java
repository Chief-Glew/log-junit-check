package org.glew.logging.check;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilteredListLogObserver implements LogObserver {

    private final List<Predicate<LogLine>> filters;
    private final List<LogLine> logLines;

    public FilteredListLogObserver(List<Predicate<LogLine>> filters) {
        this(filters, new ArrayList<>());
    }
    public FilteredListLogObserver(List<Predicate<LogLine>> filters, List<LogLine> logLines) {
        this.filters = filters;
        this.logLines = logLines;
    }

    @Override
    public void accept(LogLine logLine) {
        if (filters.stream().allMatch(f -> f.test(logLine))){
            logLines.add(logLine);
        }
    }

    public List<LogLine> logLines(){
        return logLines;
    }
}
