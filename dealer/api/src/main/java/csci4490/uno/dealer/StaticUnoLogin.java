package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A static container for an {@link UnoLogin}. This class supports
 * serialization via GSON.
 */
public class StaticUnoLogin implements UnoLogin {

    @SerializedName("account")
    private final @NotNull UnoAccount account;

    @SerializedName("address")
    private final @NotNull InetAddress address;

    @SerializedName("access_token")
    private final @NotNull UUID accessToken;

    @SerializedName("expires_at")
    private final @NotNull Date expiration;

    /**
     * @param account     the account this logs in to.
     * @param address     the login address.
     * @param accessToken the access token used for login.
     * @param expiration  the date this login expires.
     * @throws NullPointerException if {@code account}, {@code address},
     *                              {@code accessToken}, or {@code expiration}
     *                              are {@code null}.
     */
    public StaticUnoLogin(@NotNull StaticUnoAccount account,
                          @NotNull InetAddress address,
                          @NotNull UUID accessToken,
                          @NotNull Date expiration) {
        this.account = Objects.requireNonNull(account,
                "account cannot be null");
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.accessToken = Objects.requireNonNull(accessToken,
                "accessToken cannot be null");
        this.expiration = Objects.requireNonNull(expiration,
                "expiration cannot be null");
    }

    @Override
    public @NotNull UnoAccount getAccount() {
        return this.account;
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return this.address;
    }

    @Override
    public @NotNull UUID getAccessToken() {
        return this.accessToken;
    }

    @Override
    public @NotNull Date getExpiration() {
        return this.expiration;
    }
}
