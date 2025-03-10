package org.glew.logging.check.observable;

import org.glew.logging.check.annotation.Logs;

public interface LogObservableProvider {

    LogObservable get(Logs logs);
}
