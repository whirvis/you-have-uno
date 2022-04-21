package csci4490.uno.dealer;

import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.commons.scheduler.ThreadedScheduler;
import csci4490.uno.dealer.response.visit.VisitEndResponse;
import csci4490.uno.dealer.response.visit.VisitPingResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static csci4490.uno.dealer.UnoEndpoints.*;

public class LivingUnoVisit implements UnoVisit, Closeable {

    private static final Duration PING_WAIT = Duration.ofSeconds(15);

    private final UnoDealerClient client;
    private final CloseableHttpClient http;

    private final UnoAccount account;
    private final InetAddress address;
    private final UUID sessionToken;
    private long lastKeepAlive;

    private boolean ended;
    private boolean endedGracefully;

    private final Scheduler scheduler;

    public LivingUnoVisit(@NotNull UnoDealerClient client,
                          @NotNull CloseableHttpClient http,
                          @NotNull StaticUnoVisit visit) {
        Objects.requireNonNull(client, "client cannot be null");
        Objects.requireNonNull(http, "http cannot be null");
        Objects.requireNonNull(visit, "visit cannot be null");

        this.client = client;
        this.http = http;

        this.account = visit.getAccount();
        this.address = visit.getAddress();
        this.sessionToken = visit.getSessionToken();
        this.lastKeepAlive = visit.getLastKeepAlive();

        this.scheduler = new ThreadedScheduler();

        scheduler.scheduleForever(() -> {
            VisitPingResponse response = this.sendPing();
            long currentTime = System.currentTimeMillis();
            if (response.wasSuccessful()) {
                this.lastKeepAlive = currentTime;
            }
        }, PING_WAIT, PING_WAIT);
    }

    private @NotNull VisitPingResponse sendPing() throws IOException {
        URI endpoint = client.getEndpoint(UNO_VISIT_PING);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", account.getUUID());
        form.put("session_token", sessionToken);
        post.setEntity(UnoDealerClient.createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new VisitPingResponse(response);
        }
    }

    private @NotNull VisitEndResponse endVisit() throws IOException {
        URI endpoint = client.getEndpoint(UNO_VISIT_END);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", account.getUUID());
        form.put("session_token", sessionToken);
        post.setEntity(UnoDealerClient.createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new VisitEndResponse(response);
        }
    }

    @Override
    public @NotNull UnoAccount getAccount() {
        return this.account;
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return this.address;
    }

    @Override
    public @NotNull UUID getSessionToken() {
        return this.sessionToken;
    }

    @Override
    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }

    public boolean isEnded() {
        return this.ended;
    }

    public boolean endedGracefully() {
        return this.endedGracefully;
    }

    @Override
    public void close() throws IOException {
        if (this.isEnded()) {
            return;
        }

        scheduler.close();
        VisitEndResponse endResponse = this.endVisit();
        this.endedGracefully = endResponse.endedGracefully();

        this.ended = true;
    }

}
