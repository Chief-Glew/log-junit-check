package org.glew.logging.check.observable;

import org.glew.logging.check.LogLine;

public interface LogObserver {
    void accept(LogLine logLine);
}
