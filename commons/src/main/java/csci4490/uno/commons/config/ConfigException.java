package csci4490.uno.commons.config;

import org.jetbrains.annotations.Nullable;

/**
 * Signals that an error relating to a {@link Config} has occurred.
 */
public class ConfigException extends RuntimeException {

    /**
     * Constructs a new {@code ConfigException} with the specified detail
     * message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method). A {@code null}
     *                value is permitted, and indicates that the cause is
     *                nonexistent or unknown.
     */
    public ConfigException(@Nullable String message,
                           @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ConfigException} with the specified detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     */
    public ConfigException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ConfigException} with the specified cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method). A {@code null}
     *              value is permitted, and indicates that the cause is
     *              nonexistent or unknown.
     */
    public ConfigException(@Nullable Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code ConfigException} with no detail message.
     */
    public ConfigException() {
        super();
    }

}
