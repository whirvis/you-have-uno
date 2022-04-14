package csci4490.uno.network;

class NetUtils {

    private static final int PORT_MIN = 0x0000;
    private static final int PORT_MAX = 0xFFFF;

    /**
     * @param port the port to validate.
     * @return the validated port.
     * @throws IllegalArgumentException if {@code port} is less than
     *                                  {@value #PORT_MIN} or greater
     *                                  than {@value #PORT_MAX}.
     */
    static int requirePort(int port) {
        if (port < PORT_MIN || port > PORT_MAX) {
            String msg = "port must be within range";
            msg += " of " + PORT_MIN + " to " + PORT_MAX;
            throw new IllegalArgumentException(msg);
        }
        return port;
    }

}
