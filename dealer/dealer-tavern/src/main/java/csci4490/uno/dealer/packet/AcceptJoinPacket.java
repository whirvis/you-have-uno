package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class AcceptJoinPacket extends TavernPacket {

    public final @NotNull List<@NotNull InetSocketAddress> peers;

    public AcceptJoinPacket() {
        super(ID_ACCEPT_JOIN);
        this.peers = new ArrayList<>();
    }

    @Override
    public void encode() {
        buffer.writeShort(peers.size());
        for (InetSocketAddress peer : peers) {
            this.writeAddress(peer);
        }
    }

    @Override
    public void decode() {
        peers.clear();
        int peerCount = buffer.readUnsignedShort();
        for (int i = 0; i < peerCount; i++) {
            peers.add(this.readAddress());
        }
    }

}
