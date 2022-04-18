package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents an endpoint parameter type. Currently, this is used to
 * determine how the arguments for a parameter are retrieved.
 *
 * @see #getArgs(String, Context)
 */
public enum ParameterType {

    /**
     * Usually for {@code GET} endpoints.
     */
    QUERY((key, ctx) -> ctx.queryParams(key)),

    /**
     * Usually for {@code POST} endpoints.
     */
    FORM((key, ctx) -> ctx.formParams(key));

    private final ArgumentProvider provider;

    ParameterType(@NotNull ArgumentProvider provider) {
        this.provider = provider;
    }

    /**
     * @param key the parameter key.
     * @param ctx the request context.
     * @return the arguments for the parameter. If there are no arguments,
     * the returned list will be empty (and not {@code null}).
     * @throws NullPointerException if {@code key} or {@code ctx}
     *                              are {@code null}.
     */
    public @NotNull List<String> getArgs(@NotNull String key,
                                         @NotNull Context ctx) {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(ctx, "ctx cannot be null");
        return provider.values(key, ctx);
    }

}
