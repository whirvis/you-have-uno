package csci4490.uno.dealer.packet;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("NotNullFieldNotInitialized")
public class JoinTavernPacket extends TavernPacket {

    public @NotNull UUID lobbyId;

    public JoinTavernPacket() {
        super(ID_JOIN_TAVERN);
    }

    @Override
    protected void encode() {
        this.writeUUID(lobbyId);
    }

    @Override
    protected void decode() {
        this.lobbyId = this.readUUID();
    }

}
