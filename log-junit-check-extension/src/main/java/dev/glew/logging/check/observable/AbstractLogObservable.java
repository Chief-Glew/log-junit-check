package dev.glew.logging.check.observable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLogObservable implements LogObservable {
    private final Set<LogObserver> observers;

    protected AbstractLogObservable() {
        observers = new HashSet<>();
    }

    @Override
    public void subscribe(LogObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(LogObserver observer) {
        observers.remove(observer);
    }

    protected Set<LogObserver> observers(){
        return Collections.unmodifiableSet(observers);
    }
}
