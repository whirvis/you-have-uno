package csci4490.uno.dealer.endpoint;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a {@code int} parameter present in an endpoint.
 * Only the first argument for this parameter will be returned.
 */
public class IntegerParameter extends EndpointParameter<Integer> {

    /**
     * @param key the parameter key.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public IntegerParameter(@NotNull String key) {
        super(key);
    }

    @Override
    protected Integer decode(@NotNull Context ctx, @NotNull List<String> args) {
        String str = args.get(0);
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            String msg = "Not an integer: " + str;
            throw new BadRequestResponse(msg);
        }
    }

}
