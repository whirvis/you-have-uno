package csci4490.uno.web;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

public class UnoLogin {

    @SerializedName("address")
    public final InetAddress address;

    @SerializedName("uuid")
    public final UUID uuid;

    @SerializedName("access_token")
    public final UUID accessToken;

    @SerializedName("expires_at")
    public final Date expiration;

    public UnoLogin(@NotNull InetAddress address, @NotNull UUID uuid,
                    @NotNull UUID accessToken, @NotNull Date expiration) {
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.uuid = Objects.requireNonNull(uuid,
                "uuid cannot be null");
        this.accessToken = Objects.requireNonNull(accessToken,
                "accessToken cannot be null");
        this.expiration = Objects.requireNonNull(expiration,
                "expiration cannot be null");
    }

}
