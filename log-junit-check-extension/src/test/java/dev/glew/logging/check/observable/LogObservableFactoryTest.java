package dev.glew.logging.check.observable;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogObservableFactoryTest {

    @Test
    void testThatFactorySuppliesTestObservable(){
        LogObservable logObservable = LogObservableFactory.getInstance().get(null);

        assertThat(logObservable).isSameAs(StaticLogObservable.getInstance());
    }
}