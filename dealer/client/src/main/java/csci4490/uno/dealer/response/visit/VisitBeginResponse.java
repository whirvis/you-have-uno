package csci4490.uno.dealer.response.visit;

import com.google.gson.JsonElement;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.StaticUnoVisit;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.UnoVisit;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_VISIT_BEGIN} endpoint.
 */
public class VisitBeginResponse extends UnoDealerResponse {

    /**
     * The visit will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     */
    public final @Nullable UnoVisit visit;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public VisitBeginResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement visit = applicationJson.get("visit");
            this.visit = UnoJson.fromJson(visit, StaticUnoVisit.class);
        } else {
            this.visit = null;
        }
    }

}
