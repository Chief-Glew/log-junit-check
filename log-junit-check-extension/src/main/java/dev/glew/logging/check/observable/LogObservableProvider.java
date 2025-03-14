package dev.glew.logging.check.observable;

import dev.glew.logging.check.annotation.Logs;

public interface LogObservableProvider {

    LogObservable get(Logs logs);
}
