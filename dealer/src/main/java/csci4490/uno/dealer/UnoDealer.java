package csci4490.uno.dealer;

import csci4490.uno.dealer.account.AccountManager;
import csci4490.uno.dealer.config.Config;
import csci4490.uno.dealer.config.PropertiesConfig;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UnoDealer {

    private Connection dbConnection;
    private AccountManager accountManager;
    private boolean started;

    private UnoDealer() {
        /* make constructor private */
    }

    private void requireStarted() {
        if (!started) {
            throw new IllegalStateException("dealer not started");
        }
    }

    /**
     * @return the dealer's account manager.
     * @throws IllegalStateException if the dealer has yet to be started.
     */
    public @NotNull AccountManager getAccountManager() {
        this.requireStarted();
        return this.accountManager;
    }

    private void connectDb() throws IOException, SQLException {
        Config dbConfig = new PropertiesConfig();
        dbConfig.loadResource("/db.properties");

        String url = dbConfig.getProperty("url");
        String user = dbConfig.getProperty("user");
        String password = dbConfig.getProperty("password");

        if (url == null || user == null || password == null) {
            String msg = "url, user, and password required";
            throw new IOException(msg);
        }

        this.dbConnection = DriverManager.getConnection(url, user, password);
    }

    private void createManagers() {
        this.accountManager = new AccountManager(dbConnection);
    }

    private void start() throws Exception {
        if (started) {
            throw new IllegalStateException("dealer already started");
        }

        this.connectDb();
        this.createManagers();

        this.started = true;
    }

    public static void main(String[] args) throws Exception {
        UnoDealer dealer = new UnoDealer();
        dealer.start();
    }

}
