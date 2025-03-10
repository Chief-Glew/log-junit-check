package org.glew.logging.check;

import org.glew.logging.check.annotation.Logs;
import org.glew.logging.check.extension.ExpectFailure;
import org.glew.logging.check.extension.TestShouldFailExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        TestShouldFailExtension.class,
        LogCheckExtension.class,
})
class LogCheckExtensionTest {

    
    @Test
    @ExpectFailure
    @Logs(forClass = TestClass.class, shouldNotContain = "foo")
    void testThatFailsBecauseBadLogsObserved(){
        TestClass.unmaskedLoggingMethod("foo");
    }

    @Test
    @ExpectFailure
    @Logs(forClass = TestClass.class, shouldContain = "ImportantInfo")
    void testThatFailBecauseLogsMissing(){
        TestClass.maskedLoggingMethod("ImportantInfo");
    }


    @Test
    @Logs(forClass = TestClass.class, shouldNotContain = "foo")
    void testThatFooNotPresent(){
        TestClass.maskedLoggingMethod("foo");
    }

    @Test
    @Logs(forClass = TestClass.class, shouldContain = "ImportantInfo")
    void testThatImportantInfoPresent(){
        TestClass.unmaskedLoggingMethod("ImportantInfo");
    }

    @Test
    @Logs(forClass = TestClass.class, matching = NotContainFilter.class, shouldNotContain = "foo")
    @Logs(forClass = TestClass.class, matching = NotContainFilter.class, shouldContain = "ImportantInfo")
    void testThatWeCanMatchSpecificLogs(){
        TestClass.unmaskedLoggingMethod("filter foo");
        TestClass.unmaskedLoggingMethod("ImportantInfo");
    }

    public static class NotContainFilter implements LogLinePredicate {

        @Override
        public boolean test(LogLine logLine) {
            return !logLine.line().contains("filter");
        }
    }
}