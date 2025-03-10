package org.glew.logging.check;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.filter.LevelRangeFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.glew.logging.check.annotation.Logs;
import org.glew.logging.check.observable.LogObservable;
import org.glew.logging.check.observable.LogObservableProvider;

import java.util.UUID;

public class Log4jObservableProvider implements LogObservableProvider {
    @Override
    public LogObservable get(Logs logs) {
        Logger logger = LogManager.getLogger(logs.forClass());
        org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) logger;

        return new Log4jAppenderObservable(UUID.randomUUID().toString(),
                LevelRangeFilter.createFilter(null, null, null, null),
                PatternLayout.newBuilder().withPattern(PatternLayout.SIMPLE_CONVERSION_PATTERN).build(),
                coreLogger);
    }
}
