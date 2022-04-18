package csci4490.uno.web;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class UnoVisit {

    @SerializedName("address")
    public final InetAddress address;

    @SerializedName("session_token")
    public final UUID sessionToken;

    @SerializedName("last_keep_alive")
    public long lastKeepAlive;

    public UnoVisit(@NotNull InetAddress address, @NotNull UUID sessionToken,
                    long lastKeepAlive) {
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.sessionToken = Objects.requireNonNull(sessionToken,
                "sessionToken cannot be null");
        this.lastKeepAlive = lastKeepAlive;
    }

}
