package csci4490.uno.commons.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * A packet which can be sent and received over the network. This class must
 * be extended to provide definitions for encoding and decoding.
 *
 * @see #encode(ByteBuf)
 * @see #decode(ByteBuf)
 */
public abstract class TcpPacket {

    /**
     * The ID of this packet. This is written to the internal buffer
     * automatically each time the packet is encoded.
     */
    public final int packetId;

    /**
     * This value is guaranteed not to be {@code null} when encoding or
     * decoding this packet.
     */
    protected ByteBuf buffer;

    /**
     * @param packetId the packet ID, which is an {@code unsigned short}.
     *                 This will be written to the internal buffer
     *                 automatically each time the packet is encoded.
     * @throws IllegalArgumentException if {@code packetId} is not within
     *                                  range of the accepted values for
     *                                  an {@code unsigned short}.
     */
    public TcpPacket(int packetId) {
        if (packetId < 0x0000 || packetId > 0xFFFF) {
            String msg = "packetId must be within range";
            msg += " of an unsigned short";
            throw new IllegalArgumentException(msg);
        }
        this.packetId = packetId;
    }

    /**
     * Reads a {@code UTF-8} encoded string from the internal {@link #buffer}
     * of this packet.
     *
     * @return the read string.
     */
    public @NotNull String readString() {
        int len = buffer.readUnsignedShort();
        byte[] sequence = new byte[len];
        buffer.readBytes(sequence);
        return new String(sequence, StandardCharsets.UTF_8);
    }

    /**
     * Writes a string with {@code UTF-8} encoding to the internal
     * {@link #buffer} of this packet.
     *
     * @param str the string to write.
     * @return this packet.
     * @throws NullPointerException if {@code str} is {@code null}.
     */
    public @NotNull TcpPacket writeString(@NotNull String str) {
        Objects.requireNonNull(str, "str cannot be null");
        byte[] sequence = str.getBytes(StandardCharsets.UTF_8);
        buffer.writeShort(sequence.length);
        buffer.writeBytes(sequence);
        return this;
    }

    /**
     * Reads an {@link InetSocketAddress} from the internal {@link #buffer}
     * of this packet.
     *
     * @return the read address.
     */
    public @NotNull InetSocketAddress readAddress() {
        String host = this.readString();
        int port = buffer.readUnsignedShort();
        return new InetSocketAddress(host, port);
    }

    /**
     * Writes an {@link InetSocketAddress} to the internal {@link #buffer}
     * of this packet. The host string of {@code address} is written first
     * using {@link #writeString(String)}. The port of {@code address} is
     * written next as an {@code unsigned short}.
     *
     * @param address the address to write.
     * @return this packet.
     * @throws NullPointerException if {@code address} is {@code null}.
     */
    public @NotNull TcpPacket writeAddress(@NotNull InetSocketAddress address) {
        Objects.requireNonNull(address, "address cannot be null");
        this.writeString(address.getHostString());
        buffer.writeShort(address.getPort());
        return this;
    }

    /**
     * Reads a {@link UUID} from the internal {@link #buffer} of this packet.
     *
     * @return the read UUID.
     */
    public @NotNull UUID readUUID() {
        long mostSigBits = buffer.readLong();
        long leastSigBits = buffer.readLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * Writes a {@link UUID} to the internal {@link #buffer} of this packet.
     * Bot the most significant bits and least significant bits are written
     * as a {@code signed long}. The most significant bits are written first,
     * and the least significant bits are written last.
     *
     * @param uuid the UUID to write.
     * @return this packet.
     * @throws NullPointerException if {@code uuid} is {@code null}.
     */
    public @NotNull TcpPacket writeUUID(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    /**
     * Encodes the packet. The buffer to write to can be accessed via the
     * internal {@link #buffer} field. There are also helper methods which
     * exist to standardize the encoding of data not directly supported by
     * Netty's {@link ByteBuf} class.
     *
     * @see #writeString(String)
     */
    protected abstract void encode();

    /**
     * Encodes the packet to the specified buffer.
     *
     * @param dest the buffer to write to.
     * @return this packet.
     * @throws NullPointerException if {@code dest} is {@code null}.
     */
    public final @NotNull TcpPacket encode(@NotNull ByteBuf dest) {
        Objects.requireNonNull(dest, "dest cannot be null");

        this.buffer = dest;
        synchronized (this) {
            buffer.writeShort(packetId);
            this.encode();
        }
        this.buffer = null;

        return this;
    }

    /**
     * Encodes the packet to a buffer allocated by this method and sends it
     * to the specified session via {@link TcpSession#sendPacket(ByteBuf)}.
     *
     * @param session the session to send this packet to.
     * @return this packet.
     * @throws NullPointerException if {@code session} is {@code null}.
     */
    public final @NotNull TcpPacket encodeAndSend(@NotNull TcpSession session) {
        Objects.requireNonNull(session, "session cannot be null");

        ByteBuf buffer = Unpooled.buffer();
        this.encode(buffer);
        session.sendPacket(buffer);

        return this;
    }

    /**
     * Decodes the packet. The buffer to read from can be accessed via the
     * internal {@link #buffer} field. There are also helper methods which
     * exist to standardize the decoding of data not directly supported by
     * Netty's {@link ByteBuf} class.
     *
     * @see #readString()
     */
    protected abstract void decode();

    /**
     * Decodes the packet from the specified buffer.
     *
     * @param src the buffer to read from.
     * @return this packet.
     * @throws NullPointerException     if {@code src} is {@code null}.
     * @throws IllegalArgumentException if the first {@code unsigned short}
     *                                  read from {@code src} is not equal
     *                                  to the ID of this packet.
     */
    public final @NotNull TcpPacket decode(@NotNull ByteBuf src) {
        Objects.requireNonNull(src, "src cannot be null");

        this.buffer = src;
        synchronized (this) {
            int srcId = src.readUnsignedShort();
            if (srcId != packetId) {
                String msg = "srcId (" + srcId + ") does not match";
                msg += " expected packetId (" + packetId + ")";
                throw new IllegalArgumentException(msg);
            }
            this.decode();
        }
        this.buffer = null;

        return this;
    }

}
