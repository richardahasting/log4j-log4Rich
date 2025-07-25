package demo;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Demo application showing log4j to log4Rich translation in action.
 * 
 * This application uses standard log4j 1.x API calls which are automatically
 * translated to log4Rich for ultra-high-performance logging.
 */
public class DemoApplication {
    // Standard log4j Logger - automatically uses log4Rich backend
    private static final Logger logger = Logger.getLogger(DemoApplication.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("log4j to log4Rich Translation Demo");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Standard log4j API calls - zero code changes required!
        logger.info("Starting demo application...");
        
        // Different log levels work exactly as expected
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
        
        // Formatted logging with parameters
        String user = "John Doe";
        int attempts = 3;
        logger.info("User '" + user + "' logged in after " + attempts + " attempts");
        
        // Exception logging
        try {
            simulateError();
        } catch (Exception e) {
            logger.error("Error occurred during processing", e);
        }
        
        // Conditional logging for performance
        if (logger.isDebugEnabled()) {
            logger.debug("Expensive debug operation result: " + expensiveOperation());
        }
        
        // Level checking
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn("Warning level is enabled");
        }
        
        System.out.println();
        System.out.println("Demo completed! All log4j calls were automatically");
        System.out.println("translated to log4Rich for maximum performance.");
        System.out.println("=".repeat(60));
    }
    
    private static void simulateError() throws Exception {
        throw new RuntimeException("Simulated error for demo purposes");
    }
    
    private static String expensiveOperation() {
        // Simulate an expensive operation
        return "result_" + System.currentTimeMillis();
    }
}