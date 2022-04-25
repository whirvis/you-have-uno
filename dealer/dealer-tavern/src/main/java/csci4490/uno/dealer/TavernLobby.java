package csci4490.uno.dealer;

import csci4490.uno.commons.network.TcpSession;
import csci4490.uno.dealer.packet.AcceptJoinPacket;
import csci4490.uno.dealer.packet.NoSuchPeerPacket;
import csci4490.uno.dealer.packet.PeerJoinedPacket;
import csci4490.uno.dealer.packet.PeerMessagePacket;
import csci4490.uno.dealer.packet.PeerQuitPacket;
import csci4490.uno.dealer.packet.TavernPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TavernLobby {

    private final UUID uuid;
    private final Map<InetSocketAddress, TcpSession> peers;

    private boolean opened;
    private TcpSession host;

    public TavernLobby() {
        this.uuid = UUID.randomUUID();
        this.peers = new HashMap<>();
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    private void requireOpen() {
        if (!opened) {
            throw new IllegalStateException("lobby not open");
        }
    }

    /**
     * Opens the lobby to the world. The specified host must remain connected
     * for the entire lifetime of this lobby. This lobby will cease to exist
     * the moment the host disconnects.
     *
     * @param host the host of this lobby. This will be added as the first
     *             client to the lobby.
     * @throws NullPointerException  if {@code host} is {@code null}.
     * @throws IllegalStateException if this lobby has already been opened.
     */
    public void open(@NotNull TcpSession host) {
        Objects.requireNonNull(host, "host cannot be null");
        if (opened) {
            throw new IllegalStateException("lobby already opened");
        }
        this.host = host;
        this.addClient(host);
        this.opened = true;
    }

    public boolean hasClient(@NotNull InetSocketAddress address) {
        Objects.requireNonNull(address, "address cannot be null");
        return peers.containsKey(address);
    }

    public boolean hasClient(@NotNull TcpSession client) {
        Objects.requireNonNull(client, "client cannot be null");
        return peers.containsValue(client);
    }

    public @Nullable TcpSession getClient(@NotNull InetSocketAddress address) {
        Objects.requireNonNull(address, "address cannot be null");
        return peers.get(address);
    }

    /**
     * Adds a client to the lobby. Once a client is in the lobby, other
     * clients in this lobby can send messages to them directly by sending
     * a {@link PeerMessagePacket} to the server.
     *
     * @param client the client to add to this lobby.
     * @throws NullPointerException  if {@code client} is {@code null}.
     * @throws IllegalStateException if {@code client} is already in this
     *                               lobby; if this lobby has yet to be
     *                               opened via {@link #open(TcpSession)}.
     */
    public void addClient(@NotNull TcpSession client) {
        Objects.requireNonNull(client, "client cannot be null");
        if (this.hasClient(client)) {
            throw new IllegalStateException("client already in lobby");
        }
        this.requireOpen();

        peers.put(client.getAddress(), client);

        AcceptJoinPacket acceptJoin = new AcceptJoinPacket();
        acceptJoin.peers.addAll(peers.keySet());
        acceptJoin.encodeAndSend(client);

        PeerJoinedPacket peerJoined = new PeerJoinedPacket();
        peerJoined.address = client.getAddress();
        this.broadcastPacket(peerJoined);
    }

    /**
     * Removes a client from the lobby.
     *
     * @param client the client to remove rom this lobby.
     * @throws NullPointerException  if {@code client} is {@code null}.
     * @throws IllegalStateException if {@code client} is not currently in
     *                               this lobby; if this lobby has yet to be
     *                               opened via {@link #open(TcpSession)}.
     */
    public void removeClient(@NotNull TcpSession client) {
        Objects.requireNonNull(client, "client cannot be null");
        if (!this.hasClient(client)) {
            throw new IllegalStateException("client not in lobby");
        }
        this.requireOpen();

        peers.remove(client.getAddress());

        PeerQuitPacket peerQuit = new PeerQuitPacket();
        peerQuit.address = client.getAddress();
        this.broadcastPacket(peerQuit);
    }

    private void handlePeerMessage(@NotNull TcpSession sender,
                                   @NotNull ByteBuf buffer) {
        PeerMessagePacket peerMessage = new PeerMessagePacket();
        peerMessage.decode(buffer);

        InetSocketAddress destination = peerMessage.destination;
        TcpSession recipient = this.getClient(destination);

        if (recipient != null) {
            recipient.sendPacket(peerMessage.content);
        } else {
            NoSuchPeerPacket noSuchPeer = new NoSuchPeerPacket();
            noSuchPeer.ghost = peerMessage.destination;
            noSuchPeer.encodeAndSend(sender);
        }
    }

    /**
     * Handles a packet sent from a client intended for this lobby. If the
     * packet type is unknown, it will simply be ignored.
     *
     * @param sender the sender of the packet.
     * @param buffer the packet sent by the client.
     * @throws NullPointerException  if {@code sender} or {@code buffer}
     *                               are {@code null}.
     * @throws IllegalStateException if this lobby has yet to be opened
     *                               via {@link #open(TcpSession)}.
     */
    public void handlePacket(@NotNull TcpSession sender,
                             @NotNull ByteBuf buffer) {
        Objects.requireNonNull(sender, "sender cannot be null");
        Objects.requireNonNull(buffer, "buffer cannot be null");
        this.requireOpen();

        int packetId = buffer.getShort(0);
        if (packetId == TavernPacket.ID_PEER_MESSAGE) {
            this.handlePeerMessage(sender, buffer);
        }
    }

    private void broadcastPacket(@NotNull TavernPacket packet) {
        ByteBuf buffer = Unpooled.buffer();
        packet.encode(buffer);
        for (TcpSession peer : peers.values()) {
            peer.sendPacket(buffer);
        }
        buffer.release();
    }

}
