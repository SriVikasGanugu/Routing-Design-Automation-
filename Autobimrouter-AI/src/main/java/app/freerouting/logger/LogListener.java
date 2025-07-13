package app.freerouting.logger;

/**
 * An interface for classes that want to receive log updates.
 */
public interface LogListener {
    /**
     * Called whenever a new log message is generated.
     *
     * @param message The log message to process.
     */
    void onLogMessage(String message);
}
