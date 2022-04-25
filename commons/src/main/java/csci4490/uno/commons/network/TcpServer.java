package csci4490.uno.commons.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class TcpServer {

    private final int port;
    private final Map<Channel, TcpSession> connected;
    private final List<InetAddress> blocked;

    private NioEventLoopGroup group;
    private ServerBootstrap server;
    private boolean startingUp;
    private boolean running;
    private boolean shuttingDown;

    /**
     * @param port the server port.
     * @throws IllegalArgumentException if {@code port} is not within range
     *                                  of {@code 0} to {@code 65535}.
     */
    public TcpServer(int port) {
        this.port = NetUtils.requirePort(port);
        this.connected = new HashMap<>();
        this.blocked = new ArrayList<>();
    }

    /**
     * @param client the client to check.
     * @return {@code true} if {@code client} is connected, {@code false}
     * otherwise.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public final boolean isConnected(@NotNull TcpSession client) {
        Objects.requireNonNull(client, "client cannot be null");
        return connected.containsValue(client);
    }

    /* package-private for TcpServerChannelHandler */
    void connectClient(@NotNull Channel channel) {
        if (connected.containsKey(channel)) {
            String msg = "channel already connected";
            throw new IllegalStateException(msg);
        }

        TcpSession connection = new TcpSession(channel);

        /*
         * If the address of the connection has been blocked, close the
         * channel immediately and return from this method. The server
         * should not be notified that the client was connected.
         */
        if (blocked.contains(connection.getAddress())) {
            connection.close();
            return;
        }

        /*
         * Before initializing the connection, we must first add a server
         * channel handler. This handler will notify the connection when
         * any packets have come in from the client. It will also notify
         * the server when the client has disconnected.
         */
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new TcpServerChannelHandler(this, connection));

        /*
         * It's possible that the class implementing TcpServer executes
         * code that results in an exception being thrown while setting
         * up the client after connection.
         */
        try {
            connected.put(channel, connection);
            this.clientConnected(connection);
        } catch (Throwable cause) {
            connected.remove(channel);
            this.failedClientConnection(connection, cause);
        }
    }

    /**
     * Called when a client has connected to the server. This may throw
     * any exception without needing to catch it. However, if an exception
     * is thrown, the client will need to re-connect.
     *
     * @param client the client that connected.
     * @throws Exception if an error occurs.
     * @see #failedClientConnection(TcpSession, Throwable)
     */
    /* @formatter:off */
    protected abstract void
            clientConnected(@NotNull TcpSession client) throws Exception;
    /* @formatter:on */

    /**
     * Called when client connection has failed. This usually occurs when
     * an exception is thrown by {@link #clientConnected(TcpSession)}.
     * <p>
     * <b>By default, this method does nothing.</b>
     *
     * @param client the client which failed to connect.
     * @param cause  the cause of failure.
     */
    protected void failedClientConnection(@NotNull TcpSession client,
                                          @NotNull Throwable cause) {
        /* optional implement */
    }

    /**
     * Disconnects a client from the server. This automatically closes the
     * client to ensure resources are freed.
     *
     * @param client           the client to disconnect.
     * @param requireConnected {@code true} if {@code client} must be
     *                         connected, {@code false} otherwise.
     * @throws IllegalStateException if {@code requireConnected} is
     *                               {@code true} and {@code client}
     *                               is not currently connected.
     */
    private void disconnectClient(@NotNull TcpSession client,
                                  boolean requireConnected) {
        if (!this.isConnected(client) && requireConnected) {
            String msg = "client not connected";
            throw new IllegalStateException(msg);
        }

        /* ensure resources are freed */
        client.close();

        connected.remove(client.channel);
        try {
            this.clientDisconnected(client);
        } catch (Throwable cause) {
            this.failedClientDisconnection(client, cause);
        }
    }

    /* package-private for TcpServerChannelHandler */
    void disconnectClient(@NotNull TcpSession connection) {
        this.disconnectClient(connection, true);
    }

    /**
     * Called when a client has disconnected from the server. This may throw
     * any exception without needing to catch it. If any exception is thrown,
     * the client will still be disconnected.
     *
     * @param client the client that disconnected.
     * @throws Exception if an error occurs.
     * @see #failedClientDisconnection(TcpSession, Throwable)
     */
    /* @formatter:off */
    protected abstract void
            clientDisconnected(@NotNull TcpSession client) throws Exception;
    /* @formatter:on */

    /**
     * Called when client disconnection has failed. This usually occurs when
     * an exception is thrown by {@link #clientDisconnected(TcpSession)}.
     * <p>
     * <b>By default, this method does nothing.</b>
     *
     * @param client the client which failed to disconnect.
     * @param cause  the cause of failure.
     */
    protected void failedClientDisconnection(@NotNull TcpSession client,
                                             @NotNull Throwable cause) {
        /* optional implement */
    }

    /**
     * Called when a packet is received from a client.
     *
     * @param client the client which sent {@code buffer}.
     * @param buffer the packet sent by {@code client}.
     */
    protected abstract void handlePacket(@NotNull TcpSession client,
                                         @NotNull ByteBuf buffer);

    /**
     * Sets the callback for when a packet is received from a client.
     * <p>
     * <b>Note:</b> The {@link #handlePacket(TcpSession, ByteBuf)} method
     * is called for all received packets, even if a callback is set for
     * the client that sent it.
     *
     * @param client   the client whose packets to handle.
     * @param callback the code to execute when a packet is received from
     *                 {@code client}. A value of {@code null} is permitted,
     *                 and will result in nothing being executed.
     * @throws NullPointerException  if {@code client} is {@code null}.
     * @throws IllegalStateException if {@code client} is not connected to
     *                               the server.
     */
    public final void handlePackets(@NotNull TcpSession client,
                                    @Nullable BiConsumer<TcpSession, ByteBuf> callback) {
        Objects.requireNonNull(client, "client cannot be null");
        if (!this.isConnected(client)) {
            throw new IllegalArgumentException("client not connected");
        }

        ChannelPipeline pipeline = client.channel.pipeline();
        TcpServerChannelHandler handler =
                pipeline.get(TcpServerChannelHandler.class);
        handler.packetCallback = callback;
    }

    /**
     * @return {@code true} if the server is starting up, {@code false}
     * otherwise.
     */
    public final boolean isStartingUp() {
        return this.startingUp;
    }

    /**
     * @return {@code true} if the server is currently running, {@code false}
     * otherwise.
     */
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * Starts the server.
     *
     * @throws IllegalStateException if the server is already started.
     * @see #serverStartupBegun()
     * @see #serverStarted()
     */
    @MustBeInvokedByOverriders
    public void startup() {
        if (this.isRunning()) {
            throw new IllegalStateException("server already started");
        }

        this.serverStartupBegun();

        this.group = new NioEventLoopGroup();
        this.server = new ServerBootstrap();

        /*
         * This performs initial Netty setup. It tells Netty which event loop
         * group to use, what channel type to use, and what address it should
         * bind to. By setting the channel to NioServerSocketChannel, we're
         * telling Netty to create a TCP server socket.
         */
        server.group(group);
        server.channel(NioServerSocketChannel.class);
        server.localAddress(port);

        /* tell Netty how to initialize new TCP connections */
        server.childHandler(new TcpServerChannelInitializer(this));

        /*
         * Finally, create a server future by binding the socket. A call to
         * sync() is made to make sure this method does not return until the
         * server socket has been fully bound.
         */
        server.bind().syncUninterruptibly();

        this.running = true;
        this.serverStarted();
    }

    /**
     * Called when the server has begun startup. One should be careful which
     * server methods they call here, the server is not yet initialized.
     *
     * @see #isStartingUp()
     * @see #serverStarted()
     */
    protected void serverStartupBegun() {
        /* optional implement */
    }

    /**
     * Called when the server has finished startup.
     *
     * @see #serverStartupBegun()
     */
    protected void serverStarted() {
        /* optional implement */
    }

    /**
     * @return {@code true} if the server is shutting down, {@code false}
     * otherwise.
     */
    public final boolean isShuttingDown() {
        return this.shuttingDown;
    }

    /**
     * Stops the server.
     *
     * @throws IllegalStateException if the server is not started.
     */
    @MustBeInvokedByOverriders
    public void shutdown() {
        if (!this.isRunning()) {
            throw new IllegalStateException("server not started");
        }

        this.shuttingDown = true;
        this.serverShutdownBegun();

        Iterator<TcpSession> clientsI = connected.values().iterator();
        while (clientsI.hasNext()) {
            TcpSession client = clientsI.next();
            clientsI.remove();
            this.disconnectClient(client, false);
        }

        group.shutdownGracefully();

        this.group = null;
        this.server = null;

        this.running = false;
        this.shuttingDown = false;
        this.serverShutdown();
    }

    /**
     * Called when the server has begun shutdown. This is a good place to do
     * needed cleanup, like send clients a disconnect packet if the protocol
     * supports such a thing.
     *
     * @see #isShuttingDown()
     * @see #serverShutdown()
     */
    protected void serverShutdownBegun() {
        /* optional implement */
    }

    /**
     * Called when the server has finished shutdown. At this point, all
     * internal resources of the server have been cleaned up. Tasks like
     * sending packets to the client are no longer possible.
     *
     * @see #serverShutdownBegun()
     */
    protected void serverShutdown() {
        /* optional implement */
    }

}
