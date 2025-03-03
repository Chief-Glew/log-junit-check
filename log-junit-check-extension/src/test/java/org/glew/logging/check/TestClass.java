package org.glew.logging.check;

public class TestClass {

    public static void unmaskedLoggingMethod(String pii){
        //this would be a log and will be in the log library specific implementations
        LogObservable.getInstance().emit(new LogLine("%s %s".formatted(TestClass.class, pii)));
    }

    public static void maskedLoggingMethod(String pii){
        //this would be a log and will be in the log library specific implementations
        LogObservable.getInstance().emit(
                new LogLine("%s %s".formatted(TestClass.class, pii.replaceAll("(?<=.).", "*"))));
    }
}
