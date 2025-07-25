package org.apache.log4j;

/**
 * Priority class for log4j compatibility.
 * In log4j 1.x, Priority was the parent class of Level.
 * This maintains backward compatibility.
 */
public class Priority extends Level {
    
    public static final Priority FATAL = new Priority(Level.FATAL.getLog4RichLevel(), "FATAL", 50000);
    public static final Priority ERROR = new Priority(Level.ERROR.getLog4RichLevel(), "ERROR", 40000);
    public static final Priority WARN = new Priority(Level.WARN.getLog4RichLevel(), "WARN", 30000);
    public static final Priority INFO = new Priority(Level.INFO.getLog4RichLevel(), "INFO", 20000);
    public static final Priority DEBUG = new Priority(Level.DEBUG.getLog4RichLevel(), "DEBUG", 10000);
    
    protected Priority(com.log4rich.core.LogLevel log4RichLevel, String name, int intValue) {
        super(log4RichLevel, name, intValue);
    }
    
    /**
     * Convert string to Priority object.
     */
    public static Priority toPriority(String name) {
        Level level = Level.toLevel(name);
        return toPriority(level);
    }
    
    /**
     * Convert integer to Priority object.
     */
    public static Priority toPriority(int intValue) {
        Level level = Level.toLevel(intValue);
        return toPriority(level);
    }
    
    /**
     * Convert Level to Priority.
     */
    public static Priority toPriority(Level level) {
        if (level == null) return DEBUG;
        
        if (level.equals(Level.FATAL)) return FATAL;
        if (level.equals(Level.ERROR)) return ERROR;
        if (level.equals(Level.WARN)) return WARN;
        if (level.equals(Level.INFO)) return INFO;
        if (level.equals(Level.DEBUG)) return DEBUG;
        
        return DEBUG; // Default fallback
    }
    
    /**
     * Convert string to Priority with default fallback.
     */
    public static Priority toPriority(String name, Priority defaultPriority) {
        if (name == null) {
            return defaultPriority;
        }
        Priority priority = toPriority(name);
        return priority != null ? priority : defaultPriority;
    }
}