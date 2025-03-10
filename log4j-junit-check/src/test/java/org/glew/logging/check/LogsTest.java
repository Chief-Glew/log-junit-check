package org.glew.logging.check;

import org.glew.logging.check.annotation.Logs;
import org.glew.logging.check.extension.ExpectFailure;
import org.glew.logging.check.extension.TestShouldFailExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        TestShouldFailExtension.class,
        LogCheckExtension.class,
})
public class LogsTest {

    private TestClass testClass;

    @BeforeEach
    void beforeEach() {
        testClass = new TestClass();
    }

    @Test
    @ExpectFailure
    @Logs(forClass = TestClass.class, shouldNotContain = "foo")
    void testThatFailsBecauseBadLogsObserved() {
        testClass.unmaskedLoggingMethod("foo");
    }

    @Test
    @ExpectFailure
    @Logs(forClass = TestClass.class, shouldContain = "ImportantInfo")
    void testThatFailBecauseLogsMissing() {
        testClass.maskedLoggingMethod("ImportantInfo");
    }

    @Test
    @Logs(forClass = TestClass.class, shouldNotContain = "foo")
    void testThatFooNotPresent() {
        testClass.maskedLoggingMethod("foo");
    }

    @Test
    @Logs(forClass = TestClass.class, shouldContain = "ImportantInfo")
    void testThatImportantInfoPresent() {
        testClass.unmaskedLoggingMethod("ImportantInfo");
    }
}
