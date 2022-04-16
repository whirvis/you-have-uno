package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a {@code String} parameter present in an endpoint.
 * Only the first argument for this parameter will be returned.
 */
public class StringParameter extends EndpointParameter<String> {

    /**
     * @param key       the parameter key.
     * @param validator the argument validator. A value of {@code null}
     *                  is permitted, and results in no validation for
     *                  the argument.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public StringParameter(@NotNull String key,
                           @Nullable ArgumentValidator validator) {
        super(key, validator);
    }

    /**
     * Constructs a new {@code StringParameter} with no argument validator.
     *
     * @param key the parameter key.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public StringParameter(@NotNull String key) {
        super(key);
    }

    @Override
    protected String decode(@NotNull Context ctx,
                            @NotNull List<String> values) {
        return values.get(0);
    }

}
