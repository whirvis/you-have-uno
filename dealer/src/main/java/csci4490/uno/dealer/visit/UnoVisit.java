package csci4490.uno.dealer.visit;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.UUID;

public interface UnoVisit {

    @NotNull InetAddress getAddress();
    @NotNull UUID getSessionToken();
    long getLastKeepAlive();

}
