package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public class StaticUnoGame implements UnoGame {

    @SerializedName("code")
    private final String code;

    @SerializedName("host")
    private final UUID host;

    @SerializedName("lobby_id")
    private final UUID lobbyId;

    @SerializedName("tavern_address")
    private final InetSocketAddress tavernAddress;

    @SerializedName("public_key")
    private final PublicKey publicKey;

    @SerializedName("private_key")
    private final PrivateKey privateKey;

    public StaticUnoGame(@NotNull String code, @NotNull UUID host,
                         @NotNull UUID lobbyId,
                         @NotNull InetSocketAddress tavernAddress,
                         @NotNull PublicKey publicKey,
                         @Nullable PrivateKey privateKey) {
        this.code = code;
        this.host = host;
        this.lobbyId = lobbyId;
        this.tavernAddress = tavernAddress;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public @NotNull String getCode() {
        return this.code;
    }

    @Override
    public @NotNull UUID getHost() {
        return this.host;
    }

    @Override
    public @NotNull UUID getLobbyId() {
        return this.lobbyId;
    }

    @Override
    public @NotNull InetSocketAddress getTavernAddress() {
        return this.tavernAddress;
    }

    @Override
    public @NotNull PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public @Nullable PrivateKey getPrivateKey() {
        return this.privateKey;
    }

}
