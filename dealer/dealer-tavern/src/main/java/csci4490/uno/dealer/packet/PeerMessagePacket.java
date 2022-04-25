package csci4490.uno.dealer.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@SuppressWarnings("NotNullFieldNotInitialized")
public class PeerMessagePacket extends TavernPacket {

    public @NotNull InetSocketAddress destination;
    public @NotNull ByteBuf content;

    public PeerMessagePacket() {
        super(ID_PEER_MESSAGE);
    }

    @Override
    protected void encode() {
        this.writeAddress(destination);

        int contentLength = content.writerIndex();
        byte[] contentData = new byte[contentLength];
        content.getBytes(0, contentData);

        buffer.writeShort(contentLength);
        buffer.writeBytes(contentData);
    }

    @Override
    protected void decode() {
        this.destination = this.readAddress();

        int contentLength = buffer.readUnsignedShort();
        byte[] contentData = new byte[contentLength];
        buffer.readBytes(contentData);

        this.content = Unpooled.wrappedBuffer(contentData);
    }

}
