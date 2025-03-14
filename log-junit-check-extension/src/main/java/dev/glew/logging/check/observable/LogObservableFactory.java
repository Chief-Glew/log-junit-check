package dev.glew.logging.check.observable;

import dev.glew.logging.check.annotation.Logs;

import java.util.ServiceLoader;

public final class LogObservableFactory {

    private final LogObservableProvider observableProvider;

    private static final class InstanceHolder {
        private static final LogObservableFactory INSTANCE = new LogObservableFactory();
    }

    private LogObservableFactory(){
        observableProvider = ServiceLoader.load(LogObservableProvider.class)
                .findFirst().orElse(StaticLogObservable::getInstance);
    }

    public static LogObservableFactory getInstance(){
        return InstanceHolder.INSTANCE;
    }

    public LogObservable get(Logs logs){
        return observableProvider.get(logs);
    }
}
