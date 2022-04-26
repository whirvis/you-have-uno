package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Container for endpoints of the UNO dealer server. This exists to ensure
 * the strings used for endpoint paths are the same. This allows them to be
 * changed easily in an IDE.
 */
public class UnoEndpoints {

    /**
     * The default port for the Uno Dealer Webserver.
     */
    public static final int SERVER_PORT = 48902;

    /* @formatter:off */
    /**
     * Addresses for an Uno Dealer Webserver.
     */
    public static final @NotNull InetSocketAddress
            LOCALHOST = new InetSocketAddress("localhost", SERVER_PORT),
            OFFICIAL = new InetSocketAddress("uno.whirvis.net", SERVER_PORT);

    /* @formatter:off */
    /**
     * Endpoints for UNO accounts.
     */
    public static final @NotNull String
            UNO_ACCOUNT_CREATE = "/uno/account/create",
            UNO_ACCOUNT_UUID = "/uno/account/uuid",
            UNO_ACCOUNT_INFO = "/uno/account/info";

    /**
     * Endpoints for UNO logins.
     */
    public static final @NotNull String
            UNO_LOGIN = "/uno/login",
            UNO_LOGIN_VERIFY = "/uno/login/verify";

    /**
     * Endpoints for UNO visits.
     */
    public static final @NotNull String
            UNO_VISIT_BEGIN = "/uno/visit/begin",
            UNO_VISIT_END = "/uno/visit/end",
            UNO_VISIT_PING = "/uno/visit/ping";

    /**
     * Endpoints for UNO taverns.
     */
    public static final @NotNull String
            UNO_TAVERN_REGISTER = "/uno/tavern/register",
            UNO_TAVERN_UNREGISTER = "/uno/tavern/unregister",
            UNO_TAVERN_PING = "/uno/tavern/ping",
            UNO_TAVERN_LIST = "/uno/tavern/list";

    /**
     * Endpoints for UNO games.
     */
    public static final @NotNull String
            UNO_GAME_CREATE = "/uno/game/create",
            UNO_GAME_INFO = "/uno/game/info";
    /* @formatter:on */

}
