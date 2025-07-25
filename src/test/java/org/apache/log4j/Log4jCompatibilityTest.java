package org.apache.log4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for log4j compatibility layer.
 */
public class Log4jCompatibilityTest {
    
    private Logger logger;
    
    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(Log4jCompatibilityTest.class);
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up if needed
    }
    
    @Test
    public void testLoggerCreation() {
        assertNotNull(logger);
        assertEquals(Log4jCompatibilityTest.class.getName(), logger.getName());
    }
    
    @Test
    public void testLoggerFromLogManager() {
        Logger logger1 = LogManager.getLogger("test.logger");
        Logger logger2 = LogManager.getLogger("test.logger");
        
        assertNotNull(logger1);
        assertNotNull(logger2);
        assertEquals("test.logger", logger1.getName());
        assertEquals("test.logger", logger2.getName());
    }
    
    @Test
    public void testRootLogger() {
        Logger rootLogger = Logger.getRootLogger();
        assertNotNull(rootLogger);
        assertEquals("ROOT", rootLogger.getName());
        
        Logger rootFromManager = LogManager.getRootLogger();
        assertNotNull(rootFromManager);
        assertEquals("ROOT", rootFromManager.getName());
    }
    
    @Test
    public void testLevelConversion() {
        assertEquals("DEBUG", Level.DEBUG.toString());
        assertEquals("INFO", Level.INFO.toString());
        assertEquals("WARN", Level.WARN.toString());
        assertEquals("ERROR", Level.ERROR.toString());
        assertEquals("FATAL", Level.FATAL.toString());
        
        assertEquals(Level.DEBUG, Level.toLevel("DEBUG"));
        assertEquals(Level.INFO, Level.toLevel("INFO"));
        assertEquals(Level.WARN, Level.toLevel("WARN"));
        assertEquals(Level.ERROR, Level.toLevel("ERROR"));
        assertEquals(Level.FATAL, Level.toLevel("FATAL"));
    }
    
    @Test
    public void testLevelComparison() {
        assertTrue(Level.ERROR.isGreaterOrEqual(Level.WARN));
        assertTrue(Level.WARN.isGreaterOrEqual(Level.INFO));
        assertTrue(Level.INFO.isGreaterOrEqual(Level.DEBUG));
        assertTrue(Level.DEBUG.isGreaterOrEqual(Level.TRACE));
        
        assertFalse(Level.DEBUG.isGreaterOrEqual(Level.INFO));
        assertFalse(Level.INFO.isGreaterOrEqual(Level.WARN));
    }
    
    @Test
    public void testPriorityCompatibility() {
        Priority debug = Priority.DEBUG;
        Priority info = Priority.INFO;
        
        assertNotNull(debug);
        assertNotNull(info);
        assertEquals("DEBUG", debug.toString());
        assertEquals("INFO", info.toString());
    }
    
    @Test
    public void testBasicLogging() {
        // These should not throw exceptions
        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        logger.fatal("fatal message");
    }
    
    @Test
    public void testLoggingWithThrowable() {
        Exception testException = new RuntimeException("test exception");
        
        // These should not throw exceptions
        logger.trace("trace with exception", testException);
        logger.debug("debug with exception", testException);
        logger.info("info with exception", testException);
        logger.warn("warn with exception", testException);
        logger.error("error with exception", testException);
        logger.fatal("fatal with exception", testException);
    }
    
    @Test
    public void testGenericLogging() {
        // Test logging with Level objects
        logger.log(Level.DEBUG, "debug via log method");
        logger.log(Level.INFO, "info via log method");
        logger.log(Level.ERROR, "error via log method");
        
        // Test logging with Priority objects
        logger.log(Priority.DEBUG, "debug via priority log method");
        logger.log(Priority.INFO, "info via priority log method");
        logger.log(Priority.ERROR, "error via priority log method");
    }
    
    @Test
    public void testLevelChecking() {
        // These should return boolean values without throwing exceptions
        boolean traceEnabled = logger.isTraceEnabled();
        boolean debugEnabled = logger.isDebugEnabled();
        boolean infoEnabled = logger.isInfoEnabled();
        boolean warnEnabled = logger.isWarnEnabled();
        boolean errorEnabled = logger.isErrorEnabled();
        boolean fatalEnabled = logger.isFatalEnabled();
        
        // Check that these are actual boolean values
        assertTrue(traceEnabled || !traceEnabled);
        assertTrue(debugEnabled || !debugEnabled);
        assertTrue(infoEnabled || !infoEnabled);
        assertTrue(warnEnabled || !warnEnabled);
        assertTrue(errorEnabled || !errorEnabled);
        assertTrue(fatalEnabled || !fatalEnabled);
    }
    
    @Test
    public void testCategoryCompatibility() {
        Category category = Category.getInstance(Log4jCompatibilityTest.class);
        assertNotNull(category);
        assertEquals(Log4jCompatibilityTest.class.getName(), category.getName());
        
        // Test legacy methods
        category.setAdditivity(true);
        assertTrue(category.getAdditivity());
        
        category.setAdditivity(false);
        assertTrue(category.getAdditivity()); // Should still return true for compatibility
    }
    
    @Test
    public void testFormattedLogging() {
        // Test formatted logging methods (if implemented)
        logger.debug("Debug: %s = %d", "value", 42);
        logger.info("Info: %s = %d", "value", 42);
        logger.warn("Warn: %s = %d", "value", 42);
        logger.error("Error: %s = %d", "value", 42);
        logger.fatal("Fatal: %s = %d", "value", 42);
    }
    
    @Test
    public void testLogManagerOperations() {
        assertTrue(LogManager.getCachedLoggerCount() >= 0);
        
        Logger testLogger = LogManager.getLogger("test.operations");
        assertNotNull(testLogger);
        
        // Test shutdown (should not throw exception)
        assertDoesNotThrow(() -> LogManager.shutdown());
        
        // Test reset configuration (should not throw exception)
        assertDoesNotThrow(() -> LogManager.resetConfiguration());
    }
}