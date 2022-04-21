package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * A static container for an {@link UnoAccount}. This class supports
 * serialization via GSON.
 */
public class StaticUnoAccount implements UnoAccount {

    @SerializedName("uuid")
    private final @NotNull UUID uuid;

    @SerializedName("username")
    private final @NotNull String username;

    /**
     * @param uuid     the account UUID.
     * @param username the account username.
     * @throws NullPointerException if {@code uuid} or {@code username}
     *                              are {@code null}.
     */
    public StaticUnoAccount(@NotNull UUID uuid, @NotNull String username) {
        this.uuid = Objects.requireNonNull(uuid,
                "uuid cannot be null");
        this.username = Objects.requireNonNull(username,
                "username cannot be null");
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
