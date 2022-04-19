package network;

import csci4490.uno.commons.network.TcpServer;
import csci4490.uno.commons.network.TcpSession;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

class MockTcpServer extends TcpServer {

    boolean connectedClient;
    boolean disconnectedClient;
    boolean handledPacket;

    MockTcpServer(int port) {
        super(port);
    }

    @Override
    protected void clientConnected(@NotNull TcpSession client) {
        this.connectedClient = true;
    }

    @Override
    protected void clientDisconnected(@NotNull TcpSession client) {
        this.disconnectedClient = true;
    }

    @Override
    protected void handlePacket(@NotNull TcpSession client,
                                @NotNull ByteBuf buffer) {
        this.handledPacket = true;
    }

}
