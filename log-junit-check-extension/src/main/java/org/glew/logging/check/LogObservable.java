package org.glew.logging.check;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LogObservable {

    private final Set<LogObserver> observers;

    private static final class InstanceHolder {
        private static final LogObservable INSTANCE = new LogObservable();
    }

    private LogObservable(){
        observers = new HashSet<>();
    }

    public static LogObservable getInstance(){
        return InstanceHolder.INSTANCE;
    }

    public void subscribe(LogObserver observer){
        observers.add(observer);
    }

    public void unsubscribe(LogObserver observer){
        observers.remove(observer);
    }

    public void emit(LogLine logLine){
        observers.forEach(observer -> observer.accept(logLine));
    }

    Set<LogObserver> observers(){
        return Collections.unmodifiableSet(observers);
    }
}
