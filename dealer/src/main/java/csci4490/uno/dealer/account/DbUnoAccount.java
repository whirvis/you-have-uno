package csci4490.uno.dealer.account;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DbUnoAccount implements UnoAccount {

    private final UUID uuid;
    private final String username;

    DbUnoAccount(@NotNull UUID uuid, @NotNull String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull String getUsername() {
        return this.username;
    }

}
