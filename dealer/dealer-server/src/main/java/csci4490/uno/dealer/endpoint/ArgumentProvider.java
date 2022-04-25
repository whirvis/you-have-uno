package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides the arguments for a parameter.
 */
@FunctionalInterface
public interface ArgumentProvider {

    /**
     * @param key the parameter key.
     * @param ctx the request context.
     * @return the arguments for the parameter. If there are no arguments,
     * the returned list should be empty, not {@code null}.
     */
    @NotNull List<String> values(@NotNull String key, @NotNull Context ctx);

}
