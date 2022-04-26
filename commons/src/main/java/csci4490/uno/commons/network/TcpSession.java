package csci4490.uno.commons.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * Represents a connection made over TCP using Netty.
 */
public final class TcpSession implements Closeable {

    /*
     * This is package-private, so it is accessible to both the TcpServer
     * and the TcpClient. No getter method is provided as it is redundant
     * here. The field is final, so it can't be changed.
     */
    final Channel channel;

    private final InetSocketAddress address;

    private boolean disconnected;
    private boolean closed;

    /**
     * @param channel the channel.
     * @throws NullPointerException if {@code channel} is {@code null}.
     */
    public TcpSession(@NotNull Channel channel) {
        this.channel = Objects.requireNonNull(channel,
                "channel cannot be null");

        /*
         * Since the socket address is protocol dependent, it must be
         * manually cast to an InetSocketAddress here. Since we know
         * that every connection is made over TCP, it is safe to make
         * a cast to an InetSocketAddress.
         */
        SocketAddress sockAddr = channel.remoteAddress();
        this.address = (InetSocketAddress) sockAddr;
    }

    /**
     * @return the remote socket address.
     * @see #getInetAddress()
     * @see #getPort()
     */
    public @NotNull InetSocketAddress getAddress() {
        return this.address;
    }

    /**
     * @return the address of the connection.
     * @see #getAddress()
     * @see #getPort()
     */
    public @NotNull InetAddress getInetAddress() {
        return address.getAddress();
    }

    /**
     * @return the port of the connection.
     * @see #getAddress()
     * @see #getInetAddress()
     */
    public int getPort() {
        return address.getPort();
    }

    /**
     * Sends a packet to the remote.
     *
     * @param buffer the packet to send.
     * @throws NullPointerException if {@code buffer} is {@code null}.
     */
    public void sendPacket(@NotNull ByteBuf buffer) {
        Objects.requireNonNull(buffer, "buffer cannot be null");
        channel.write(buffer);
        channel.flush(); /* force packet to send */
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    /**
     * Disconnects the TCP session.
     *
     * @throws IllegalStateException if this session has already been
     *                               disconnected.
     */
    public void disconnect() {
        if (disconnected) {
            throw new IllegalStateException("already disconnected");
        }
        channel.close().syncUninterruptibly();
        this.disconnected = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Note:</b> If the session is still connected, this will disconnect
     * it. An {@code IllegalStateException} will <i>not</i> be thrown, as a
     * check is made before calling {@link #disconnect()}.
     */
    @Override
    public void close() {
        if (this.isClosed()) {
            return;
        }

        if (!this.isDisconnected()) {
            this.disconnect();
        }

        this.closed = true;
    }

}
