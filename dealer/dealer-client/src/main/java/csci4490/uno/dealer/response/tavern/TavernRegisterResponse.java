package csci4490.uno.dealer.response.tavern;

import com.google.gson.JsonElement;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.StaticUnoTavern;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.UnoTavern;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_TAVERN_REGISTER} endpoint.
 */
public class TavernRegisterResponse extends UnoDealerResponse {

    /**
     * The tavern will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     */
    public final @Nullable UnoTavern tavern;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public TavernRegisterResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement visit = applicationJson.get("tavern");
            this.tavern = UnoJson.fromJson(visit, StaticUnoTavern.class);
        } else {
            this.tavern = null;
        }
    }

}
