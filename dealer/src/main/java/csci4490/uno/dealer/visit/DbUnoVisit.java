package csci4490.uno.dealer.visit;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.UUID;

public class DbUnoVisit implements UnoVisit {

    @SerializedName("address")
    private final InetAddress address;

    @SerializedName("session_token")
    private final UUID sessionToken;

    @SerializedName("last_keep_alive")
    private final long lastKeepAlive;

    public DbUnoVisit(@NotNull InetAddress address, @NotNull UUID sessionToken,
                      long lastKeepAlive) {
        this.address = address;
        this.sessionToken = sessionToken;
        this.lastKeepAlive = lastKeepAlive;
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
