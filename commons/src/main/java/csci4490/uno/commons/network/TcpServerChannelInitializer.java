package csci4490.uno.commons.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.jetbrains.annotations.NotNull;

final class TcpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final @NotNull TcpServer server;

    TcpServerChannelInitializer(@NotNull TcpServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(@NotNull SocketChannel channel) {
        server.connectClient(channel);
    }

}
