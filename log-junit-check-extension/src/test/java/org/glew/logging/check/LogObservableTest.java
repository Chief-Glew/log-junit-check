package org.glew.logging.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.assertj.core.api.Assertions.assertThat;

class LogObservableTest {

    @Test
    void testThatObserverIsAdded() {
        var observable = LogObservable.getInstance();
        LogObserver observer = ll -> {};
        var observers = observable.observers();

        observable.subscribe(observer);
        assertThat(observers).contains(observer);

        observable.unsubscribe(observer);
        assertThat(observers).doesNotContain(observer);
    }
}