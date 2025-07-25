package org.apache.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Log4j LogManager compatibility class that manages loggers.
 * Provides drop-in replacement for org.apache.log4j.LogManager.
 */
public class LogManager {
    
    private static final ConcurrentMap<String, Logger> loggerCache = new ConcurrentHashMap<>();
    private static final Logger rootLogger = new Logger("ROOT");
    
    /**
     * Get a logger for the specified class.
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
    
    /**
     * Get a logger for the specified name.
     */
    public static Logger getLogger(String name) {
        if (name == null) {
            name = "NULL";
        }
        
        // Use cache for performance
        Logger logger = loggerCache.get(name);
        if (logger == null) {
            logger = new Logger(name);
            Logger existing = loggerCache.putIfAbsent(name, logger);
            if (existing != null) {
                logger = existing;
            }
        }
        return logger;
    }
    
    /**
     * Get the root logger.
     */
    public static Logger getRootLogger() {
        return rootLogger;
    }
    
    /**
     * Check if a logger exists.
     */
    public static boolean exists(String name) {
        return loggerCache.containsKey(name);
    }
    
    /**
     * Shutdown the logging system.
     * This delegates to log4Rich's shutdown mechanism.
     */
    public static void shutdown() {
        try {
            // Call log4Rich shutdown if available
            com.log4rich.Log4Rich.shutdown();
        } catch (Exception e) {
            // Ignore shutdown errors
        }
        
        // Clear our cache
        loggerCache.clear();
    }
    
    /**
     * Reset the configuration.
     * This is a no-op in our implementation as log4Rich handles configuration differently.
     */
    public static void resetConfiguration() {
        // log4Rich handles configuration differently, so this is mostly a no-op
        // But we clear the cache to force recreation of loggers
        loggerCache.clear();
    }
    
    /**
     * Get the current number of cached loggers.
     */
    public static int getCachedLoggerCount() {
        return loggerCache.size();
    }
}