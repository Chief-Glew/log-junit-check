package dev.glew.logging.check;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import dev.glew.logging.check.observable.LogObservable;
import dev.glew.logging.check.observable.LogObserver;

import java.util.HashSet;
import java.util.Set;

public class Log4jAppenderObservable extends AbstractAppender implements LogObservable {

    private final Set<LogObserver> observers;
    private final Logger logger;

    protected Log4jAppenderObservable(String name, Filter filter, StringLayout layout, Logger logger) {
        super(name, filter, layout, false, new Property[]{});
        this.logger = logger;
        observers = new HashSet<>();
    }

    @Override
    public void append(LogEvent event) {
        String s = ((StringLayout) getLayout()).toSerializable(event);
        observers.forEach(observer -> observer.accept(new LogLine(s)));
    }

    @Override
    public void subscribe(LogObserver observer) {
        if (observers.isEmpty()){
            start();
            logger.get().addAppender(this, null, getFilter());
        }
        observers.add(observer);
    }

    @Override
    public void unsubscribe(LogObserver observer) {
        observers.remove(observer);
        if (observers.isEmpty()){
            stop();
            logger.get().removeAppender(getName());
        }
    }
}
