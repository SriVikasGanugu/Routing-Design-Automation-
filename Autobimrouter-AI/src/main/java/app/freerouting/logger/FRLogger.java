package app.freerouting.logger;

import app.freerouting.Freerouting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/// <summary>
/// Provides logging functionality.
/// </summary>
public class FRLogger {

  // Formatting for floating-point numbers in logs
  public static final DecimalFormat defaultFloatFormat = new DecimalFormat("0.00");

  // Stores performance data for tracing methods
  private static final HashMap<Integer, Instant> perfData = new HashMap<>();
  private static final LogEntries logEntries = new LogEntries();
  private static Logger logger;
  private static boolean enabled = true;

  // List to hold custom log listeners
  private static final List<LogListener> logListeners = new ArrayList<>();

  /**
   * Adds a custom listener to receive log messages.
   *
   * @param listener The listener to add.
   */
  public static void addLogListener(LogListener listener) {
    logListeners.add(listener);
  }

  /**
   * Removes a custom listener.
   *
   * @param listener The listener to remove.
   */
  public static void removeLogListener(LogListener listener) {
    logListeners.remove(listener);
  }

  /**
   * Notifies all registered listeners about a log message.
   *
   * @param message The log message to notify.
   */
  private static void notifyListeners(String message) {
    for (LogListener listener : logListeners) {
      listener.onLogMessage(message);
    public static final DecimalFormat defaultFloatFormat = new DecimalFormat("0.00");
    private static final HashMap<Integer, Instant> perfData = new HashMap<>();
    private static final LogEntries logEntries = new LogEntries();
    private static Logger logger;
    private static boolean enabled = true;

    // List to hold custom log listeners
    private static final List<LogListener> logListeners = new ArrayList<>();

    // Add a listener to receive log messages
    public static void addLogListener(LogListener listener) {
        logListeners.add(listener);

    }


  /**
   * Formats a duration in seconds into a human-readable string.
   *
   * @param totalSeconds The duration in seconds.
   * @return A formatted duration string.
   */
  public static String formatDuration(double totalSeconds) {
    double seconds = totalSeconds;
    double minutes = seconds / 60.0;
    double hours = minutes / 60.0;

    hours = Math.floor(hours);
    minutes = Math.floor(minutes % 60.0);
    seconds = seconds % 60.0;

    return (hours > 0 ? (int) hours + " hour(s) " : "")
        + (minutes > 0 ? (int) minutes + " minute(s) " : "")
        + defaultFloatFormat.format(seconds) + " seconds";
  }

  /**
   * Records the start time for a performance trace.
   *
   * @param perfId A unique identifier for the trace.
   */
  public static void traceEntry(String perfId) {
    if (!enabled) {
      return;

    // Remove a listener
    public static void removeLogListener(LogListener listener) {
        logListeners.remove(listener);

    }


  /**
   * Records the end time for a performance trace and logs the duration.
   *
   * @param perfId A unique identifier for the trace.
   * @return The elapsed time in seconds.
   */
  public static double traceExit(String perfId) {
    return traceExit(perfId, null);
  }

  /**
   * Records the end time for a performance trace, logs the duration, and includes
   * a result.
   *
   * @param perfId A unique identifier for the trace.
   * @param result An optional result to include in the log.
   * @return The elapsed time in seconds.
   */
  public static double traceExit(String perfId, Object result) {
    if (!enabled) {
      return 0.0;
    }
    if (logger == null) {
      logger = LogManager.getLogger(Freerouting.class);
    }

    long timeElapsed = 0;
    try {
      timeElapsed = Duration.between(perfData.get(perfId.hashCode()), Instant.now()).toMillis();
    } catch (Exception e) {
      // Ignore exceptions for missing or invalid performance data

    // Notify all registered listeners about a log message
    private static void notifyListeners(String message) {
        for (LogListener listener : logListeners) {
            listener.onLogMessage(message);
        }
    }

    public static String formatDuration(double totalSeconds) {
        double seconds = totalSeconds;
        double minutes = seconds / 60.0;
        double hours = minutes / 60.0;

        hours = Math.floor(hours);
        minutes = Math.floor(minutes % 60.0);
        seconds = seconds % 60.0;

        return (hours > 0 ? (int) hours + " hour(s) " : "") + (minutes > 0 ? (int) minutes + " minute(s) " : "")
                + defaultFloatFormat.format(seconds) + " seconds";

    }

    public static void traceEntry(String perfId) {
        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        perfData.put(perfId.hashCode(), Instant.now());
    }

    public static double traceExit(String perfId) {
        if (!enabled) {
            return 0.0;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        return traceExit(perfId, null);
    }

    public static double traceExit(String perfId, Object result) {
        if (!enabled) {
            return 0.0;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }


  /**
   * Logs an informational message.
   *
   * @param msg The message to log.
   */
  public static void info(String msg) {
    logEntries.add(LogEntryType.Info, msg);

        long timeElapsed = 0;
        try {
            timeElapsed = Duration.between(perfData.get(perfId.hashCode()), Instant.now()).toMillis();
        } catch (Exception e) {
            // we can ignore this exception
        }


        perfData.remove(perfId.hashCode());
        if (timeElapsed < 0) {
            timeElapsed = 0;
        }

        String logMessage = "Method '" + perfId.replace("{}", result != null ? result.toString() : "(null)")
                + "' was performed in " + FRLogger.formatDuration(timeElapsed / 1000.0) + ".";


  /**
   * Logs a warning message.
   *
   * @param msg The message to log.
   */
  public static void warn(String msg) {
    logEntries.add(LogEntryType.Warning, msg);

        FRLogger.trace(logMessage);


        return timeElapsed / 1000.0;
    }


    logger.warn(msg);
  }

  /**
   * Logs a debug message.
   *
   * @param msg The message to log.
   */
  public static void debug(String msg) {
    logEntries.add(LogEntryType.Debug, msg);

    public static void info(String msg) {
        logEntries.add(LogEntryType.Info, msg);

        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }


        logger.info(msg);
        notifyListeners("INFO: " + msg);
    }


    logger.debug(msg);
  }

  /**
   * Logs an error message.
   *
   * @param msg The error message to log.
   * @param t   An optional throwable to include in the log.
   */
  public static void error(String msg, Throwable t) {
    logEntries.add(LogEntryType.Error, msg);

    public static void warn(String msg) {
        logEntries.add(LogEntryType.Warning, msg);

        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        logger.warn(msg);
        // notifyListeners("WARN: " + msg);
    }

    if (t == null) {
      logger.error(msg);
    } else {
      logger.error(msg, t);
    }
  }

  /**
   * Logs a trace message.
   *
   * @param msg The message to log.
   */
  public static void trace(String msg) {
    logEntries.add(LogEntryType.Trace, msg);
    public static void debug(String msg) {
        logEntries.add(LogEntryType.Debug, msg);

        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        logger.debug(msg);
        // notifyListeners("DEBUG: " + msg);
    }

    logger.trace(msg);
  }

  /**
   * Disables the logger.
   */
  public static void disableLogging() {
    enabled = false;
  }

  /**
   * Retrieves all logged entries.
   *
   * @return A {@link LogEntries} object containing logged entries.
   */
  public static LogEntries getLogEntries() {
    return logEntries;
  }

  /**
   * Changes the log level for the file logger.
   *
   * @param level The new log level.
   */
  public static void changeFileLogLevel(Level level) {
    // Obtain the LoggerContext
    var contextObject = LogManager.getContext(false);

    if (!(contextObject instanceof LoggerContext context)) {
      FRLogger.warn(
          "Failed to change the log level. The context object is not an instance of org.apache.logging.log4j.core.LoggerContext.");
      return;
    }

    Configuration config = context.getConfiguration();
    LoggerConfig rootLoggerConfig = config.getRootLogger();

    List<AppenderRef> refList = rootLoggerConfig.getAppenderRefs();
    var refs = refList.toArray(new AppenderRef[0]);
    for (int i = 0; i < refs.length; i++) {
      if (refs[i].getRef().equals("Console")) {
        refs[i] = AppenderRef.createAppenderRef("Console", level, null);
      }
    }

    rootLoggerConfig.removeAppender("Console");

    for (AppenderRef ref : refs) {
      rootLoggerConfig.addAppender(config.getAppender(ref.getRef()), ref.getLevel(), ref.getFilter());
    }

    context.updateLoggers();
  }

  /**
   * Changes the log level for the file logger using a string representation.
   *
   * @param level A string representing the new log level.
   */
  public static void changeFileLogLevel(String level) {
    String logLevel = level.toUpperCase();

    switch (logLevel) {
      case "OFF", "0" -> disableLogging();
      case "FATAL", "1" -> changeFileLogLevel(Level.FATAL);
      case "ERROR", "2" -> changeFileLogLevel(Level.ERROR);
      case "WARN", "3" -> changeFileLogLevel(Level.WARN);
      case "INFO", "4" -> changeFileLogLevel(Level.INFO);
      case "DEBUG", "5" -> changeFileLogLevel(Level.DEBUG);
      case "TRACE", "6" -> changeFileLogLevel(Level.TRACE);
      case "ALL", "7" -> changeFileLogLevel(Level.ALL);
=======
    public static void error(String msg, Throwable t) {
        logEntries.add(LogEntryType.Error, msg);

        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        if (t == null) {
            logger.error(msg);
        } else {
            logger.error(msg, t);
        }
        // notifyListeners("ERROR: " + msg);
    }

    public static void trace(String msg) {
        logEntries.add(LogEntryType.Trace, msg);

        if (!enabled) {
            return;
        }
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        logger.trace(msg);
        // notifyListeners("TRACE: " + msg);
    }

    /// <summary>
    /// Disables the log4j logger.
    /// </summary>
    public static void disableLogging() {
        enabled = false;
    }

    public static LogEntries getLogEntries() {
        return logEntries;
    }

    public static void changeFileLogLevel(Level level) {
        // Obtain the LoggerContext
        var contextObject = LogManager.getContext(false);

        // Check if the contextObject is an instance of
        // org.apache.logging.log4j.core.LoggerContext
        if (!(contextObject instanceof LoggerContext context)) {
            FRLogger.warn(
                    "Failed to change the log level. The context object is not an instance of org.apache.logging.log4j.core.LoggerContext.");
            return;
        }

        // Get the Configuration
        Configuration config = context.getConfiguration();

        // Get the Root LoggerConfig
        LoggerConfig rootLoggerConfig = config.getRootLogger();

        // Create a new AppenderRef with the desired level
        List<AppenderRef> refList = rootLoggerConfig.getAppenderRefs();
        var refs = refList.toArray(new AppenderRef[0]);
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getRef().equals("Console")) {
                refs[i] = AppenderRef.createAppenderRef("Console", level, null);
            }
        }

        // Remove the existing AppenderRefs
        rootLoggerConfig.removeAppender("Console");

        // Add the modified AppenderRef back to the LoggerConfig
        for (AppenderRef ref : refs) {
            rootLoggerConfig.addAppender(config.getAppender(ref.getRef()), ref.getLevel(), ref.getFilter());
        }

        // Update the configuration
        context.updateLoggers();
    }


  /**
   * Retrieves the logger instance.
   *
   * @return The logger instance.
   */
  public static Logger getLogger() {
    if (logger == null) {
      logger = LogManager.getLogger(Freerouting.class);
    }

    return logger;
  }
}
    public static void changeFileLogLevel(String level) {
        String logLevel = level.toUpperCase();

        if (logLevel.equals("OFF") || logLevel.equals("0")) {
            FRLogger.disableLogging();
        } else if (logLevel.equals("FATAL") || logLevel.equals("1")) {
            FRLogger.changeFileLogLevel(Level.FATAL);
        } else if (logLevel.equals("ERROR") || logLevel.equals("2")) {
            FRLogger.changeFileLogLevel(Level.ERROR);
        } else if (logLevel.equals("WARN") || logLevel.equals("3")) {
            FRLogger.changeFileLogLevel(Level.WARN);
        } else if (logLevel.equals("INFO") || logLevel.equals("4")) {
            FRLogger.changeFileLogLevel(Level.INFO);
        } else if (logLevel.equals("DEBUG") || logLevel.equals("5")) {
            FRLogger.changeFileLogLevel(Level.DEBUG);
        } else if (logLevel.equals("TRACE") || logLevel.equals("6")) {
            FRLogger.changeFileLogLevel(Level.TRACE);
        } else if (logLevel.equals("ALL") || logLevel.equals("7")) {
            FRLogger.changeFileLogLevel(Level.ALL);
        }
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = LogManager.getLogger(Freerouting.class);
        }

        return logger;
    }
}
