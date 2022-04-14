package csci4490.uno.network;

import io.netty.channel.socket.SocketChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TcpServerChannelInitializerTest {

    private TcpServer server;
    private TcpServerChannelInitializer initializer;

    @BeforeEach
    void setup() {
        this.server = mock(TcpServer.class);
        this.initializer = new TcpServerChannelInitializer(server);
    }

    @Test
    void initChannel() {
        /*
         * When a channel is initialized, the initializer must notify the
         * server that a new client has connected. Failure to do so would
         * negate its entire purpose.
         */
        SocketChannel channel = mock(SocketChannel.class);
        initializer.initChannel(channel);
        verify(server).connectClient(channel);
    }

}
