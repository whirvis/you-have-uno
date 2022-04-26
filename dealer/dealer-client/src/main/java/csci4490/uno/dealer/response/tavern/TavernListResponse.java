package csci4490.uno.dealer.response.tavern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.StaticUnoTavern;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.UnoTavern;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_TAVERN_LIST} endpoint.
 */
public class TavernListResponse extends UnoDealerResponse {

    /**
     * The tavern will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     */
    public final List<UnoTavern> taverns;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public TavernListResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            List<UnoTavern> taverns = new ArrayList<>();
            JsonArray tavernsJson = applicationJson.getAsJsonArray("taverns");
            for (JsonElement element : tavernsJson) {
                taverns.add(UnoJson.fromJson(element, StaticUnoTavern.class));
            }
            this.taverns = Collections.unmodifiableList(taverns);
        } else {
            this.taverns = null;
        }
    }

}
