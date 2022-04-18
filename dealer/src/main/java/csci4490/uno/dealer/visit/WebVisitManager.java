package csci4490.uno.dealer.visit;

import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.endpoint.StringParameter;
import csci4490.uno.dealer.endpoint.UUIDParameter;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.UUID;

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

    @Endpoint(type = HandlerType.POST, path = "/uno/visit/begin")
    public void begin(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx);
        UUID accessToken = ACCESS_TOKEN_PARAM.require(ctx);

        UnoVisit visit = manager.beginVisit(address, uuid, accessToken);

        if (visit == null) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or access token");
            return;
        }

        ctx.json(visit);
    }

    @Endpoint(type = HandlerType.POST, path = "/uno/visit/ping")
    public void ping(Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx);
        UUID sessionToken = SESSION_TOKEN_PARAM.require(ctx);

        if (!manager.verifySession(address, uuid, sessionToken)) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or session token");
            return;
        }

        manager.keepAlive(address, uuid, sessionToken);
    }

}
