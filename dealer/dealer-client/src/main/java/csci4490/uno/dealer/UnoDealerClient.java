package csci4490.uno.dealer;

import csci4490.uno.commons.UnoJson;
import csci4490.uno.dealer.response.account.AccountCreateResponse;
import csci4490.uno.dealer.response.account.AccountInfoResponse;
import csci4490.uno.dealer.response.account.AccountUUIDResponse;
import csci4490.uno.dealer.response.game.GameCreateResponse;
import csci4490.uno.dealer.response.game.GameInfoResponse;
import csci4490.uno.dealer.response.login.LoginResponse;
import csci4490.uno.dealer.response.login.LoginVerifyResponse;
import csci4490.uno.dealer.response.tavern.TavernListResponse;
import csci4490.uno.dealer.response.tavern.TavernRegisterResponse;
import csci4490.uno.dealer.response.visit.VisitBeginResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static csci4490.uno.dealer.UnoEndpoints.*;

public class UnoDealerClient {

    /**
     * Assuming SSL is enabled, the HTTP client will be able to communicate
     * over a secure channel with the UNO dealer server. However, it will
     * <i>only</i> be able to communicate with the UNO dealer.
     *
     * @param disableSSL {@code true} if SSL should be disabled for the HTTP
     *                   client being created, {@code false otherwise}. This
     *                   should only be {@code true} when performing tests
     *                   with an offline UNO dealer server.
     * @return the HTTP client to use for communication.
     */
    /* @formatter:off */
    private static @NotNull CloseableHttpClient
            createHttpClient(boolean disableSSL) {
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
                RegistryBuilder.<ConnectionSocketFactory>create().register(
                        "https", sslFactory).build();
        BasicHttpClientConnectionManager manager =
                new BasicHttpClientConnectionManager(registry);

        HttpClientBuilder builder = HttpClients.custom();
        builder.setSSLSocketFactory(sslFactory);
        builder.setConnectionManager(manager);

        return builder.build();
    }
    /* @formatter:on */

