package network;

import csci4490.uno.commons.network.NetUtils;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NetUtilsTest {

    @Test
    void requirePort() {
        /*
         * The purpose of requirePort() is to validate that the specified
         * port is within range of a valid networking port. As such, any
         * values outside the range of 0 to 65535 should result in an
         * exception being thrown.
         */
        assertThrows(IllegalArgumentException.class,
                () -> NetUtils.requirePort(Integer.MIN_VALUE));
        assertThrows(IllegalArgumentException.class,
                () -> NetUtils.requirePort(Integer.MAX_VALUE));

        /*
         * When a valid port is specified as the argument, this method
         * should return that value exactly.
         */
        int port = new Random().nextInt(0x10000);
        assertEquals(port, NetUtils.requirePort(port));
    }

}
