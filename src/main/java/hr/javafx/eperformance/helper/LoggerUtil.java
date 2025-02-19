package hr.javafx.eperformance.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging.
 */

public class LoggerUtil {

    private LoggerUtil() {}

    public static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logError(String message){
        logger.error(message);
    }

    public static void logError(String message, Object... params){
        logger.error(message, params);
    }

    public static void logInfo(String message){
        logger.info(message);
    }

    public static void logInfo(String message, Object... params){
        logger.info(message, params);
    }

}
