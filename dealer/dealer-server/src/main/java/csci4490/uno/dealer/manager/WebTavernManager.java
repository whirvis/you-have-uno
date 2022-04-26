package csci4490.uno.dealer.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.UnoTavern;
import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.endpoint.IntegerParameter;
import csci4490.uno.dealer.endpoint.ParameterType;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.List;

import static csci4490.uno.dealer.UnoEndpoints.*;

public class WebTavernManager {

    /* @formatter:off */
    private static final IntegerParameter
            PORT_PARAM = new IntegerParameter("port"),
            MAX_PARAM = new IntegerParameter("max");
    /* @formatter:on */

    private final @NotNull TavernManager manager;

    public WebTavernManager(@NotNull TavernManager manager) {
        this.manager = manager;
    }

    @Endpoint(type = HandlerType.POST, path = UNO_TAVERN_REGISTER)
    public void register(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        int port = PORT_PARAM.require(ctx, ParameterType.FORM);

        InetSocketAddress sockAddr = new InetSocketAddress(address, port);
        if (manager.isTavernRegistered(sockAddr)) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Tavern already registered");
            return;
        }

        UnoTavern tavern = manager.registerTavern(sockAddr);

        JsonObject response = new JsonObject();
        response.add("tavern", UnoJson.toJson(tavern));
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.POST, path = UNO_TAVERN_UNREGISTER)
    public void unregister(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        int port = PORT_PARAM.require(ctx, ParameterType.FORM);

        InetSocketAddress sockAddr = new InetSocketAddress(address, port);

        boolean unregistered = manager.unregisterTavern(sockAddr);

        JsonObject response = new JsonObject();
        response.addProperty("unregistered", unregistered);
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.POST, path = UNO_TAVERN_PING)
    public void ping(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        int port = PORT_PARAM.require(ctx, ParameterType.FORM);

        InetSocketAddress sockAddr = new InetSocketAddress(address, port);
        if (!manager.isTavernRegistered(sockAddr)) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("No such tavern");
            return;
        }

        manager.keepAlive(sockAddr);
    }

    @Endpoint(type = HandlerType.GET, path = UNO_TAVERN_LIST)
    public void list(@NotNull Context ctx) throws SQLException {
        int max = MAX_PARAM.get(ctx, ParameterType.QUERY, 10);

        List<UnoTavern> taverns = manager.getTaverns(max);

        JsonArray tavernsJson = new JsonArray();
        for (UnoTavern tavern : taverns) {
            tavernsJson.add(UnoJson.toJson(tavern));
        }

        JsonObject response = new JsonObject();
        response.add("taverns", tavernsJson);
        ctx.json(response);
    }

}
