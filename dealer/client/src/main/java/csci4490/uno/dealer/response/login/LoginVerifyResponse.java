package csci4490.uno.dealer.response.login;

import com.google.gson.JsonElement;
import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_LOGIN_VERIFY} endpoint.
 *
 * @see #wasVerified()
 */
public class LoginVerifyResponse extends UnoDealerResponse {

    private final boolean verified;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public LoginVerifyResponse(@NotNull HttpResponse response) throws IOException {
        super(response);
        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement verified = applicationJson.get("verified");
            this.verified = verified.getAsBoolean();
        } else {
            this.verified = false;
        }
    }

    /**
     * This will be {@code true} if, and only if, the status code of this
     * response is {@value HttpStatus#SC_OK}. If it is {@code false}, make
     * sure to check the status code.
     *
     * @return {@code true} if the status code of the response is
     * {@value HttpStatus#SC_OK}, {@code false} otherwise.
     */
    public boolean wasVerified() {
        return this.verified;
    }

}
