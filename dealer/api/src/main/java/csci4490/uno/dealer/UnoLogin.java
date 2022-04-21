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
public class UnoLogin {

    @SerializedName("account")
    protected final @NotNull UnoAccount account;

    @SerializedName("address")
    protected final @NotNull InetAddress address;

    @SerializedName("access_token")
    protected final @NotNull UUID accessToken;

    @SerializedName("expires_at")
    protected final @NotNull Date expiration;

    /**
     * @param account     the account this logs in to.
     * @param address     the login address.
     * @param accessToken the access token used for login.
     * @param expiration  the date this login expires.
     * @throws NullPointerException if {@code account}, {@code address},
     *                              {@code accessToken}, or {@code expiration}
     *                              are {@code null}.
     */
    public UnoLogin(@NotNull UnoAccount account, @NotNull InetAddress address,
                    @NotNull UUID accessToken, @NotNull Date expiration) {
        this.account = Objects.requireNonNull(account,
                "account cannot be null");
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.accessToken = Objects.requireNonNull(accessToken,
                "accessToken cannot be null");
        this.expiration = Objects.requireNonNull(expiration,
                "expiration cannot be null");
    }

    /**
     * @return the account this logs in to.
     */
    public @NotNull UnoAccount getAccount() {
        return this.account;
    }

    /**
     * The address used for login is the only one that can use the access
     * token for login. This restriction exists for security reasons. If a
     * malicious actor gains access to the user's access token, it cannot
     * be used on another machine.
     *
     * @return the login address.
     */
    public @NotNull InetAddress getAddress() {
        return this.address;
    }

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
    public @NotNull UUID getAccessToken() {
        return this.accessToken;
    }

    /**
     * After this date, the access token can no longer be used for login.
     * The user will have to enter their username and password again.
     *
     * @return the expiration date for this login.
     */
    public @NotNull Date getExpiration() {
        return this.expiration;
    }

}
