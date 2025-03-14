package dev.glew.logging.check.observable;

import dev.glew.logging.check.LogLine;

public interface LogObserver {
    void accept(LogLine logLine);
}
