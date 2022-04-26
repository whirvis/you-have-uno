package csci4490.uno.dealer;

import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.commons.scheduler.ThreadedScheduler;
import csci4490.uno.dealer.response.tavern.TavernPingResponse;
import csci4490.uno.dealer.response.tavern.TavernUnregisterResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static csci4490.uno.dealer.UnoEndpoints.*;

public class LivingUnoTavern implements UnoTavern, Closeable {

    private static final Duration PING_WAIT = Duration.ofSeconds(15);

    private final UnoDealerClient client;
    private final CloseableHttpClient http;

    private final InetSocketAddress address;
    private long lastKeepAlive;

    private boolean unregistered;
    private boolean unregisteredGracefully;

    private final Scheduler scheduler;

    public LivingUnoTavern(@NotNull UnoDealerClient client,
                           @NotNull CloseableHttpClient http,
                           @NotNull StaticUnoTavern tavern) {
        Objects.requireNonNull(client, "client cannot be null");
        Objects.requireNonNull(http, "http cannot be null");
        Objects.requireNonNull(tavern, "tavern cannot be null");

        this.client = client;
        this.http = http;

        this.address = tavern.getAddress();
        this.lastKeepAlive = tavern.getLastKeepAlive();

        this.scheduler = new ThreadedScheduler();

        scheduler.scheduleForever(() -> {
            TavernPingResponse response = this.sendPing();
            long currentTime = System.currentTimeMillis();
            if (response.wasSuccessful) {
                this.lastKeepAlive = currentTime;
            }
        }, PING_WAIT, PING_WAIT);
    }

    private @NotNull TavernPingResponse sendPing() throws IOException {
        URI endpoint = client.getEndpoint(UNO_TAVERN_PING);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("port", address.getPort());
        post.setEntity(UnoDealerClient.createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new TavernPingResponse(response);
        }
    }

    private @NotNull TavernUnregisterResponse unregister() throws IOException {
        URI endpoint = client.getEndpoint(UNO_TAVERN_UNREGISTER);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("port", address.getPort());
        post.setEntity(UnoDealerClient.createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new TavernUnregisterResponse(response);
        }
    }

    @Override
    public @NotNull InetSocketAddress getAddress() {
        return this.address;
    }

    @Override
    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }

    public boolean isUnregistered() {
        return this.unregistered;
    }

    public boolean unregisteredGracefully() {
        return this.unregisteredGracefully;
    }

    @Override
    public void close() throws IOException {
        if(this.isUnregistered()) {
            return;
        }

        scheduler.close();
        TavernUnregisterResponse unregisterResponse = this.unregister();
        this.unregisteredGracefully = unregisterResponse.wasUnregistered;

        this.unregistered = true;
    }

}
