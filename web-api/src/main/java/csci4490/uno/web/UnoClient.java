package csci4490.uno.web;

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
import org.apache.http.ssl.SSLContexts;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Objects;

public class UnoClient {

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
        URL unoTrustResource = UnoClient.class.getResource(unoTrustPath);
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
    public UnoClient(@NotNull InetSocketAddress dealerAddress,
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
    public UnoClient() {
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

}
