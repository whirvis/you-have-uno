package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

/**
 * A static container for an {@link UnoVisit}. This class supports
 * serialization via GSON.
 */
public class StaticUnoVisit implements UnoVisit {

    @SerializedName("account")
    private final @NotNull UnoAccount account;

    @SerializedName("address")
    private final @NotNull InetAddress address;

    @SerializedName("session_token")
    private final @NotNull UUID sessionToken;

    @SerializedName("last_keep_alive")
    private final long lastKeepAlive;

    /**
     * @param account       the account this represents.
     * @param address       the visit address.
     * @param sessionToken  the session token.
     * @param lastKeepAlive the last keep alive time, specified in
     *                      milliseconds.
     * @throws NullPointerException if {@code account}, {@code address}
     *                              or {@code sessionToken} are {@code null}.
     */
    public StaticUnoVisit(@NotNull UnoAccount account,
                          @NotNull InetAddress address,
                          @NotNull UUID sessionToken, long lastKeepAlive) {
        this.account = Objects.requireNonNull(account,
                "account cannot be null");
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.sessionToken = Objects.requireNonNull(sessionToken,
                "sessionToken cannot be null");
        this.lastKeepAlive = lastKeepAlive;
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
    public @NotNull UUID getSessionToken() {
        return this.sessionToken;
    }

    @Override
    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }
}
