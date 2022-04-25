package csci4490.uno.commons.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

final class TcpClientChannelHandler extends ChannelInboundHandlerAdapter {

    private final TcpClient client;
    private TcpSession server;

    @Nullable BiConsumer<TcpSession, ByteBuf> packetCallback;

    public TcpClientChannelHandler(@NotNull TcpClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        this.server = client.connectServer(ctx.channel());
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx,
                            @NotNull Object msg) {
        ByteBuf buffer = (ByteBuf) msg;

        /*
         * If a packet callback is set, execute it using a copy of the
         * original buffer. This allows the client to still handle the
         * packet if it desires. If the callback read from the original
         * buffer, it would more-or-less corrupt it for the client.
         */
        if (packetCallback != null) {
            ByteBuf copied = buffer.copy();
            packetCallback.accept(server, copied);
            copied.release(); /* free resources */
        }

        client.handlePacket(server, buffer);
        buffer.release(); /* free resources */
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) {
        client.disconnectServer();
        this.server = null;
    }

}
