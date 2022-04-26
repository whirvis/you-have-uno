package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Represents an UNO tavern.
 *
 * @see #getAddress()
 */
public interface UnoTavern {

    /**
     * @return the IP address of this tavern.
     */
    @NotNull InetSocketAddress getAddress();

    /**
     * A keep alive is employed by the UNO dealer server to determine if
     * an UNO tavern is still online. A keep alive must be sent at least
     * once every thirty seconds to be considered online.
     *
     * @return the last keep alive time, specified in milliseconds.
     */
    long getLastKeepAlive();

}
