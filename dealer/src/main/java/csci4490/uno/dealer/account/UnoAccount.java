package csci4490.uno.dealer.account;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UnoAccount {

    @NotNull UUID getUUID();
    @NotNull String getUsername();

}
