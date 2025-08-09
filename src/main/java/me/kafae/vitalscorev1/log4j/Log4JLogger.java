package me.kafae.vitalscorev1.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JLogger {

    private static final String MODID = "vitalscorev1";
    private final Logger logger = LogManager.getLogger(MODID);

    public void info(String s) {
        logger.info("[" + MODID + "] {}", s);
    }

    public void warn(String s) {
        logger.warn("[" + MODID + "] {}", s);
    }

    public void error(String s) {
        logger.error("[" + MODID + "] {}", s);
    }

}
