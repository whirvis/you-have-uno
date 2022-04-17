package csci4490.uno.dealer.account;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import csci4490.uno.dealer.UnoDealer;
import csci4490.uno.dealer.endpoint.Endpoint;
import csci4490.uno.dealer.endpoint.StringParameter;
import csci4490.uno.dealer.endpoint.UUIDParameter;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Container for endpoint methods to access {@link AccountManager}
 * functionality via the web.
 */
public class WebAccountManager {

    /* @formatter:off */
    private static final StringParameter
            USERNAME_PARAM = new StringParameter("username",
                    WebAccountManager::validateUsername),
            PASSWORD_PARAM = new StringParameter("password",
                    WebAccountManager::validatePassword);

    private static final UUIDParameter
            UUID_PARAM = new UUIDParameter("uuid");
    /* @formatter:on */

    private static final int USERNAME_MAX_LENGTH = 16;
    private static final int PASSWORD_MAX_LENGTH = 128;
    private static final String USERNAME_ALPHABET = "[a-zA-Z0-9_]+";

    private static void validateUsername(@NotNull Context ctx,
                                         @NotNull List<String> values) {
        String username = values.get(0);

        if (username.isEmpty()) {
            String msg = "Username cannot be empty";
            throw new BadRequestResponse(msg);
        }

        if (!username.matches(USERNAME_ALPHABET)) {
            String msg = "Username must be alphanumeric";
            throw new BadRequestResponse(msg);
        }

        /* TODO: check for profanity */

        if (username.length() > USERNAME_MAX_LENGTH) {
            String msg = "Username cannot be longer than";
            msg += " " + USERNAME_MAX_LENGTH + " characters";
            throw new BadRequestResponse(msg);
        }
    }

    private static void validatePassword(@NotNull Context ctx,
                                         @NotNull List<String> values) {
        String password = values.get(0);

        if (password.isEmpty()) {
            String msg = "Password cannot be empty";
            throw new BadRequestResponse(msg);
        }

        /*
         * Spaces are allowed in passwords, however they are not allowed
         * to start with whitespace or end with whitespace. This is just
         * in case somebody copy-pastes their password and accidentally
         * leaves in trailing whitespace.
         */
        String trimmed = password.trim();
        if (!password.equals(trimmed)) {
            String msg = "Password cannot be surrounded by whitespace";
            throw new BadRequestResponse(msg);
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            String msg = "Password cannot be longer than";
            msg += " " + PASSWORD_MAX_LENGTH + " characters";
            throw new BadRequestResponse(msg);
        }
    }

    private final @NotNull AccountManager manager;

    WebAccountManager(@NotNull AccountManager manager) {
        this.manager = manager;
    }

    @Endpoint(type = HandlerType.POST, path = "/uno/account/create")
    public void create(@NotNull Context ctx) throws SQLException {
        String username = USERNAME_PARAM.require(ctx);
        String password = PASSWORD_PARAM.require(ctx);

        if (manager.usernameExists(username)) {
            ctx.status(HttpCode.CONFLICT);
            return; /* account already exists */
        }

        UnoAccount account = manager.createAccount(username, password);
        JsonElement accountJson = UnoDealer.toJson(account);

        JsonObject response = new JsonObject();
        response.add("account", accountJson);
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.POST, path = "/uno/account/uuid")
    public void uuid(@NotNull Context ctx) throws SQLException {
        String username = USERNAME_PARAM.require(ctx);

        /*
         * Attempt to get the UUID for the account with the specified
         * username. If the account does not exist, return a value of
         * to the client intentionally. This lets them know that the
         * account does not exist without a mysterious empty response.
         */
        UUID uuid = manager.getUUID(username);
        String uuidStr = Objects.toString(uuid, null);

        JsonObject response = new JsonObject();
        response.addProperty("uuid", uuidStr);
        ctx.json(response);
    }

    @Endpoint(type = HandlerType.GET, path = "/uno/account/get")
    public void get(@NotNull Context ctx) throws SQLException {
        UUID uuid = UUID_PARAM.require(ctx);

        UnoAccount account = manager.getAccount(uuid);
        JsonElement accountJson = UnoDealer.toJson(account);

        JsonObject response = new JsonObject();
        response.add("account", accountJson);
        ctx.json(response);
    }

}
