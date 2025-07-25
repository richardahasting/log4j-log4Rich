package org.apache.log4j;

import com.log4rich.core.LogLevel;

/**
 * Log4j Level compatibility class that maps to log4Rich LogLevel.
 * Provides drop-in replacement for org.apache.log4j.Level.
 */
public class Level {
    
    public static final Level OFF = new Level(LogLevel.OFF, "OFF", Integer.MAX_VALUE);
    public static final Level FATAL = new Level(LogLevel.FATAL, "FATAL", 50000);
    public static final Level ERROR = new Level(LogLevel.ERROR, "ERROR", 40000);
    public static final Level WARN = new Level(LogLevel.WARN, "WARN", 30000);
    public static final Level INFO = new Level(LogLevel.INFO, "INFO", 20000);
    public static final Level DEBUG = new Level(LogLevel.DEBUG, "DEBUG", 10000);
    public static final Level TRACE = new Level(LogLevel.TRACE, "TRACE", 5000);
    public static final Level ALL = new Level(LogLevel.TRACE, "ALL", Integer.MIN_VALUE);
    
    private final LogLevel log4RichLevel;
    private final String name;
    private final int intValue;
    
    protected Level(LogLevel log4RichLevel, String name, int intValue) {
        this.log4RichLevel = log4RichLevel;
        this.name = name;
        this.intValue = intValue;
    }
    
    /**
     * Get the log4Rich LogLevel equivalent.
     */
    public LogLevel getLog4RichLevel() {
        return log4RichLevel;
    }
    
    /**
     * Get the string representation of this level.
     */
    public String toString() {
        return name;
    }
    
    /**
     * Get the integer value of this level.
     */
    public int toInt() {
        return intValue;
    }
    
    /**
     * Check if this level is greater than or equal to the specified level.
     */
    public boolean isGreaterOrEqual(Level level) {
        return this.intValue >= level.intValue;
    }
    
    /**
     * Convert log4j level name to Level object.
     */
    public static Level toLevel(String name) {
        if (name == null) {
            return DEBUG;
        }
        
        String upperName = name.toUpperCase();
        switch (upperName) {
            case "OFF": return OFF;
            case "FATAL": return FATAL;
            case "ERROR": return ERROR;
            case "WARN": return WARN;
            case "INFO": return INFO;
            case "DEBUG": return DEBUG;
            case "TRACE": return TRACE;
            case "ALL": return ALL;
            default: return DEBUG;
        }
    }
    
    /**
     * Convert integer value to Level object.
     */
    public static Level toLevel(int intValue) {
        if (intValue >= OFF.intValue) return OFF;
        if (intValue >= FATAL.intValue) return FATAL;
        if (intValue >= ERROR.intValue) return ERROR;
        if (intValue >= WARN.intValue) return WARN;
        if (intValue >= INFO.intValue) return INFO;
        if (intValue >= DEBUG.intValue) return DEBUG;
        if (intValue >= TRACE.intValue) return TRACE;
        return ALL;
    }
    
    /**
     * Convert integer value to Level object with default fallback.
     */
    public static Level toLevel(int intValue, Level defaultLevel) {
        Level level = toLevel(intValue);
        return level != null ? level : defaultLevel;
    }
    
    /**
     * Convert string to Level object with default fallback.
     */
    public static Level toLevel(String name, Level defaultLevel) {
        if (name == null) {
            return defaultLevel;
        }
        Level level = toLevel(name);
        return level != null ? level : defaultLevel;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Level level = (Level) obj;
        return intValue == level.intValue;
    }
    
    @Override
    public int hashCode() {
        return intValue;
    }
}