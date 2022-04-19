package network;

import csci4490.uno.commons.network.TcpServer;
import csci4490.uno.commons.network.TcpServerChannelHandler;
import csci4490.uno.commons.network.TcpSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TcpServerChannelHandlerTest {

    private TcpServer server;
    private TcpSession client;
    private TcpServerChannelHandler handler;
    private ChannelHandlerContext ctx;

    @BeforeEach
    void setup() {
        this.server = mock(TcpServer.class);
        this.client = mock(TcpSession.class);
        this.handler = new TcpServerChannelHandler(server, client);
        this.ctx = mock(ChannelHandlerContext.class);
    }

    @Test
    void channelRead() {
        /* set callback for next test */
        AtomicBoolean handledPacket = new AtomicBoolean();
        handler.packetCallback = (c, b) -> handledPacket.set(true);

        ByteBuf buffer = mock(ByteBuf.class);
        ByteBuf copied = mock(ByteBuf.class);
        when(buffer.copy()).thenReturn(copied);

        handler.channelRead(ctx, buffer);

        /*
         * When a packet is received, the handler must notify the server
         * (and callback, if specified) that they have a packet from the
         * client they need to handle.
         */
        assertTrue(handledPacket.get());
        verify(server).handlePacket(client, buffer);

        /*
         * Furthermore, the buffers must be released after the packet is
         * handled to prevent a memory leak. This is done in the channel
         * handler for the convenience of the server and the callback.
         */
        verify(copied).release();
        verify(buffer).release();
    }

    @Test
    void channelInactive() {
        /*
         * When a channel has become inactive, the handler must notify
         * the server that the associated client has disconnected.
         */
        handler.channelInactive(ctx);
        verify(server).disconnectClient(client);
    }

}
