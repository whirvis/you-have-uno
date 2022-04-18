package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An argument validator for an {@link EndpointParameter}. This may throw an
 * {@code HttpResponseException} to have the webserver respond to the client
 * with an appropriate error code and message.
 */
@FunctionalInterface
public interface ArgumentValidator {

    /**
     * @param ctx  the request context.
     * @param args the arguments provided.
     */
    void validate(@NotNull Context ctx, @NotNull List<String> args);

}
