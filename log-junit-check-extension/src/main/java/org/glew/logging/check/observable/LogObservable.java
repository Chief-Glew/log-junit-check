package org.glew.logging.check.observable;

public interface LogObservable {
    void subscribe(LogObserver observer);

    void unsubscribe(LogObserver observer);
}