    /**
     * Constructs a new {@link UrlEncodedFormEntity} from a given map. Each
     * entry of the map is converted to a name value pair, with each value
     * converted to a string using {@link Objects#toString(Object)}.
     *
     * @param form the map used to create the form.
     * @return the instantiated form entity.
     * @throws NullPointerException if {@code form} is {@code null}.
     */
    /* @formatter:off */
    public static @NotNull UrlEncodedFormEntity
            createForm(@NotNull Map<String, ?> form) {
        Objects.requireNonNull(form, "form cannot be null");
        Collection<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, ?> entry : form.entrySet()) {
            String key = entry.getKey();
            String value = Objects.toString(entry.getValue());
            pairs.add(new BasicNameValuePair(key, value));
        }
        return new UrlEncodedFormEntity(pairs);
    }
    /* @formatter:on */

    private final InetSocketAddress dealerAddress;
    private final boolean disableSSL;
    private final CloseableHttpClient http;

    /**
     * Constructs a new {@code UnoClient} that will make requests to the
     * UNO dealer at the specified address.
     *
     * @param dealerAddress the address of the UNO dealer.
     * @throws NullPointerException if {@code dealerAddress} is {@code null}.
     */
    public UnoDealerClient(@NotNull InetSocketAddress dealerAddress) {
        this.dealerAddress = Objects.requireNonNull(dealerAddress,
                "dealerAddress cannot be null");

        String disableJks = System.getProperty("disable_jks", "false");
        this.disableSSL = Boolean.parseBoolean(disableJks);
        if (disableSSL) {
            System.out.println("WARNING: DISABLED JKS ON DEALER CLIENT");
        }

        this.http = createHttpClient(disableSSL);
    }

    /**
     * Constructs a new {@code UnoClient} that will make requests to the
     * official Uno Dealer, specified via {@link UnoEndpoints#OFFICIAL}.
     */
    public UnoDealerClient() {
        this(OFFICIAL);
    }

    public final boolean isSSLDisabled() {
        return this.disableSSL;
    }

    /**
     * A utility method which generates an endpoint URI from a given path.
     * This takes into account the address of the UNO dealer, whether SSL
     * is disabled or not, etc.
     *
     * @param path       the endpoint path.
     * @param parameters the query parameters.
     * @return the URI for the specified endpoint.
     * @throws NullPointerException if {@code path} or {@code parameters}
     *                              are {@code null}.
     */
    /* @formatter:off */
    public final @NotNull URI
            getEndpoint(@NotNull String path,
                        @NotNull List<NameValuePair> parameters) {
        Objects.requireNonNull(path, "path cannot be null");
        Objects.requireNonNull(parameters, "parameters cannot be null");

        URIBuilder uri = new URIBuilder();
        uri.setScheme(disableSSL ? "http" : "https");
        uri.setHost(dealerAddress.getHostName());
        uri.setPort(dealerAddress.getPort());
        uri.setPath(path);

        uri.setParameters(parameters);

        try {
            return uri.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /* @formatter:on */

    /**
     * A utility method which generates an endpoint URI from a given path.
     * This takes into account the address of the UNO dealer, whether SSL
     * is disabled or not, etc.
     *
     * @param path       the endpoint path.
     * @param parameters the query parameters.
     * @return the URI for the specified endpoint.
     * @throws NullPointerException if {@code path} or {@code parameters}
     *                              are {@code null}.
     */
    /* @formatter:off */
    public final @NotNull URI
            getEndpoint(@NotNull String path,
                        @NotNull Map<String, ?> parameters) {
        Objects.requireNonNull(path, "path cannot be null");
        Objects.requireNonNull(parameters, "parameters cannot be null");

        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, ?> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = Objects.toString(entry.getValue());
            pairs.add(new BasicNameValuePair(key, value));
        }

        return this.getEndpoint(path, pairs);
    }
    /* @formatter:on */

    /**
     * A utility method which generates an endpoint URI from a given path.
     * This takes into account the address of the UNO dealer, whether SSL
     * is disabled or not, etc.
     * <p>
     * This method is a shorthand for {@link #getEndpoint(String, List)},
     * with the argument for {@code parameters} being an empty list.
     *
     * @param path the endpoint path.
     * @return the URI for the specified endpoint.
     * @throws NullPointerException if {@code path} is {@code null}.
     */
    public final @NotNull URI getEndpoint(@NotNull String path) {
        return this.getEndpoint(path, Collections.emptyList());
    }

    /**
     * Sends a request to create an account with the specified username and
     * password to the UNO dealer server.
     *
     * @param username the username of the account.
     * @param password the password of the account.
     * @return the result of account creation.
     * @throws NullPointerException if {@code username} or {@code password}
     *                              are {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    /* @formatter:off */
    public @NotNull AccountCreateResponse
            createAccount(@NotNull String username, @NotNull String password)
            throws IOException {
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        URI endpoint = this.getEndpoint(UNO_ACCOUNT_CREATE);
        HttpPost post = new HttpPost(endpoint);

        Map<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new AccountCreateResponse(response);
        }
    }
    /* @formatter:on */

    /**
     * Sends a request for the UUID of an account with the specified username
     * to the UNO dealer server.
     *
     * @param username the username of the account.
     * @return the result of getting the UUID.
     * @throws NullPointerException if {@code username} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    /* @formatter:off */
    public @NotNull AccountUUIDResponse
            getUUID(@NotNull String username) throws IOException {
        Objects.requireNonNull(username, "username cannot be null");

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        URI endpoint = this.getEndpoint(UNO_ACCOUNT_UUID, params);
        HttpGet get = new HttpGet(endpoint);

        try (CloseableHttpResponse response = http.execute(get)) {
            return new AccountUUIDResponse(response);
        }
    }
    /* @formatter:on */

    /**
     * Sends a request for the information of an account with the specified
     * UUID to the UNO dealer server.
     *
     * @param uuid the UUID of the account.
     * @return the result of requesting the account info.
     * @throws NullPointerException if {@code uuid} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see #getUUID(String)
     */
    /* @formatter:off */
    public @NotNull AccountInfoResponse
            getAccountInfo(@NotNull UUID uuid) throws IOException {
        Objects.requireNonNull(uuid, "uuid cannot be null");

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);

        URI endpoint = this.getEndpoint(UNO_ACCOUNT_INFO, params);
        HttpGet get = new HttpGet(endpoint);

        try (CloseableHttpResponse response = http.execute(get)) {
            return new AccountInfoResponse(response);
        }
    }
    /* @formatter:on */

    /**
     * Sends a login request for the account with the specified UUID to the
     * UNO dealer server.
     *
     * @param uuid     the account UUID.
     * @param password the account password.
     * @return the result of logging in.
     * @throws NullPointerException if {@code uuid} or {@code password}
     *                              are {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see #getUUID(String)
     */
    /* @formatter:off */
    public @NotNull LoginResponse
            loginAccount(@NotNull UUID uuid, @NotNull String password)
            throws IOException {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        URI endpoint = this.getEndpoint(UNO_LOGIN);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", uuid);
        form.put("password", password);
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new LoginResponse(response);
        }
    }
    /* @formatter:on */

    /**
     * Sends a request to the UNO dealer server to verify that an existing
     * login is still valid.
     *
     * @param login the login to verify.
     * @return the login verification response.
     * @throws NullPointerException if {@code login} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    /* @formatter:off */
    public @NotNull LoginVerifyResponse
            verifyLogin(@NotNull UnoLogin login) throws IOException {
        Objects.requireNonNull(login, "login cannot be null");

        URI endpoint = this.getEndpoint(UNO_LOGIN_VERIFY);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", login.getAccount().getUUID());
        form.put("access_token", login.getAccessToken());
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new LoginVerifyResponse(response);
        }
    }
    /* @formatter:on */

    /**
     * Sends a visit begin request for the account with the specified UUID to
     * the UNO dealer server.
     *
     * @param uuid        the account UUID.
     * @param accessToken the access token.
     * @return the response to the visit begin request.
     * @throws NullPointerException if {@code uuid} or {@code accessToken}
     *                              are {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see #loginAccount(UUID, String)
     */
    /* @formatter:off */
    public @NotNull VisitBeginResponse
            beginVisit(@NotNull UUID uuid, @NotNull UUID accessToken)
            throws IOException {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(accessToken, "accessToken cannot be null");

        URI endpoint = this.getEndpoint(UNO_VISIT_BEGIN);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", uuid);
        form.put("access_token", accessToken);
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new VisitBeginResponse(response);
        }
    }
    /* @formatter:on */


    /**
     * Sends a tavern register request for a tavern server on this machine
     * with the specified port to the UNO dealer server.
     *
     * @param port the tavern server port.
     * @return the response to the tavern register request.
     * @throws IOException if an I/O error occurs.
     * @see #getTaverns(int)
     */
    public @NotNull TavernRegisterResponse registerTavern(int port) throws IOException {
        URI endpoint = this.getEndpoint(UNO_TAVERN_REGISTER);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("port", port);
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new TavernRegisterResponse(response);
        }
    }

    /**
     * Sends a tavern list request for the currently registered tavern
     * servers to the UNO dealer server.
     *
     * @param max the maximum amount of servers to return.
     * @return the response to the tavern list request.
     * @throws IOException if an I/O error occurs.
     * @see #registerTavern(int)
     */
    public @NotNull TavernListResponse getTaverns(int max) throws IOException {
        Map<String, Object> query = new HashMap<>();
        query.put("max", max);

        URI endpoint = this.getEndpoint(UNO_TAVERN_LIST, query);
        HttpGet get = new HttpGet(endpoint);

        try (CloseableHttpResponse response = http.execute(get)) {
            return new TavernListResponse(response);
        }
    }

    /**
     * Sends a tavern list request for the currently registered tavern
     * servers to the UNO dealer server. No more than ten servers will
     * be returned.
     *
     * @return the response to the tavern list request.
     * @throws IOException if an I/O error occurs.
     * @see #registerTavern(int)
     */
    public @NotNull TavernListResponse getTaverns() throws IOException {
        return this.getTaverns(10);
    }

    public @NotNull GameCreateResponse createGame(@NotNull UUID hostId,
                                                  @NotNull UUID lobbyId,
                                                  @NotNull InetSocketAddress tavernAddress) throws IOException {
        URI endpoint = this.getEndpoint(UNO_GAME_CREATE);
        HttpPost post = new HttpPost(endpoint);

        Map<String, Object> form = new HashMap<>();
        form.put("uuid", hostId.toString());
        form.put("lobby_id", lobbyId.toString());
        form.put("tavern_ip", tavernAddress.getHostString());
        form.put("tavern_port", tavernAddress.getPort());
        post.setEntity(createForm(form));

        try (CloseableHttpResponse response = http.execute(post)) {
            return new GameCreateResponse(response);
        }
    }

    public @NotNull GameInfoResponse getGame(@NotNull String code) throws IOException {
        Map<String, Object> query = new HashMap<>();
        query.put("code", code);

        URI endpoint = this.getEndpoint(UNO_GAME_INFO, query);
        HttpGet get = new HttpGet(endpoint);

        try (CloseableHttpResponse response = http.execute(get)) {
            return new GameInfoResponse(response);
        }
    }

}
