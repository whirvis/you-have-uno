package csci4490.uno.dealer.endpoint;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.HttpResponseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a UUI parameter that could be present in an endpoint.
 * Only the first argument for this parameter will be returned.
 */
public class UUIDParameter extends EndpointParameter<UUID> {

    /**
     * @param key the parameter key.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public UUIDParameter(@NotNull String key) {
        super(key);
    }

    @Override
    protected UUID decode(@NotNull Context ctx, @NotNull List<String> values) {
        try {
            return UUID.fromString(values.get(0));
        } catch (IllegalArgumentException e) {
            int status = HttpCode.BAD_REQUEST.getStatus();
            throw new HttpResponseException(status, e.getMessage());
        }
    }

}
