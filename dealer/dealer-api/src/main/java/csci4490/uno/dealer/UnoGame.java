package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public interface UnoGame {

    @NotNull String getCode();

    @NotNull UUID getHost();

    @NotNull UUID getLobbyId();

    @NotNull InetSocketAddress getTavernAddress();

    @NotNull PublicKey getPublicKey();

    @Nullable PrivateKey getPrivateKey();

}
