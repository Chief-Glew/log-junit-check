package org.glew.logging.check;

import org.glew.logging.check.observable.StaticLogObservable;

public class TestClass {

    public static void unmaskedLoggingMethod(String pii){
        //this would be a log and will be in the log library specific implementations
        StaticLogObservable.getInstance().emit(new LogLine("%s %s".formatted(TestClass.class, pii)));
    }

    public static void maskedLoggingMethod(String pii){
        //this would be a log and will be in the log library specific implementations
        StaticLogObservable.getInstance().emit(
                new LogLine("%s %s".formatted(TestClass.class, pii.replaceAll("(?<=.).", "*"))));
    }
}
