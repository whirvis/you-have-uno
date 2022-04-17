package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.HttpResponseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a parameter that could be present in an endpoint. These
 * represent single parameters, but can contain multiple values.
 *
 * @param <T> the parameter value type.
 * @see #get(Context, Object)
 * @see #require(Context)
 */
public abstract class EndpointParameter<T> {

    private final String key;
    private final ArgumentValidator validator;

    /**
     * @param key       the parameter key.
     * @param validator the argument validator. A value of {@code null}
     *                  is permitted, and results in no validation for
     *                  the argument.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public EndpointParameter(@NotNull String key,
                             @Nullable ArgumentValidator validator) {
        this.key = Objects.requireNonNull(key, "key cannot be null");
        this.validator = validator;
    }

    /**
     * Constructs a new {@code EndpointParameter} with no argument validator.
     *
     * @param key the parameter key.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public EndpointParameter(@NotNull String key) {
        this(key, null);
    }

    public final @NotNull String getKey() {
        return this.key;
    }

    public final boolean hasValidator() {
        return this.validator != null;
    }

    /**
     * Decodes the arguments for the parameter and returns them. While
     * multiple arguments <i>can</i> be specified for a single parameter,
     * often only one is used. To support multiple arguments, the return
     * type must support containing them.
     * <p>
     * <b>Note:</b> If an error occurs while decoding <i>due to a invalid
     * argument</i>, make sure to throw an {@code HttpResponseException}.
     * This ensures the webserver will respond to the client with an
     * appropriate error code and message.
     *
     * @param ctx    the context of the request.
     * @param values the values provided for the parameter.
     * @return the decoded argument.
     */
    protected abstract T decode(@NotNull Context ctx,
                                @NotNull List<String> values);

    private T validate(@NotNull Context ctx, @NotNull List<String> values,
                       boolean skipValidation) {
        if (validator != null && !skipValidation) {
            validator.validate(ctx, values);
        }
        return this.decode(ctx, values);
    }

    /**
     * Searches for the parameter in the specified request. If it is not
     * present, the specified fallback value is returned.
     *
     * @param ctx            the context of the request.
     * @param skipValidation {@code true} to skip validation for the
     *                       parameter argument, {@code false} otherwise.
     * @return the argument for the parameter, {@code fallback} if it is not
     * present in the request.
     * @throws NullPointerException if {@code ctx} is {@code null}.
     */
    public final T get(@NotNull Context ctx, @Nullable T fallback,
                       boolean skipValidation) {
        Objects.requireNonNull(ctx, "ctx cannot be null");

        Map<String, List<String>> params = ctx.queryParamMap();
        if (!params.containsKey(key)) {
            return fallback;
        }

        List<String> values = params.get(key);
        return this.validate(ctx, values, skipValidation);
    }

    /**
     * Searches for the parameter in the specified request. If it is not
     * present, the specified fallback value is returned.
     *
     * @param ctx the context of the request.
     * @return the argument for the parameter, {@code fallback} if it is not
     * present in the request.
     * @throws NullPointerException if {@code ctx} is {@code null}.
     */
    public final T get(@NotNull Context ctx, @Nullable T fallback) {
        return this.get(ctx, fallback, false);
    }

    /**
     * Requires that the parameter be present in the specified request.
     *
     * @param ctx            the context of the request.
     * @param skipValidation {@code true} to skip validation for the
     *                       parameter argument, {@code false} otherwise.
     * @return the argument for the parameter.
     * @throws NullPointerException  if {@code ctx} is {@code null}.
     * @throws HttpResponseException if the parameter is not present.
     */
    public final T require(@NotNull Context ctx, boolean skipValidation) {
        Objects.requireNonNull(ctx, "ctx cannot be null");

        Map<String, List<String>> params = ctx.queryParamMap();
        if (!params.containsKey(key)) {
            int code = HttpCode.BAD_REQUEST.getStatus();
            String msg = "Missing required parameter";
            msg += " \"" + key + "\"";
            throw new HttpResponseException(code, msg);
        }

        List<String> values = params.get(key);
        return this.validate(ctx, values, skipValidation);
    }


    /**
     * Requires that the parameter be present in the specified request.
     *
     * @param ctx the context of the request.
     * @return the argument for the parameter.
     * @throws NullPointerException  if {@code ctx} is {@code null}.
     * @throws HttpResponseException if the parameter is not present.
     */
    public final T require(@NotNull Context ctx) {
        return this.require(ctx, false);
    }

}
