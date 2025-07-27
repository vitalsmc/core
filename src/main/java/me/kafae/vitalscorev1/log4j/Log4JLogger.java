package me.kafae.vitalscorev1.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JLogger {

    private final String MODID = "vitalscorev1";
    private Logger logger = LogManager.getLogger(MODID);

    // getters
    public Logger getLogger() {
        return logger;
    }

}
