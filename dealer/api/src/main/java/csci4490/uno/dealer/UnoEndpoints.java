package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;

/**
 * Container for endpoints of the UNO dealer server. This exists to ensure
 * the strings used for endpoint paths are the same. This allows them to be
 * changed easily in an IDE.
 */
public class UnoEndpoints {

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
    /* @formatter:on */

}
