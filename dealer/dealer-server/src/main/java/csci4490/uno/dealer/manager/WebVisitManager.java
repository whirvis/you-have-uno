package csci4490.uno.dealer.manager;

import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.endpoint.ParameterType;
import csci4490.uno.dealer.endpoint.StringParameter;
import csci4490.uno.dealer.endpoint.UUIDParameter;
import csci4490.uno.dealer.UnoVisit;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.UUID;

import static csci4490.uno.dealer.UnoEndpoints.*;

/**
 * Container for endpoint methods to access {@link VisitManager}
 * functionality via the web.
 */
public class WebVisitManager {

    /* @formatter:off */
    private static final StringParameter
            PASSWORD_PARAM = new StringParameter("password");

    private static final UUIDParameter
            UUID_PARAM = new UUIDParameter("uuid"),
            ACCESS_TOKEN_PARAM = new UUIDParameter("access_token"),
            SESSION_TOKEN_PARAM = new UUIDParameter("session_token");
    /* @formatter:on */

    private final @NotNull VisitManager manager;

    WebVisitManager(@NotNull VisitManager manager) {
        this.manager = manager;
    }

    @Endpoint(type = HandlerType.POST, path = UNO_VISIT_BEGIN)
    public void begin(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx, ParameterType.FORM);
        UUID accessToken = ACCESS_TOKEN_PARAM.require(ctx, ParameterType.FORM);

        UnoVisit visit = manager.beginVisit(address, uuid, accessToken);

        if (visit == null) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or access token");
            return;
        }

        JsonObject response = new JsonObject();
        response.add("visit", UnoJson.toJson(visit));
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.POST, path = UNO_VISIT_END)
    public void end(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx, ParameterType.FORM);
        UUID sessionToken = SESSION_TOKEN_PARAM.require(ctx,
                ParameterType.FORM);

        if (!manager.endVisit(address, uuid, sessionToken)) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or access token");
        }
    }

    @Endpoint(type = HandlerType.POST, path = UNO_VISIT_PING)
    public void ping(Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx, ParameterType.FORM);
        UUID sessionToken = SESSION_TOKEN_PARAM.require(ctx,
                ParameterType.FORM);

        if (!manager.verifySession(address, uuid, sessionToken)) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or session token");
            return;
        }

        manager.keepAlive(address, uuid, sessionToken);
    }

}
