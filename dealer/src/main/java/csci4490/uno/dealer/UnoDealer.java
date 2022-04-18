package csci4490.uno.dealer;

import csci4490.uno.commons.UnoJson;
import csci4490.uno.commons.config.Config;
import csci4490.uno.commons.config.ConfigException;
import csci4490.uno.commons.config.PropertiesConfig;
import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.commons.scheduler.ThreadedScheduler;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.manager.AccountManager;
import csci4490.uno.dealer.manager.LoginManager;
import csci4490.uno.dealer.manager.VisitManager;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UnoDealer {

    private static final File CONFIG_DIR = new File("./dealer/config");
    private static final int WEBSERVER_PORT = 48902;

    /**
     * Hashes a string with MD5 via MySQL.
     *
     * @param connection the database connection.
     * @param str        the string to hash.
     * @return the MD5 hash of {@code str}.
     * @throws SQLException         if an SQL error occurs.
     * @throws NullPointerException if {@code connection} or {@code str}
     *                              are {@code null}.
     */
    /* @formatter:off */
    public static @NotNull String
            getMD5(@NotNull Connection connection, @NotNull String str)
                    throws SQLException {
        Objects.requireNonNull(connection, "connection cannot be null");
        Objects.requireNonNull(str, "str cannot be null");

        String sql = "SELECT MD5(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, str);
            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                throw new SQLException("MD5 query failure");
            }
            return query.getString(1);
        }
    }
    /* @formatter:on */

    private final Scheduler scheduler;

    private Connection dbConnection;
    private AccountManager accountManager;
    private LoginManager loginManager;
    private VisitManager visitManager;
    private boolean started;

    private UnoDealer() {
        this.scheduler = new ThreadedScheduler();
    }

    private void connectDb() throws IOException, SQLException {
        Config dbConfig = new PropertiesConfig("Database Config");
        dbConfig.loadFile(new File(CONFIG_DIR, "db.properties"));

        String url = dbConfig.getProperty("url");
        String user = dbConfig.getProperty("user");
        String password = dbConfig.getProperty("password");

        if (url == null || user == null || password == null) {
            String msg = dbConfig.getName() + " cannot have null";
            msg += " URL, username, or password";
            throw new ConfigException(msg);
        }

        this.dbConnection = DriverManager.getConnection(url, user, password);
    }

    private void createManagers() {
        this.accountManager = new AccountManager(dbConnection);
        this.loginManager = new LoginManager(dbConnection);
        this.visitManager = new VisitManager(dbConnection);

        loginManager.setAccountManager(accountManager);
        visitManager.setLoginManager(loginManager);
    }

    private static @NotNull Server createSSLServer() {
        Server server = new Server();

        File unoDealerJks = new File(CONFIG_DIR, "uno_dealer.jks");
        if (!unoDealerJks.exists()) {
            String msg = "missing required keystore file";
            msg += " " + unoDealerJks.getPath();
            throw new ConfigException(msg);
        }

        SslContextFactory context = new SslContextFactory.Server();
        context.setKeyStorePath(unoDealerJks.getPath());
        context.setKeyStorePassword("you_have_uno");

        ServerConnector connector = new ServerConnector(server, context);
        connector.setPort(WEBSERVER_PORT);

        Connector[] connectors = new Connector[1];
        connectors[0] = connector;
        server.setConnectors(connectors);

        return server;
    }

    private void startWebServer() {
        Javalin webserver = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.jsonMapper(new GsonMapper(UnoJson.GSON));
            config.server(UnoDealer::createSSLServer);
        });

        Endpoints.handleExceptions(webserver);

        Endpoints.register(webserver, accountManager.getWebManager());
        Endpoints.register(webserver, loginManager.getWebManager());
        Endpoints.register(webserver, visitManager.getWebManager());

        visitManager.removeInactiveVisits(scheduler);

        webserver.start();
    }

    private void start() throws Exception {
        if (started) {
            throw new IllegalStateException("dealer already started");
        }

        this.connectDb();
        this.createManagers();
        this.startWebServer();

        this.started = true;
    }

    public static void main(String[] args) throws Exception {
        UnoDealer dealer = new UnoDealer();
        dealer.start();
    }

}
