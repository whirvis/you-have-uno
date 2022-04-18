package csci4490.uno.web;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class UnoAccount {

    @SerializedName("uuid")
    public final UUID uuid;

    @SerializedName("username")
    public final String username;

    public UnoAccount(@NotNull UUID uuid, @NotNull String username) {
        this.uuid = Objects.requireNonNull(uuid,
                "uuid cannot be null");
        this.username = Objects.requireNonNull(username,
                "username cannot be null");
    }

}
