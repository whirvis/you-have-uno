package csci4490.uno.dealer.manager;

import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.endpoint.ParameterType;
import csci4490.uno.dealer.endpoint.StringParameter;
import csci4490.uno.dealer.endpoint.UUIDParameter;
import csci4490.uno.dealer.UnoLogin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.UUID;

import static csci4490.uno.dealer.UnoEndpoints.*;

/**
 * Container for endpoint methods to access {@link LoginManager}
 * functionality via the web.
 */
public class WebLoginManager {

    /* @formatter:off */
    private static final StringParameter
            PASSWORD_PARAM = new StringParameter("password");

    private static final UUIDParameter
            UUID_PARAM = new UUIDParameter("uuid"),
            ACCESS_TOKEN_PARAM = new UUIDParameter("access_token");
    /* @formatter:on */

    private final @NotNull LoginManager manager;

    WebLoginManager(@NotNull LoginManager manager) {
        this.manager = manager;
    }

    @Endpoint(type = HandlerType.POST, path = UNO_LOGIN)
    public void login(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx, ParameterType.FORM);
        String password = PASSWORD_PARAM.require(ctx, ParameterType.FORM);

        UnoLogin login = manager.loginAccount(address, uuid, password);

        if (login == null) {
            ctx.status(HttpCode.FORBIDDEN);
            ctx.result("Invalid UUID or password");
            return;
        }

        JsonObject response = new JsonObject();
        response.add("login", UnoJson.toJson(login));
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.POST, path = UNO_LOGIN_VERIFY)
    public void verify(@NotNull Context ctx) throws SQLException {
        InetAddress address = Endpoints.getAddress(ctx);
        UUID uuid = UUID_PARAM.require(ctx, ParameterType.FORM);
        UUID accessToken = ACCESS_TOKEN_PARAM.require(ctx, ParameterType.FORM);

        boolean verified = manager.verifyAccess(address, uuid, accessToken);

        JsonObject response = new JsonObject();
        response.addProperty("verified", verified);
        ctx.json(response);
    }

}
