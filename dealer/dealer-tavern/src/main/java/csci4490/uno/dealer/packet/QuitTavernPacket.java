package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.Nullable;

public class QuitTavernPacket extends TavernPacket {

    public @Nullable String reason;

    public QuitTavernPacket() {
        super(ID_QUIT_TAVERN);
    }

    @Override
    protected void encode() {
        buffer.writeBoolean(reason != null);
        if (reason != null) {
            this.writeString(reason);
        }
    }

    @Override
    protected void decode() {
        boolean hasReason = buffer.readBoolean();
        if (hasReason) {
            this.reason = this.readString();
        }
    }

}
