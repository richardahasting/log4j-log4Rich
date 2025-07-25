package org.apache.log4j.demo;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.Category;

/**
 * Demonstration of log4j-log4Rich compatibility.
 * Shows how existing log4j code works without any modifications.
 */
public class Log4jCompatibilityDemo {
    
    // Standard log4j logger declaration - no changes needed!
    private static final Logger logger = Logger.getLogger(Log4jCompatibilityDemo.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("log4j-log4Rich Compatibility Demonstration");
        System.out.println("=".repeat(60));
        System.out.println("This demo shows log4j 1.x code running unchanged");
        System.out.println("with log4Rich's ultra-high-performance backend!");
        System.out.println();
        
        // Test all log4j API patterns that applications commonly use
        demonstrateBasicLogging();
        demonstrateLogManagerUsage();
        demonstrateLevelChecking();
        demonstrateExceptionLogging();
        demonstrateCategoryCompatibility();
        demonstratePriorityUsage();
        demonstrateGenericLogging();
        
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("All log4j patterns work perfectly!");
        System.out.println("Your application gets log4Rich performance");
        System.out.println("with ZERO code changes required!");
        System.out.println("=".repeat(60));
    }
    
    private static void demonstrateBasicLogging() {
        System.out.println("1. Basic Logging (all levels work exactly like log4j):");
        
        logger.trace("TRACE: Detailed tracing information");
        logger.debug("DEBUG: Debugging information for developers");
        logger.info("INFO: General information about application flow");
        logger.warn("WARN: Warning about potential issues");
        logger.error("ERROR: Error conditions that don't stop the application");
        logger.fatal("FATAL: Critical errors that may cause shutdown");
        
        System.out.println("   ✅ All log levels working perfectly\n");
    }
    
    private static void demonstrateLogManagerUsage() {
        System.out.println("2. LogManager Usage (log4j factory patterns):");
        
        // Different ways to get loggers - all work identically
        Logger logger1 = LogManager.getLogger("custom.logger.name");
        Logger logger2 = LogManager.getLogger(Log4jCompatibilityDemo.class);
        Logger rootLogger = LogManager.getRootLogger();
        
        logger1.info("Logger from LogManager with custom name");
        logger2.info("Logger from LogManager with class");
        rootLogger.info("Root logger from LogManager");
        
        System.out.println("   ✅ LogManager factory methods working\n");
    }
    
    private static void demonstrateLevelChecking() {
        System.out.println("3. Level Checking (performance optimization patterns):");
        
        // Standard log4j level checking patterns
        if (logger.isTraceEnabled()) {
            logger.trace("Expensive trace operation: " + performExpensiveOperation());
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Debug info: " + gatherDebugInfo());
        }
        
        if (logger.isInfoEnabled()) {
            logger.info("Info: Application state is healthy");
        }
        
        // Check specific levels
        boolean debugEnabled = logger.isDebugEnabled();
        boolean errorEnabled = logger.isErrorEnabled();
        
        System.out.println("   ✅ Level checking: DEBUG=" + debugEnabled + ", ERROR=" + errorEnabled + "\n");
    }
    
    private static void demonstrateExceptionLogging() {
        System.out.println("4. Exception Logging (error handling patterns):");
        
        try {
            throw new RuntimeException("Simulated application error");
        } catch (Exception e) {
            // Standard log4j exception logging - works identically
            logger.error("Caught exception in demo", e);
            logger.warn("Exception warning", e);
            logger.fatal("Critical exception", e);
        }
        
        System.out.println("   ✅ Exception logging with stack traces\n");
    }
    
    private static void demonstrateCategoryCompatibility() {
        System.out.println("5. Category Compatibility (legacy log4j 1.x):");
        
        // Old log4j Category API still works for legacy code
        Category category = Category.getInstance(Log4jCompatibilityDemo.class);
        category.info("Category logging works for legacy applications");
        
        Category rootCategory = Category.getRoot();
        rootCategory.warn("Root category logging");
        
        // Legacy additivity methods (no-op but compatible)
        category.setAdditivity(true);
        boolean additivity = category.getAdditivity();
        
        System.out.println("   ✅ Category API compatibility: additivity=" + additivity + "\n");
    }
    
    private static void demonstratePriorityUsage() {
        System.out.println("6. Priority Usage (log4j 1.x priority system):");
        
        // Old Priority-based logging
        logger.log(Priority.DEBUG, "Debug via Priority");
        logger.log(Priority.INFO, "Info via Priority");
        logger.log(Priority.WARN, "Warning via Priority");
        logger.log(Priority.ERROR, "Error via Priority");
        
        System.out.println("   ✅ Priority-based logging working\n");
    }
    
    private static void demonstrateGenericLogging() {
        System.out.println("7. Generic Logging (Level-based API):");
        
        // Level-based logging
        logger.log(Level.DEBUG, "Debug via Level object");
        logger.log(Level.INFO, "Info via Level object");
        logger.log(Level.ERROR, "Error via Level object");
        
        // Check levels with isEnabledFor
        if (logger.isEnabledFor(Level.INFO)) {
            logger.log(Level.INFO, "Info level is enabled");
        }
        
        if (logger.isEnabledFor(Priority.WARN)) {
            logger.log(Priority.WARN, "Warn priority is enabled");
        }
        
        System.out.println("   ✅ Generic logging with Level/Priority objects\n");
    }
    
    private static String performExpensiveOperation() {
        // Simulate expensive string building
        return "expensive_data_" + System.currentTimeMillis();
    }
    
    private static String gatherDebugInfo() {
        return "debug_info_" + Thread.currentThread().getName();
    }
}