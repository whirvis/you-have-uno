package csci4490.uno.dealer.manager;

import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.UnoGame;
import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.IntegerParameter;
import csci4490.uno.dealer.endpoint.ParameterType;
import csci4490.uno.dealer.endpoint.StringParameter;
import csci4490.uno.dealer.endpoint.UUIDParameter;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.UUID;

import static csci4490.uno.dealer.UnoEndpoints.*;

public class WebGameManager {

    /* @formatter:off */
    private static final UUIDParameter
            UUID_PARAM = new UUIDParameter("uuid"),
            LOBBY_ID_PARAM = new UUIDParameter("lobby_id");
    private static final StringParameter
            CODE_PARAM = new StringParameter("code"),
            TAVERN_IP_PARAM = new StringParameter("tavern_ip");
    private static final IntegerParameter
            TAVERN_PORT_PARAM = new IntegerParameter("tavern_port");
    /* @formatter:on */

    private GameManager manager;

    public WebGameManager(GameManager manager) {
        this.manager = manager;
    }

    @Endpoint(type = HandlerType.POST, path = UNO_GAME_CREATE)
    public void create(@NotNull Context ctx) throws SQLException {
        UUID host = UUID_PARAM.require(ctx, ParameterType.FORM);
        UUID lobbyId = LOBBY_ID_PARAM.require(ctx, ParameterType.FORM);

        String tavernIp = TAVERN_IP_PARAM.require(ctx, ParameterType.FORM);
        int tavernPort = TAVERN_PORT_PARAM.require(ctx, ParameterType.FORM);
        InetSocketAddress tavernAddress = new InetSocketAddress(tavernIp,
                tavernPort);

        UnoGame game = manager.createGame(host, tavernAddress, lobbyId);

        JsonObject response = new JsonObject();
        response.add("game", UnoJson.toJson(game));
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.GET, path = UNO_GAME_INFO)
    public void get(@NotNull Context ctx) throws SQLException {
        String code = CODE_PARAM.require(ctx, ParameterType.QUERY);

        UnoGame game = manager.getGame(code);

        JsonObject response = new JsonObject();
        response.add("game", UnoJson.toJson(game));
        ctx.json(response);
    }

}
