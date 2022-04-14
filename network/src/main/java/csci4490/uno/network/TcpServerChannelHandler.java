package csci4490.uno.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

final class TcpServerChannelHandler extends ChannelInboundHandlerAdapter {

    private final TcpServer server;
    private final TcpSession client;

    @Nullable BiConsumer<TcpSession, ByteBuf> packetCallback;

    TcpServerChannelHandler(@NotNull TcpServer server,
                            @NotNull TcpSession client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx,
                            @NotNull Object msg) {
        ByteBuf buffer = (ByteBuf) msg;

        /*
         * If a packet callback is set, execute it using a copy of the
         * original buffer. This allows the server to still handle the
         * packet if it desires. If the callback read from the original
         * buffer, it would more-or-less corrupt it for the server.
         */
        if (packetCallback != null) {
            ByteBuf copied = buffer.copy();
            packetCallback.accept(client, copied);
            copied.release(); /* free resources */
        }

        server.handlePacket(client, buffer);
        buffer.release(); /* free resources */
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) {
        server.disconnectClient(client);
    }

}
