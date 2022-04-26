package csci4490.uno.dealer;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * A static container for an {@link UnoTavern}. This class supports
 * serialization via GSON.
 */
public class StaticUnoTavern implements UnoTavern {

    @SerializedName("address")
    private final @NotNull InetSocketAddress address;

    @SerializedName("last_keep_alive")
    private final long lastKeepAlive;

    /**
     * @param address       the tavern address.
     * @param lastKeepAlive the last keep alive time, specified in
     *                      milliseconds.
     * @throws NullPointerException if {@code address} is {@code null}.
     */
    public StaticUnoTavern(@NotNull InetSocketAddress address,
                           long lastKeepAlive) {
        this.address = Objects.requireNonNull(address,
                "address cannot be null");
        this.lastKeepAlive = lastKeepAlive;
    }

    @Override
    public @NotNull InetSocketAddress getAddress() {
        return this.address;
    }

    @Override
    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }

}
