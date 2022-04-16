package csci4490.uno.dealer.endpoint;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link UUID} parameter present in an endpoint.
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
        String str = values.get(0);
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            String msg = "Invalid UUID: " + str;
            throw new BadRequestResponse(msg);
        }
    }

}
