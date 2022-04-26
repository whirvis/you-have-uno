package csci4490.uno.dealer.response.game;

import com.google.gson.JsonElement;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.StaticUnoGame;
import csci4490.uno.dealer.UnoGame;
import csci4490.uno.dealer.response.UnoDealerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GameInfoResponse extends UnoDealerResponse {

    /**
     * The visit will not be {@code null} if, and only if, the status code
     * of this response is {@value HttpStatus#SC_OK}. If it is {@code null},
     * make sure to check the status code.
     */
    public final @Nullable UnoGame game;

    /**
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public GameInfoResponse(@NotNull HttpResponse response) throws IOException {
        super(response);

        if (status.getStatusCode() == HttpStatus.SC_OK) {
            JsonElement game = applicationJson.get("game");
            this.game = UnoJson.fromJson(game, StaticUnoGame.class);
        } else {
            this.game = null;
        }
    }

}
