package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a login for an {@link UnoAccount}.
 *
 * @see #getAccount()
 * @see #getAccessToken()
 */
public interface UnoLogin {

    /**
     * @return the account this logs in to.
     */
    @NotNull UnoAccount getAccount();

    /**
     * The address used for login is the only one that can use the access
     * token for login. This restriction exists for security reasons. If a
     * malicious actor gains access to the user's access token, it cannot
     * be used on another machine.
     *
     * @return the login address.
     */
    @NotNull InetAddress getAddress();

    /**
     * The access token is used for authenticating requests. Although a
     * select few endpoints accept either a password or an access token,
     * most require the use an access token. This is to prevent as much
     * as possible the password from being sent over the network.
     * <p>
     * <b>Note:</b> It is safe (and encouraged!) to store the access token
     * to the user's file system for automatic login. By design, only the
     * IP address that requested it may use it.
     *
     * @return the login access token.
     */
    @NotNull UUID getAccessToken();

    /**
     * After this date, the access token can no longer be used for login.
     * The user will have to enter their username and password again.
     *
     * @return the expiration date for this login.
     */
    @NotNull Date getExpiration();

}
