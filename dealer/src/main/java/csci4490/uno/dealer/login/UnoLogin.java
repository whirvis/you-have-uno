package csci4490.uno.dealer.login;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.Date;
import java.util.UUID;

public interface UnoLogin {

    @NotNull InetAddress getAddress();
    @NotNull UUID getUUID();
    @NotNull UUID getAccessToken();
    @NotNull Date getExpiration();

}
