package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@SuppressWarnings("NotNullFieldNotInitialized")
public class NoSuchPeerPacket extends TavernPacket {

    public @NotNull InetSocketAddress ghost;

    public NoSuchPeerPacket() {
        super(ID_NO_SUCH_PEER);
    }

    @Override
    protected void encode() {
        this.writeAddress(ghost);
    }

    @Override
    protected void decode() {
        this.ghost = this.readAddress();
    }

}
