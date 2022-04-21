package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an UNO account.
 *
 * @see #getUUID()
 * @see #getUsername()
 */
public class UnoAccount {

    @SerializedName("uuid")
    protected final @NotNull UUID uuid;

    @SerializedName("username")
    protected @NotNull String username;

    /**
     * @param uuid     the account UUID.
     * @param username the account username.
     * @throws NullPointerException if {@code uuid} or {@code username}
     *                              are {@code null}.
     */
    public UnoAccount(@NotNull UUID uuid, @NotNull String username) {
        this.uuid = Objects.requireNonNull(uuid,
                "uuid cannot be null");
        this.username = Objects.requireNonNull(username,
                "username cannot be null");
    }

    /**
     * The UUID of an account is guaranteed to never be {@code null}, and to
     * never change. It will remain the same even if the user updates account
     * details, like their username.
     *
     * @return the account UUID.
     */
    public final @NotNull UUID getUUID() {
        return this.uuid;
    }

    /**
     * The username, while guaranteed to never be {@code null}, is <i>not</i>
     * guaranteed to remain the same. The UUID of an account should be used
     * to identify it instead.
     *
     * @return the account username.
     */
    public final @NotNull String getUsername() {
        return this.username;
    }

    /**
     * Updates the account username.
     * <p>
     * <b>Note:</b> For this to function, an extending class must
     * implement this method. Otherwise, this method will throw an
     * {@code UnsupportedOperationException}.
     *
     * @param username the account username.
     * @throws NullPointerException if {@code username} is {@code null}.
     */
    public void setUsername(@NotNull String username) {
        Objects.requireNonNull(username, "username cannot be null");
        throw new UnsupportedOperationException();
    }

}
