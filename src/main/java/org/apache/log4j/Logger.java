package org.apache.log4j;

import com.log4rich.Log4Rich;
import com.log4rich.core.LogLevel;

/**
 * Log4j Logger compatibility class that delegates to log4Rich.
 * Provides drop-in replacement for org.apache.log4j.Logger.
 */
public class Logger {
    
    private final com.log4rich.core.Logger log4RichLogger;
    private final String name;
    
    protected Logger(String name) {
        this.name = name;
        this.log4RichLogger = Log4Rich.getLogger(name);
    }
    
    /**
     * Get a logger for the specified class.
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getName());
    }
    
    /**
     * Get a logger for the specified name.
     */
    public static Logger getLogger(String name) {
        return new Logger(name);
    }
    
    /**
     * Get the root logger.
     */
    public static Logger getRootLogger() {
        return new Logger("ROOT");
    }
    
    /**
     * Get the logger name.
     */
    public String getName() {
        return name;
    }
    
    // Level checking methods
    
    /**
     * Check if TRACE level is enabled.
     */
    public boolean isTraceEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.TRACE);
    }
    
    /**
     * Check if DEBUG level is enabled.
     */
    public boolean isDebugEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.DEBUG);
    }
    
    /**
     * Check if INFO level is enabled.
     */
    public boolean isInfoEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.INFO);
    }
    
    /**
     * Check if WARN level is enabled.
     */
    public boolean isWarnEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.WARN);
    }
    
    /**
     * Check if ERROR level is enabled.
     */
    public boolean isErrorEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.ERROR);
    }
    
    /**
     * Check if FATAL level is enabled.
     */
    public boolean isFatalEnabled() {
        return log4RichLogger.isLevelEnabled(LogLevel.FATAL);
    }
    
    /**
     * Check if the specified level is enabled.
     */
    public boolean isEnabledFor(Level level) {
        return log4RichLogger.isLevelEnabled(level.getLog4RichLevel());
    }
    
    /**
     * Check if the specified priority is enabled.
     */
    public boolean isEnabledFor(Priority priority) {
        return log4RichLogger.isLevelEnabled(priority.getLog4RichLevel());
    }
    
    // TRACE level logging methods
    
    public void trace(Object message) {
        log4RichLogger.trace(String.valueOf(message));
    }
    
    public void trace(Object message, Throwable throwable) {
        log4RichLogger.trace(String.valueOf(message), throwable);
    }
    
    // DEBUG level logging methods
    
    public void debug(Object message) {
        log4RichLogger.debug(String.valueOf(message));
    }
    
    public void debug(Object message, Throwable throwable) {
        log4RichLogger.debug(String.valueOf(message), throwable);
    }
    
    // INFO level logging methods
    
    public void info(Object message) {
        log4RichLogger.info(String.valueOf(message));
    }
    
    public void info(Object message, Throwable throwable) {
        log4RichLogger.info(String.valueOf(message), throwable);
    }
    
    // WARN level logging methods
    
    public void warn(Object message) {
        log4RichLogger.warn(String.valueOf(message));
    }
    
    public void warn(Object message, Throwable throwable) {
        log4RichLogger.warn(String.valueOf(message), throwable);
    }
    
    // ERROR level logging methods
    
    public void error(Object message) {
        log4RichLogger.error(String.valueOf(message));
    }
    
    public void error(Object message, Throwable throwable) {
        log4RichLogger.error(String.valueOf(message), throwable);
    }
    
    // FATAL level logging methods
    
    public void fatal(Object message) {
        log4RichLogger.fatal(String.valueOf(message));
    }
    
    public void fatal(Object message, Throwable throwable) {
        log4RichLogger.fatal(String.valueOf(message), throwable);
    }
    
    // Generic logging methods
    
    /**
     * Log a message at the specified level.
     */
    public void log(Level level, Object message) {
        switch (level.getLog4RichLevel()) {
            case TRACE: trace(message); break;
            case DEBUG: debug(message); break;
            case INFO: info(message); break;
            case WARN: warn(message); break;
            case ERROR: error(message); break;
            case FATAL: fatal(message); break;
        }
    }
    
    /**
     * Log a message and throwable at the specified level.
     */
    public void log(Level level, Object message, Throwable throwable) {
        switch (level.getLog4RichLevel()) {
            case TRACE: trace(message, throwable); break;
            case DEBUG: debug(message, throwable); break;
            case INFO: info(message, throwable); break;
            case WARN: warn(message, throwable); break;
            case ERROR: error(message, throwable); break;
            case FATAL: fatal(message, throwable); break;
        }
    }
    
    /**
     * Log a message at the specified priority.
     */
    public void log(Priority priority, Object message) {
        log(Level.toLevel(priority.toInt()), message);
    }
    
    /**
     * Log a message and throwable at the specified priority.
     */
    public void log(Priority priority, Object message, Throwable throwable) {
        log(Level.toLevel(priority.toInt()), message, throwable);
    }
    
    // Formatted logging methods (log4j style)
    
    /**
     * Log formatted message at TRACE level.
     */
    public void trace(String format, Object... args) {
        if (isTraceEnabled()) {
            log4RichLogger.trace(String.format(format, args));
        }
    }
    
    /**
     * Log formatted message at DEBUG level.
     */
    public void debug(String format, Object... args) {
        if (isDebugEnabled()) {
            log4RichLogger.debug(String.format(format, args));
        }
    }
    
    /**
     * Log formatted message at INFO level.
     */
    public void info(String format, Object... args) {
        if (isInfoEnabled()) {
            log4RichLogger.info(String.format(format, args));
        }
    }
    
    /**
     * Log formatted message at WARN level.
     */
    public void warn(String format, Object... args) {
        if (isWarnEnabled()) {
            log4RichLogger.warn(String.format(format, args));
        }
    }
    
    /**
     * Log formatted message at ERROR level.
     */
    public void error(String format, Object... args) {
        if (isErrorEnabled()) {
            log4RichLogger.error(String.format(format, args));
        }
    }
    
    /**
     * Log formatted message at FATAL level.
     */
    public void fatal(String format, Object... args) {
        if (isFatalEnabled()) {
            log4RichLogger.fatal(String.format(format, args));
        }
    }
    
    /**
     * Get the underlying log4Rich logger (for advanced usage).
     */
    public com.log4rich.core.Logger getLog4RichLogger() {
        return log4RichLogger;
    }
}