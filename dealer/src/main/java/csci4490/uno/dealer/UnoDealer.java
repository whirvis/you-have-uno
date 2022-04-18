package csci4490.uno.dealer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import csci4490.uno.dealer.config.Config;
import csci4490.uno.dealer.config.ConfigException;
import csci4490.uno.dealer.config.PropertiesConfig;
import csci4490.uno.dealer.endpoint.Endpoints;
import csci4490.uno.dealer.manager.AccountManager;
import csci4490.uno.dealer.manager.LoginManager;
import csci4490.uno.dealer.manager.VisitManager;
import csci4490.uno.dealer.scheduler.Scheduler;
import csci4490.uno.dealer.scheduler.ThreadedScheduler;
import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UnoDealer {

    private static final int WEBSERVER_PORT = 48902;
    private static final Gson GSON =
            new GsonBuilder().serializeNulls().create();

    /**
     * Converts an {@code Object} to JSON.
     *
     * @param src the object to convert.
     * @return {@code src} converted to JSON.
     */
    public static JsonElement toJson(@Nullable Object src) {
        return GSON.toJsonTree(src);
    }

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
        dbConfig.loadFile("./dealer/config/db.properties");

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

    private void startWebServer() {
        Javalin webserver = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.jsonMapper(new GsonMapper(GSON));
        });

        Endpoints.handleExceptions(webserver);

        Endpoints.register(webserver, accountManager.getWebManager());
        Endpoints.register(webserver, loginManager.getWebManager());
        Endpoints.register(webserver, visitManager.getWebManager());

        visitManager.removeInactiveVisits(scheduler);

        webserver.start(WEBSERVER_PORT);
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
