package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@SuppressWarnings("NotNullFieldNotInitialized")
public class PeerJoinedPacket extends TavernPacket {

    public @NotNull InetSocketAddress address;

    public PeerJoinedPacket() {
        super(ID_PEER_JOINED);
    }

    @Override
    protected void encode() {
        this.writeAddress(address);
    }

    @Override
    protected void decode() {
        this.address = this.readAddress();
    }
}
