package csci4490.uno.dealer.response.account;

import com.google.gson.JsonElement;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.StaticUnoAccount;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_ACCOUNT_INFO} endpoint.
 */
public class AccountInfoResponse extends UnoDealerResponse {

    /**
     * The account will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     */
    public final @Nullable StaticUnoAccount account;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public AccountInfoResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement json = applicationJson.get("account");
            if(!json.isJsonNull()) {
                this.account = UnoJson.fromJson(json, StaticUnoAccount.class);
            } else {
                this.account = null;
            }
        } else {
            this.account = null;
        }
    }

}
