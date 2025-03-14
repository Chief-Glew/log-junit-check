package dev.glew.logging.check.observable;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class StaticLogObservableTest {

    @Test
    void testThatObserverIsAdded() {
        var observable = StaticLogObservable.getInstance();
        LogObserver observer = ll -> {};
        var observers = observable.observers();

        observable.subscribe(observer);
        assertThat(observers).contains(observer);

        observable.unsubscribe(observer);
        assertThat(observers).doesNotContain(observer);
    }
}