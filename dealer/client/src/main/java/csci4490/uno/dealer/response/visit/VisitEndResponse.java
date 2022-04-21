package csci4490.uno.dealer.response.visit;

import csci4490.uno.dealer.UnoEndpoints;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


/**
 * Contains the response of an UNO dealer server to the
 * {@value UnoEndpoints#UNO_VISIT_END} endpoint.
 *
 * @see #endedGracefully()
 */
public class VisitEndResponse extends UnoDealerResponse {

    private final boolean graceful;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public VisitEndResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        this.graceful = (status.getStatusCode() == HttpStatus.SC_OK);
    }

    /**
     * This will be {@code true} if, and only if, the status code of this
     * response is {@value HttpStatus#SC_OK}. If it is {@code false}, make
     * sure to check the status code.
     *
     * @return {@code true} if the status code of the response is
     * {@value HttpStatus#SC_OK}, {@code false} otherwise.
     */
    public boolean endedGracefully() {
        return this.graceful;
    }

}
