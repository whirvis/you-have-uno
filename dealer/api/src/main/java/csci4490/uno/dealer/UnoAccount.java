package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an UNO account.
 *
 * @see #getUUID()
 * @see #getUsername()
 */
public interface UnoAccount {

    /**
     * The UUID of an account is guaranteed to never be {@code null}, and to
     * never change. It will remain the same even if the user updates account
     * details, like their username.
     *
     * @return the account UUID.
     */
    @NotNull UUID getUUID();

    /**
     * The username, while guaranteed to never be {@code null}, is <i>not</i>
     * guaranteed to remain the same. The UUID of an account should be used
     * to identify it instead.
     *
     * @return the account username.
     */
    @NotNull String getUsername();

    /**
     * Updates the account username.
     * <p>
     * <b>Note:</b> For this to function, an extending class must
     * implement this method. Otherwise, this method will throw an
     * {@code UnsupportedOperationException}.
     *
     * @param username the account username.
     * @throws NullPointerException if {@code username} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    default void setUsername(@NotNull String username) throws IOException {
        Objects.requireNonNull(username, "username cannot be null");
        throw new UnsupportedOperationException();
    }

}
