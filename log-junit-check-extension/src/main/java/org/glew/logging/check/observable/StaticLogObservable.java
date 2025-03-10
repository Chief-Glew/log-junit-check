package org.glew.logging.check.observable;

import org.glew.logging.check.LogLine;
import org.glew.logging.check.annotation.Logs;

public class StaticLogObservable extends AbstractLogObservable {

    private static final class InstanceHolder {

        private static final StaticLogObservable INSTANCE = new StaticLogObservable();
    }
    private StaticLogObservable(){
        super();
    }

    public static StaticLogObservable getInstance(){
        return InstanceHolder.INSTANCE;
    }

    public static LogObservable getInstance(Logs logs) {
        return getInstance();
    }

    public void emit(LogLine logLine){
        observers().forEach(observer -> observer.accept(logLine));
    }

}
