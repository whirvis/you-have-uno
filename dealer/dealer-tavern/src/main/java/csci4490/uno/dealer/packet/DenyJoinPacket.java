package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.Nullable;

public class DenyJoinPacket extends TavernPacket {

    public @Nullable String reason;

    public DenyJoinPacket() {
        super(ID_DENY_JOIN);
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
