# log4j2-log4Rich Bridge Architecture for Code Reuse

## Core Principle: Single Responsibility, Maximum Reuse

Instead of implementing 180+ individual methods, we'll create a layered architecture with shared utility classes that handle the common operations once.

## Architecture Layers

### Layer 1: Core Utilities (Write Once, Use Everywhere)

#### MessageExtractor
```java
public class MessageExtractor {
    public static String extractMessage(Object messageObj, Object... params) {
        if (messageObj instanceof String) {
            return formatParameterizedMessage((String) messageObj, params);
        } else if (messageObj instanceof Message) {
            return ((Message) messageObj).getFormattedMessage();
        } else if (messageObj instanceof Supplier) {
            return String.valueOf(((Supplier<?>) messageObj).get());
        } else {
            return String.valueOf(messageObj);
        }
    }
    
    private static String formatParameterizedMessage(String pattern, Object... params) {
        // Single implementation of {} parameter substitution
        // Handles: "User {} has {} items" with ["john", 5]
        // Returns: "User john has 5 items"
    }
}
```

#### MarkerHandler
```java
public class MarkerHandler {
    public static String extractMarkerInfo(Marker marker) {
        if (marker == null) return null;
        // Single implementation of marker extraction
        // Handles parent markers, nested markers, etc.
        return marker.getName(); // + parent info if needed
    }
    
    public static boolean shouldLog(Marker marker, Level level) {
        // Single implementation of marker-based filtering
        return true; // For now, always log
    }
}
```

#### LevelTranslator
```java
public class LevelTranslator {
    private static final Map<Level, com.log4rich.core.Level> LEVEL_MAP = Map.of(
        Level.TRACE, com.log4rich.core.Level.TRACE,
        Level.DEBUG, com.log4rich.core.Level.DEBUG,
        Level.INFO, com.log4rich.core.Level.INFO,
        Level.WARN, com.log4rich.core.Level.WARN,
        Level.ERROR, com.log4rich.core.Level.ERROR,
        Level.FATAL, com.log4rich.core.Level.FATAL
    );
    
    public static com.log4rich.core.Level translate(Level log4j2Level) {
        return LEVEL_MAP.get(log4j2Level);
    }
}
```

#### ContextBridge
```java
public class ContextBridge {
    public static void applyThreadContext() {
        // Single implementation of ThreadContext → log4Rich context mapping
        Map<String, String> context = ThreadContext.getContext();
        // Apply to log4Rich context
    }
}
```

### Layer 2: Core Logging Engine (Single Implementation)

#### LoggingEngine
```java
public class LoggingEngine {
    
    /**
     * THE central logging method - all 180+ methods funnel through here
     */
    public static void log(com.log4rich.core.Logger log4RichLogger, 
                          Level level, 
                          Marker marker, 
                          Object message, 
                          Throwable throwable, 
                          Object... params) {
        
        // 1. Check if logging is enabled (performance optimization)
        com.log4rich.core.Level richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        // 2. Check marker-based filtering
        if (!MarkerHandler.shouldLog(marker, level)) {
            return;
        }
        
        // 3. Extract final message string
        String finalMessage = MessageExtractor.extractMessage(message, params);
        
        // 4. Add marker info if present
        if (marker != null) {
            String markerInfo = MarkerHandler.extractMarkerInfo(marker);
            finalMessage = "[" + markerInfo + "] " + finalMessage;
        }
        
        // 5. Apply thread context
        ContextBridge.applyThreadContext();
        
        // 6. Log to log4Rich (single call point)
        if (throwable != null) {
            log4RichLogger.log(richLevel, finalMessage, throwable);
        } else {
            log4RichLogger.log(richLevel, finalMessage);
        }
    }
}
```

### Layer 3: Logger Implementation (Thin Wrappers)

#### Logger Class
```java
public class Logger {
    private final com.log4rich.core.Logger log4RichLogger;
    
    // ALL 180+ methods become simple one-liners:
    
    public void trace(String message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null);
    }
    
    public void trace(String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null, param);
    }
    
    public void trace(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null, params);
    }
    
    public void trace(String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, throwable);
    }
    
    public void trace(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }
    
    public void trace(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, params);
    }
    
    public void trace(Message message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null);
    }
    
    public void trace(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, supplier, null);
    }
    
    // Copy/paste pattern for debug, info, warn, error, fatal
    // Just change Level.TRACE → Level.DEBUG, etc.
}
```

## Code Generation Strategy

Since we have the same pattern for 6 log levels, we can:

1. **Write the utilities once** (MessageExtractor, MarkerHandler, etc.)
2. **Write LoggingEngine once** (single central logging method)
3. **Generate the Logger methods** using templates:

```java
// Template for all levels
public void ${LEVEL}(String message) {
    LoggingEngine.log(log4RichLogger, Level.${UPPER_LEVEL}, null, message, null);
}

public void ${LEVEL}(String message, Object param) {
    LoggingEngine.log(log4RichLogger, Level.${UPPER_LEVEL}, null, message, null, param);
}

// Generate for: trace/TRACE, debug/DEBUG, info/INFO, warn/WARN, error/ERROR, fatal/FATAL
```

## Benefits of This Architecture

### 1. Single Point of Truth
- Message formatting logic: **1 place** (MessageExtractor)
- Marker handling logic: **1 place** (MarkerHandler)  
- Level translation: **1 place** (LevelTranslator)
- Core logging flow: **1 place** (LoggingEngine)

### 2. Easy Testing
- Test MessageExtractor with various inputs once
- Test LoggingEngine with all combinations once
- Logger methods become trivial wrappers (minimal testing needed)

### 3. Easy Debugging
- All logging flows through LoggingEngine.log()
- Single breakpoint catches all logging calls
- Easy to add performance monitoring, metrics, etc.

### 4. Easy Maintenance
- Bug fixes in one place benefit all 180+ methods
- Feature additions (like custom formatting) added once
- Performance optimizations applied universally

### 5. Clear Separation of Concerns
- **Layer 1**: Handles log4j2 → log4Rich data conversion
- **Layer 2**: Handles logging logic and performance
- **Layer 3**: Provides log4j2 API compatibility

## Implementation Order

1. **Create utilities** (MessageExtractor, MarkerHandler, LevelTranslator)
2. **Create LoggingEngine** (central logging method)
3. **Generate Logger methods** (using templates/code generation)
4. **Add advanced features** (Fluent API, Configuration) as needed

## Testing Strategy

Instead of testing 180+ methods individually:

1. **Unit test utilities** extensively (MessageExtractor, MarkerHandler)
2. **Integration test LoggingEngine** with various combinations
3. **Smoke test Logger methods** (few samples to ensure wiring works)
4. **Performance test** the central LoggingEngine.log() method

This architecture turns a 180-method implementation nightmare into a manageable, maintainable solution with shared code and single points of responsibility.