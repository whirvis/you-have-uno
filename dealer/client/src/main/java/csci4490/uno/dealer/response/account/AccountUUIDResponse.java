package csci4490.uno.dealer.response.account;

import com.google.gson.JsonElement;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_ACCOUNT_UUID} endpoint.
 *
 * @see #getUUID()
 */
public class AccountUUIDResponse extends UnoDealerResponse {

    private final @Nullable UUID uuid;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public AccountUUIDResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement json = applicationJson.get("uuid");
            this.uuid = UUID.fromString(json.getAsString());
        } else {
            this.uuid = null;
        }
    }

    /**
     * The UUID will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     *
     * @return the UUID, {@code null} if the status code of the response
     * is not {@value HttpStatus#SC_OK}.
     */
    public @Nullable UUID getUUID() {
        return this.uuid;
    }

}
