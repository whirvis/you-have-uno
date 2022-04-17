package csci4490.uno.dealer.login;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.Date;
import java.util.UUID;

public class DbUnoLogin implements UnoLogin {

    @SerializedName("address")
    private final InetAddress address;

    @SerializedName("uuid")
    private final UUID uuid;

    @SerializedName("access_token")
    private final UUID accessToken;

    @SerializedName("expires_at")
    private final Date expiration;

    public DbUnoLogin(@NotNull InetAddress address, @NotNull UUID uuid,
                      @NotNull UUID accessToken, @NotNull Date expiration) {
        this.address = address;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return this.address;
    }

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
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
