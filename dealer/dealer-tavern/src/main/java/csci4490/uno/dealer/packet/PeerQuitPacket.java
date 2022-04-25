package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

@SuppressWarnings("NotNullFieldNotInitialized")
public class PeerQuitPacket extends TavernPacket {

    public @NotNull InetSocketAddress address;
    public @Nullable String reason;

    public PeerQuitPacket() {
        super(ID_PEER_QUIT);
    }

    @Override
    protected void encode() {
        this.writeAddress(address);

        buffer.writeBoolean(reason != null);
        if (reason != null) {
            this.writeString(reason);
        }
    }

    @Override
    protected void decode() {
        this.address = this.readAddress();

        boolean hasReason = buffer.readBoolean();
        if (hasReason) {
            this.reason = this.readString();
        }
    }
}
