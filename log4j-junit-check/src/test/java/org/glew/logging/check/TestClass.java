package org.glew.logging.check;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestClass {
    private static final Logger log = LogManager.getLogger(TestClass.class);

    public void unmaskedLoggingMethod(String pii) {
        log.info(pii);
    }

    public void maskedLoggingMethod(String pii) {
        log.info(pii.replaceAll("(?<=.).", "*"));
    }
}
