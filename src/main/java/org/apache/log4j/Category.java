package org.apache.log4j;

/**
 * Log4j Category compatibility class.
 * In older versions of log4j, Category was the parent class of Logger.
 * This maintains backward compatibility for legacy applications.
 */
public class Category extends Logger {
    
    protected Category(String name) {
        super(name);
    }
    
    /**
     * Get a category for the specified class.
     */
    public static Category getInstance(Class<?> clazz) {
        return new Category(clazz.getName());
    }
    
    /**
     * Get a category for the specified name.
     */
    public static Category getInstance(String name) {
        return new Category(name);
    }
    
    /**
     * Get the root category.
     */
    public static Category getRoot() {
        return new Category("ROOT");
    }
    
    // Additional legacy methods that some old applications might use
    
    /**
     * Legacy method - same as isDebugEnabled().
     */
    public boolean isDebugEnabled() {
        return super.isDebugEnabled();
    }
    
    /**
     * Legacy method - same as isInfoEnabled().
     */
    public boolean isInfoEnabled() {
        return super.isInfoEnabled();
    }
    
    /**
     * Legacy method for setting additivity (not applicable in log4Rich).
     */
    public void setAdditivity(boolean additivity) {
        // log4Rich doesn't have additivity concept, so this is a no-op
    }
    
    /**
     * Legacy method for getting additivity (always returns true for compatibility).
     */
    public boolean getAdditivity() {
        // log4Rich doesn't have additivity concept, return true for compatibility
        return true;
    }
}