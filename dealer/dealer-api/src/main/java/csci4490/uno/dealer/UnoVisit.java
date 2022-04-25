package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Represents an UNO visit session.
 *
 * @see #getSessionToken()
 */
public interface UnoVisit {

    /**
     * @return the account this session represents.
     */
    @NotNull UnoAccount getAccount();

    /**
     * The address used for beginning the visit session is the only one that
     * can use the session token. This is for the same reason an access token
     * can only be used by the address it was issued to.
     *
     * @return the session address.
     */
    @NotNull InetAddress getAddress();

    /**
     * The session token is used for authenticating requests. This is
     * required by endpoints which expect the user to be logged in.
     * <p>
     * <b>Note:</b> By design, only the IP address that requested the
     * session token may use it.
     *
     * @return the login access token.
     */
    @NotNull UUID getSessionToken();

    /**
     * A keep alive is employed by the UNO dealer server to determine if
     * an UNO account is still online. A keep alive must be sent at least
     * once every thirty seconds to be considered online.
     *
     * @return the last keep alive time, specified in milliseconds.
     */
    long getLastKeepAlive();

}
