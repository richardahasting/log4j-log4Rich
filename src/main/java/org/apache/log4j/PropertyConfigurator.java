package org.apache.log4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * Log4j PropertyConfigurator compatibility class.
 * Provides basic configuration translation from log4j properties to log4Rich configuration.
 */
public class PropertyConfigurator {
    
    /**
     * Configure log4j from properties file in classpath.
     */
    public static void configure(String resourceName) {
        try {
            InputStream is = PropertyConfigurator.class.getClassLoader().getResourceAsStream(resourceName);
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                configure(props);
                is.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to configure from " + resourceName + ": " + e.getMessage());
        }
    }
    
    /**
     * Configure log4j from properties object.
     */
    public static void configure(Properties properties) {
        try {
            Log4jToLog4RichConfigTranslator.translateAndApply(properties);
        } catch (Exception e) {
            System.err.println("Failed to configure from properties: " + e.getMessage());
        }
    }
    
    /**
     * Configure log4j from properties file path.
     */
    public static void configureAndWatch(String configFilename) {
        configure(configFilename);
        // Note: log4Rich handles file watching differently, so we don't implement the watching here
    }
    
    /**
     * Configure log4j from properties file path with watch interval.
     */
    public static void configureAndWatch(String configFilename, long delay) {
        configure(configFilename);
        // Note: log4Rich handles file watching differently, so we don't implement the watching here
    }
}

/**
 * Internal class to translate log4j properties to log4Rich configuration.
 */
class Log4jToLog4RichConfigTranslator {
    
    public static void translateAndApply(Properties log4jProps) {
        Properties log4RichProps = new Properties();
        
        // Translate root logger
        String rootLogger = log4jProps.getProperty("log4j.rootLogger");
        if (rootLogger != null) {
            String[] parts = rootLogger.split(",");
            if (parts.length > 0) {
                String level = parts[0].trim().toUpperCase();
                log4RichProps.setProperty("log4rich.rootLevel", level);
            }
            
            // Check for console appender
            boolean hasConsole = false;
            for (int i = 1; i < parts.length; i++) {
                if (parts[i].trim().toLowerCase().contains("console")) {
                    hasConsole = true;
                    break;
                }
            }
            log4RichProps.setProperty("log4rich.console.enabled", String.valueOf(hasConsole));
        }
        
        // Translate specific logger levels
        for (Object key : log4jProps.keySet()) {
            String keyStr = key.toString();
            if (keyStr.startsWith("log4j.logger.")) {
                String loggerName = keyStr.substring("log4j.logger.".length());
                String value = log4jProps.getProperty(keyStr);
                if (value != null) {
                    String[] parts = value.split(",");
                    if (parts.length > 0) {
                        String level = parts[0].trim().toUpperCase();
                        log4RichProps.setProperty("log4rich.logger." + loggerName, level);
                    }
                }
            }
        }
        
        // Translate console appender properties
        translateConsoleAppender(log4jProps, log4RichProps);
        
        // Translate file appender properties
        translateFileAppender(log4jProps, log4RichProps);
        
        // Apply the translated configuration to log4Rich
        applyLog4RichConfiguration(log4RichProps);
    }
    
    private static void translateConsoleAppender(Properties log4jProps, Properties log4RichProps) {
        // Look for console appender configuration
        String target = log4jProps.getProperty("log4j.appender.console.Target");
        if (target != null) {
            log4RichProps.setProperty("log4rich.console.target", target.toUpperCase());
        }
        
        String pattern = log4jProps.getProperty("log4j.appender.console.layout.ConversionPattern");
        if (pattern != null) {
            String log4RichPattern = translatePattern(pattern);
            log4RichProps.setProperty("log4rich.console.pattern", log4RichPattern);
        }
    }
    
    private static void translateFileAppender(Properties log4jProps, Properties log4RichProps) {
        // Look for file appender configuration
        String fileName = log4jProps.getProperty("log4j.appender.file.File");
        if (fileName != null) {
            log4RichProps.setProperty("log4rich.file.path", fileName);
        }
        
        String maxFileSize = log4jProps.getProperty("log4j.appender.file.MaxFileSize");
        if (maxFileSize != null) {
            log4RichProps.setProperty("log4rich.file.maxSize", maxFileSize);
        }
        
        String maxBackupIndex = log4jProps.getProperty("log4j.appender.file.MaxBackupIndex");
        if (maxBackupIndex != null) {
            log4RichProps.setProperty("log4rich.file.maxBackups", maxBackupIndex);
        }
        
        String pattern = log4jProps.getProperty("log4j.appender.file.layout.ConversionPattern");
        if (pattern != null) {
            String log4RichPattern = translatePattern(pattern);
            log4RichProps.setProperty("log4rich.file.pattern", log4RichPattern);
        }
    }
    
    private static String translatePattern(String log4jPattern) {
        // Basic pattern translation from log4j to log4Rich format
        if (log4jPattern == null) {
            return "[%level] %date{yyyy-MM-dd HH:mm:ss} [%thread] %class.%method:%line - %message%n";
        }
        
        String pattern = log4jPattern;
        
        // Translate common log4j pattern elements to log4Rich equivalents
        pattern = pattern.replace("%d{yyyy-MM-dd HH:mm:ss}", "%date{yyyy-MM-dd HH:mm:ss}");
        pattern = pattern.replace("%d", "%date{yyyy-MM-dd HH:mm:ss}");
        pattern = pattern.replace("%p", "%level");
        pattern = pattern.replace("%c", "%class");
        pattern = pattern.replace("%t", "%thread");
        pattern = pattern.replace("%m", "%message");
        pattern = pattern.replace("%l", "%class.%method:%line");
        pattern = pattern.replace("%M", "%method");
        pattern = pattern.replace("%L", "%line");
        
        return pattern;
    }
    
    private static void applyLog4RichConfiguration(Properties log4RichProps) {
        try {
            // Apply basic configuration directly to log4Rich
            String rootLevel = log4RichProps.getProperty("log4rich.rootLevel");
            if (rootLevel != null) {
                com.log4rich.core.LogLevel level = com.log4rich.core.LogLevel.valueOf(rootLevel.toUpperCase());
                com.log4rich.config.ConfigurationManager.setRootLevel(level);
            }
            
            // Apply other configuration properties as needed
            // For now, we focus on the basic properties translation
            System.out.println("Applied log4Rich configuration with " + log4RichProps.size() + " properties");
        } catch (Exception e) {
            System.err.println("Failed to apply log4Rich configuration: " + e.getMessage());
        }
    }
}