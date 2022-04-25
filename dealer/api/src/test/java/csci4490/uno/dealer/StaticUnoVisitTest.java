package csci4490.uno.dealer;

import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("ConstantConditions")
class StaticUnoVisitTest {

    private StaticUnoAccount account;
    private InetAddress address;
    private UUID sessionToken;
    private long lastKeepAlive;

    private StaticUnoVisit visit;

    @BeforeEach
    void setup() throws IOException {
        UUID u = UUID.randomUUID();
        String a = "waa";

        this.account = new StaticUnoAccount(u, a);
        this.address = InetAddress.getByName("localhost");
        this.sessionToken = UUID.randomUUID();
        this.lastKeepAlive = 123456789L;

        this.visit = new StaticUnoVisit(account, address, sessionToken, lastKeepAlive);
    }

    @Test
    void init() {
        assertThrows(NullPointerException.class,
                () -> new StaticUnoVisit(null, address, sessionToken, lastKeepAlive));
        assertThrows(NullPointerException.class,
                () -> new StaticUnoVisit(account, null, sessionToken, lastKeepAlive));
        assertThrows(NullPointerException.class,
                () -> new StaticUnoVisit(account, address, null, lastKeepAlive));
    }

    @Test
    void json() {
        JsonObject json = new JsonObject();

        json.add("account", UnoJson.toJson(account));
        json.add("address", UnoJson.toJson(address));
        json.addProperty("session_token", sessionToken.toString());
        json.addProperty("last_keep_alive", lastKeepAlive);

        System.out.println(UnoJson.GSON.toJson(json));

        UnoJson.fromJson(json, StaticUnoVisit.class);
    }

    @Test
    void getAccount() {
        assertEquals(account, visit.getAccount());
    }

    @Test
    void getAddress() {
        assertEquals(address, visit.getAddress());
    }

    @Test
    void getSessionToken() {
        assertEquals(sessionToken, visit.getSessionToken());
    }

    @Test
    void getLastKeepAlive() {
        assertEquals(lastKeepAlive, visit.getLastKeepAlive());
    }

}
