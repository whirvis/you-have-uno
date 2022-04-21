package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class UnoVisit {

    @SerializedName("address")
    protected final @NotNull InetAddress address;

    @SerializedName("session_token")
    protected final @NotNull UUID sessionToken;

    @SerializedName("last_keep_alive")
    protected long lastKeepAlive;

    public UnoVisit(@NotNull InetAddress address, @NotNull UUID sessionToken,
                    long lastKeepAlive) {
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.sessionToken = Objects.requireNonNull(sessionToken,
                "sessionToken cannot be null");
        this.lastKeepAlive = lastKeepAlive;
    }

    public @NotNull InetAddress getAddress() {
        return this.address;
    }

    public @NotNull UUID getSessionToken() {
        return this.sessionToken;
    }

    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }

}
