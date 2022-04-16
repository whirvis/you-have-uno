package csci4490.uno.dealer.endpoint;

import org.jetbrains.annotations.Nullable;

/**
 * Signals that an error relating to a {@link Endpoint} has occurred.
 */
@SuppressWarnings("unused")
public class EndpointException extends RuntimeException {

    /**
     * Constructs a new {@code EndpointException} with the specified detail
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
    public EndpointException(@Nullable String message,
                             @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code EndpointException} with the specified detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     */
    public EndpointException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new {@code EndpointException} with the specified cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method). A {@code null}
     *              value is permitted, and indicates that the cause is
     *              nonexistent or unknown.
     */
    public EndpointException(@Nullable Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code EndpointException} with no detail message.
     */
    public EndpointException() {
        super();
    }

}
