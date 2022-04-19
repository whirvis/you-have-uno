package csci4490.uno.dealer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class UnoDealerClient {

    private static final int DEALER_PORT = 48902;

    /* @formatter:off */
    public static final @NotNull InetSocketAddress
            LOCALHOST = new InetSocketAddress("localhost", DEALER_PORT),
            OFFICIAL = new InetSocketAddress("uno.whirvis.net", DEALER_PORT);
    /* @formatter:on */

    private static @NotNull CloseableHttpClient createHttpClient(boolean disableSSL) {
        if (disableSSL) {
            return HttpClients.createDefault();
        }

        String unoTrustPath = "/uno_trust.jks";
        String unoTrustPassword = "you_have_uno";

        /*
         * Before an HTTP client that supports SSL can be created, it must
         * first have a keystore. The keystore contains the CA for the SSL
         * certificates. In this case, the creators of "You Have Uno!" are
         * the only certificate authority.
         *
         * A caveat to this is that we will only be able to connect to the
         * official Uno Dealer, unless we reconfigure the client. However,
         * this isn't an issue as the HTTP client will only send requests
         * to the official UNO dealer.
         */
        URL unoTrustResource = UnoDealerClient.class.getResource(unoTrustPath);
        if (unoTrustResource == null) {
            String msg = "missing " + unoTrustPath;
            throw new RuntimeException(msg);
        }

        /*
         * After ensuring the keystore resource exists in the classpath,
         * instantiate a KeyStore object for the SSL context to use. This
         * context will then be used in creating the HTTP client.
         */
        SSLContext sslContext;
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(unoTrustResource.openStream(),
                    unoTrustPassword.toCharArray());
            sslContext = SSLContexts.custom().loadTrustMaterial(keystore,
                    null).build();
        } catch (GeneralSecurityException | IOException e) {
            String msg = "failure to load " + unoTrustPath;
            throw new RuntimeException(msg, e);
        }

        /*
         * The code below is used to tell the HTTP client how to create new
         * connections with SSL over HTTPS requests. It looks complicated,
         * but it really isn't. This is due to the long type names.
         */
        SSLConnectionSocketFactory sslFactory =
                new SSLConnectionSocketFactory(sslContext,
                        NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslFactory).build();
        BasicHttpClientConnectionManager manager =
                new BasicHttpClientConnectionManager(registry);

        HttpClientBuilder builder = HttpClients.custom();
        builder.setSSLSocketFactory(sslFactory);
        builder.setConnectionManager(manager);

        return builder.build();
    }

    private final InetSocketAddress dealerAddress;
    private final boolean disableSSL;
    private final CloseableHttpClient client;

    /**
     * Constructs a new {@code UnoClient} that will make requests to the
     * UNO dealer at the specified address.
     *
     * @param dealerAddress the address of the UNO dealer.
     * @param disableSSL    {@code true} to disable SSL, {@code false}
     *                      otherwise. Disabling SSL poses a major security
     *                      risk. As such, this should only be {@code true}
     *                      when testing the client.
     * @throws NullPointerException if {@code dealerAddress} is {@code null}.
     */
    public UnoDealerClient(@NotNull InetSocketAddress dealerAddress,
                           boolean disableSSL) {
        this.dealerAddress = Objects.requireNonNull(dealerAddress,
                "dealerAddress cannot be null");
        this.disableSSL = disableSSL;
        this.client = createHttpClient(disableSSL);
    }

    /**
     * Constructs a new {@code UnoClient} that will make requests to the
     * official Uno Dealer, specified via {@link #OFFICIAL}.
     */
    public UnoDealerClient() {
        this(OFFICIAL, false);
    }

    public final boolean isSSLDisabled() {
        return this.disableSSL;
    }

    /**
     * A utility method which generates an endpoint URI from a given path.
     * This takes into account the address of the UNO dealer, whether SSL
     * is disabled or not, etc.
     *
     * @param path the endpoint path.
     * @return the URI for the specified endpoint.
     * @throws NullPointerException if {@code path} is {@code null}.
     */
    protected final @NotNull URI endpointURI(@NotNull String path) {
        Objects.requireNonNull(path, "path cannot be null");

        URIBuilder uri = new URIBuilder();
        uri.setScheme(disableSSL ? "http" : "https");
        uri.setHost(dealerAddress.getHostName());
        uri.setPort(dealerAddress.getPort());
        uri.setPath(path);

        try {
            return uri.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void setFormParams(
            @NotNull HttpEntityEnclosingRequestBase request,
            @NotNull Map<String, String> params) {

        List<BasicNameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            pairs.add(new BasicNameValuePair(key, value));
        }

        try {
            request.setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getStr(@NotNull HttpResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        long length = response.getEntity().getContentLength();
        for(int i = 0; i < length; i++) {
            char c = (char) response.getEntity().getContent().read();
            builder.append(c);
        }
        return builder.toString();
    }

    public UnoAccount createAccount(@NotNull String username,
                                             @NotNull String password) {
        URI endpoint = this.endpointURI("/uno/account/create");
        HttpPost post = new HttpPost(endpoint);

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        this.setFormParams(post, params);

        try (CloseableHttpResponse response = client.execute(post)) {

            int code = response.getStatusLine().getStatusCode();
            if(code != HttpStatus.SC_OK) {
                System.err.println(getStr(response));
                return null;
            }

            InputStream in = response.getEntity().getContent();
            JsonObject responseJson = UnoJson.fromJson(in);
            JsonElement accountJson = responseJson.get("account");
            return UnoJson.fromJson(accountJson, UnoAccount.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        UnoDealerClient client = new UnoDealerClient(LOCALHOST, false);

        Scanner s = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        UnoAccount account = client.createAccount(username, password);
        if(account != null) {
            System.out.println("UUID: " + account.uuid);
        } else {
            System.err.println("Failed to create account.");
        }
    }

}
