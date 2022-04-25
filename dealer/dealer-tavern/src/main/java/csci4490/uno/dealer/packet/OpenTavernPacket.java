package csci4490.uno.dealer.packet;

public class OpenTavernPacket extends TavernPacket {

    public byte[] publicKey;

    public OpenTavernPacket() {
        super(ID_OPEN_TAVERN);
    }

    @Override
    protected void encode() {
        buffer.writeShort(publicKey.length);
        buffer.writeBytes(publicKey);
    }

    @Override
    protected void decode() {
        int publicKeyLength = buffer.readUnsignedShort();
        this.publicKey = new byte[publicKeyLength];
        buffer.readBytes(publicKey);
    }

}
