package csci4490.uno.dealer.packet;

import csci4490.uno.commons.network.TcpPacket;

public abstract class TavernPacket extends TcpPacket {

    /* @formatter:off */
    public static final short
            ID_OPEN_TAVERN   = 0x01,
            ID_JOIN_TAVERN   = 0x02,
            ID_QUIT_TAVERN   = 0x03,
            ID_PEER_MESSAGE  = 0x04,
            ID_ACCEPT_JOIN   = 0xF1,
            ID_DENY_JOIN     = 0xF2;
    /* @formatter:on */

    /**
     * @param packetId the packet ID, which is an {@code unsigned short}.
     *                 This will be written to the internal buffer
     *                 automatically each time the packet is encoded.
     * @throws IllegalArgumentException if {@code packetId} is not within
     *                                  range of the accepted values for
     *                                  an {@code unsigned short}.
     */
    public TavernPacket(int packetId) {
        super(packetId);
    }

}
