# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

log4j-log4Rich is a drop-in replacement bridge for Apache log4j 1.x that provides ultra-high-performance logging by delegating to the log4Rich framework underneath. The project maintains 100% API compatibility with log4j 1.x while delivering up to 23x performance improvement.

## Development Commands

### Build and Package
```bash
# Compile, test, and create shaded JAR
mvn clean package

# Compile without running tests
mvn clean compile

# Create only the shaded JAR (skip tests)
mvn clean package -DskipTests
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=Log4jCompatibilityTest

# Run performance tests specifically
mvn test -Dtest=PerformanceComparisonTest

# Run tests with verbose output
mvn test -Dtest=Log4jCompatibilityTest -Dmaven.surefire.debug=true
```

### Development
```bash
# Generate source JAR
mvn source:jar

# Generate Javadoc
mvn javadoc:jar

# Install to local Maven repository
mvn install

# Clean all build artifacts
mvn clean
```

## Architecture

### Core Structure
The project implements a bridge pattern with these key components:

1. **Bridge Layer** (`src/main/java/org/apache/log4j/`):
   - `Logger.java` - Main logger class that delegates to log4Rich
   - `LogManager.java` - Factory for logger creation with caching
   - `Level.java` - Log level enumeration with log4Rich mapping
   - `Priority.java` - Legacy priority system (for compatibility)
   - `Category.java` - Legacy Category class (extends Logger)
   - `PropertyConfigurator.java` - Configuration system for log4j.properties

2. **Demo Application** (`src/main/java/org/apache/log4j/demo/`):
   - `Log4jCompatibilityDemo.java` - Demonstrates API compatibility

3. **Test Suite** (`src/test/java/org/apache/log4j/`):
   - `Log4jCompatibilityTest.java` - Comprehensive compatibility tests
   - `PerformanceComparisonTest.java` - Performance benchmarking

### Key Design Patterns

- **Facade Pattern**: log4j classes provide familiar API while delegating to log4Rich
- **Factory Pattern**: LogManager creates and caches Logger instances
- **Adapter Pattern**: Level and Priority classes map log4j concepts to log4Rich equivalents

### Dependencies

The project uses Maven Shade Plugin to create a fat JAR containing:
- **log4Rich 1.0.4** - The underlying high-performance logging framework
- All transitive dependencies bundled to avoid classpath conflicts

## Code Conventions

### Java Version
- Target: Java 8+ (maintains broad compatibility)
- Encoding: UTF-8
- Style: Standard Java conventions with clear javadoc comments

### Maven Configuration
- Uses standard Maven directory layout
- Shade plugin creates single deployable JAR at `target/log4j-log4Rich.jar`
- Source and Javadoc JARs generated for distribution

### Testing Approach
- JUnit 5 for all tests
- Comprehensive compatibility testing ensures log4j 1.x behaviors work identically
- Performance tests validate speed improvements
- No external test dependencies required

## Important Files

- `pom.xml` - Maven build configuration with shading setup
- `src/main/java/org/apache/log4j/Logger.java` - Core logger implementation
- `src/main/java/org/apache/log4j/LogManager.java` - Logger factory with caching
- `src/test/java/org/apache/log4j/Log4jCompatibilityTest.java` - Main test suite

## Performance Notes

The bridge maintains log4Rich's performance advantages:
- Logger instances are cached in LogManager for efficiency
- Level checking delegates directly to log4Rich's optimized implementations
- Message formatting uses log4Rich's high-performance string handling
- All logging operations are zero-allocation when possible

## Testing Strategy

When working with this codebase:
1. Run compatibility tests after any bridge layer changes
2. Run performance tests to ensure no regression
3. Test with sample applications to verify drop-in replacement works
4. Verify shaded JAR contains all required dependencies

## Configuration

The project automatically translates log4j.properties files to log4Rich configuration. Standard log4j configuration patterns work without modification, while advanced log4Rich features can be enabled through additional properties.