package csci4490.uno.commons.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class TcpClient {

    private boolean connecting;
    private NioEventLoopGroup group;
    private Bootstrap client;
    private TcpSession server;

    public TcpClient() {
        /* nothing to construct */
    }

    /**
     * <b>Note:</b> This method only returns if a client is connecting to
     * a server. It does <i>not</i> return if a client is connected to a
     * server. This can return {@code false} after the client has connected
     * to a server.
     *
     * @return {@code true} if the client is currently connecting to a server,
     * {@code false} otherwise.
     * @see #isConnected()
     */
    public boolean isConnecting() {
        return this.connecting;
    }

    /**
     * @return {@code true} if the client is currently connected to a server,
     * {@code false} otherwise.
     * @see #isConnecting()
     */
    public boolean isConnected() {
        return this.server != null;
    }

    /* package-private for TcpClientChannelHandler */
    @Nullable TcpSession connectServer(@NotNull Channel channel) {
        if (this.isConnected()) {
            String msg = "channel already connected";
            throw new IllegalStateException(msg);
        }

        /*
         * Now that the server has responded to the client and finished
         * connection, the client is no longer in the process of connecting
         * to the server. It is now connected to the server.
         */
        this.connecting = false;

        this.server = new TcpSession(channel);

        try {
            this.connected(server);
            return server;
        } catch (Throwable cause) {
            this.failedConnection(server, cause);
            return null;
        }
    }

    /**
     * Connects this client to a server at the specified address.
     *
     * @param address the address of the server.
     * @throws NullPointerException  if {@code address} is {@code null}.
     * @throws IllegalStateException if this client is in the process of
     *                               connecting to a server or is already
     *                               connected to a server.
     * @see #disconnect()
     */
    public void connect(@NotNull InetSocketAddress address) {
        Objects.requireNonNull(address, "address cannot be null");
        if (this.isConnecting()) {
            throw new IllegalStateException("already connecting to server");
        } else if (this.isConnected()) {
            throw new IllegalStateException("already connected to server");
        }

        this.connecting = true;

        this.group = new NioEventLoopGroup();
        this.client = new Bootstrap();

        /*
         * This performs initial Netty setup. It tells Netty which event loop
         * group to use, what channel type to use, and what handler it should
         * use for connections. By setting the channel to NioSocketChannel,
         * we're telling Netty to create a TCP client socket.
         */
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.handler(new TcpClientChannelHandler(this));

        /*
         * Unlike the TCP server (which makes a call to sync socket binding),
         * the client does not sync the connection. This is to allow other
         * code to execute while the client is attempting to connect to the
         * server. The client will know when it has successfully connected
         * via the connected() hook method.
         */
        client.connect(address);
    }

    /**
     * Called when the client has connected to a server. This may throw
     * any exception without needing to catch it. However, if an exception
     * is thrown, the client will need to re-attempt connection.
     *
     * @param server the server the client has connected to.
     * @throws Exception if an error occurs.
     * @see #failedConnection(TcpSession, Throwable)
     */
    /* @formatter:off */
    protected abstract void
            connected(@NotNull TcpSession server) throws Exception;
    /* @formatter:on */

    /**
     * Called when client connection has failed. This usually occurs when
     * an exception is thrown by {@link #connected(TcpSession)}.
     * <p>
     * <b>By default, this method does nothing.</b>
     *
     * @param server the server which the client failed to connect to.
     * @param cause  the cause of failure.
     */
    protected void failedConnection(@NotNull TcpSession server,
                                    @NotNull Throwable cause) {
        /* optional implement */
    }

    /* package-private for TcpClientChannelHandler */
    void disconnectServer() {
        this.disconnect("Server disconnected", null);
    }

    /**
     * Disconnects from the server.
     *
     * @param reason the reason for disconnecting.
     * @param error  the exception which triggered this disconnection.
     *               A value of {@code null} is permitted, and indicates
     *               an exception was not the cause for disconnection.
     * @throws IllegalStateException if this client is not currently
     *                               connected to a server.
     */
    public void disconnect(@Nullable String reason,
                           @Nullable Throwable error) {
        if (!this.isConnected()) {
            throw new IllegalStateException("not connected to server");
        }

        group.shutdownGracefully();

        this.group = null;
        this.client = null;

        try {
            server.close();
            this.disconnected(server);
        } catch (Throwable cause) {
            this.failedDisconnection(server, cause);
        }

        this.server = null;
    }


    /**
     * Disconnects from the server.
     *
     * @param reason the reason for disconnecting.
     * @throws IllegalStateException if this client is not currently
     *                               connected to a server.
     */
    public void disconnect(@Nullable String reason) {
        this.disconnect(reason, null);
    }


    /**
     * Disconnects from the server with the reason being the message
     * returned by the {@code error} parameter. Specifically, the
     * result of {@link Throwable#getMessage()}.
     *
     * @param error the exception which triggered this disconnection.
     *              A value of {@code null} is permitted, and indicates
     *              an exception was not the cause for disconnection.
     * @throws IllegalStateException if this client is not currently
     *                               connected to a server.
     */
    public void disconnect(@Nullable Throwable error) {
        if (error != null) {
            this.disconnect(error.getMessage(), error);
        } else {
            this.disconnect(null, null);
        }
    }

    /**
     * Disconnects from the server with no reason specified.
     *
     * @throws IllegalStateException if this client is not currently
     *                               connected to a server.
     */
    public void disconnect() {
        this.disconnect(null, null);
    }

    /**
     * Called when the client has disconnected from the server. This may throw
     * any exception without needing to catch it. If any exception is thrown,
     * the client will still be disconnected from the server.
     *
     * @param server the server the client disconnected from.
     * @throws Exception if an error occurs.
     * @see #failedDisconnection(TcpSession, Throwable)
     */
    /* @formatter:off */
    protected abstract void
            disconnected(@NotNull TcpSession server) throws Exception;
    /* @formatter:on */

    /**
     * Called when server disconnection has failed. This usually occurs when
     * an exception is thrown by {@link #disconnected(TcpSession)}.
     * <p>
     * <b>By default, this method does nothing.</b>
     *
     * @param server the server the client failed to disconnect from.
     * @param cause  the cause of failure.
     */
    protected void failedDisconnection(@NotNull TcpSession server,
                                       @NotNull Throwable cause) {
        /* optional implement */
    }

    /**
     * Called when a packet is received from the server.
     *
     * @param server the server which sent {@code buffer}.
     * @param buffer the packet sent by {@code server}.
     */
    protected abstract void handlePacket(@NotNull TcpSession server,
                                         @NotNull ByteBuf buffer);

    /**
     * Sets the callback for when a packet is received from the server.
     *
     * @param callback the code to execute when a packet is received from
     *                 {@code client}. A value of {@code null} is permitted,
     *                 and will result in nothing being executed.
     * @throws IllegalStateException if this client is not connected to a
     *                               server.
     */
    /* @formatter:off */
    public final void handlePackets(@Nullable BiConsumer<TcpSession,
            ByteBuf> callback) {
        if (!this.isConnected()) {
            throw new IllegalArgumentException("not connected to server");
        }

        ChannelPipeline pipeline = server.channel.pipeline();
        TcpClientChannelHandler handler =
                pipeline.get(TcpClientChannelHandler.class);
        handler.packetCallback = callback;
    }
    /* @formatter:on */

}
