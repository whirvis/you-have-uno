package csci4490.uno.dealer.manager;

import csci4490.uno.commons.SaltGenerator;
import csci4490.uno.dealer.UnoDealerServer;
import csci4490.uno.dealer.UnoLogin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public class LoginManager {

    private static Date getOneWeekFromNow() {
        Instant oneWeek = Instant.now().plus(7, ChronoUnit.DAYS);
        return new Date(oneWeek.toEpochMilli());
    }

    private final Connection db;
    private final WebLoginManager webManager;

    private AccountManager accountManager;

    /**
     * @param db the database connection.
     * @throws NullPointerException if {@code db} is {@code null}.
     * @see #getWebManager()
     */
    public LoginManager(@NotNull Connection db) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.webManager = new WebLoginManager(this);
    }

    public @NotNull WebLoginManager getWebManager() {
        return this.webManager;
    }

    private void requireAccountManager() {
        if (accountManager == null) {
            throw new IllegalStateException("");
        }
    }

    /**
     * Sets the account manager for this login manager. This is required for
     * select operations, like user account login.
     *
     * @param manager the account manager to use.
     * @throws NullPointerException  if {@code manager} is {@code null}.
     * @throws IllegalStateException if an account manager has already been
     *                               set for this login manager.
     */
    public void setAccountManager(@NotNull AccountManager manager) {
        Objects.requireNonNull(manager, "manager cannot be null");
        this.accountManager = manager;
    }

    private void invalidateAccess(@NotNull InetAddress address,
                                  @NotNull UUID uuid) throws SQLException {
        String sql = "DELETE FROM login WHERE";
        sql += " ip_address = INET6_ATON(?) AND uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());
            stmt.execute();
        }
    }

    /**
     * Logs a user into their account. This provides them an access token
     * they can use (instead of their password) to perform actions. This
     * token can only be used from the IP address that requested it.
     * Furthermore, it will only last for one week.
     * <p>
     * <b>Note:</b> If a user logs in from the same IP address twice,
     * the previous access token they were assigned is invalidated.
     *
     * @param address  the IP address the user is logging in from.
     * @param uuid     the UUID of the user logging in.
     * @param password the password being used for login.
     * @return the user login information, {@code null} if an incorrect
     * {@code uuid} and {@code password} combination was provided.
     * @throws NullPointerException  if {@code address}, {@code uuid},
     *                               or {@code password} are {@code null}.
     * @throws IllegalStateException if an account manager has not been
     *                               provided for this login manager.
     * @throws SQLException          if an SQL error occurs.
     * @see #setAccountManager(AccountManager)
     */
    public @Nullable UnoLogin loginAccount(@NotNull InetAddress address,
                                           @NotNull UUID uuid,
                                           @NotNull String password) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        this.requireAccountManager();
        if (!accountManager.verifyLogin(uuid, password)) {
            return null; /* invalid login */
        }

        /*
         * For both security and storage reasons, a user cannot have
         * multiple access tokens on the same IP address. Invalidate
         * the previous access token (if any) for the user.
         */
        this.invalidateAccess(address, uuid);

        UUID accessToken = UUID.randomUUID();
        String salt = SaltGenerator.generateSalt(32);
        Date expiresAt = getOneWeekFromNow();

        String sql = "INSERT INTO login";
        sql += " VALUES(INET6_ATON(?), ?, MD5(?), ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());
            stmt.setString(3, accessToken + salt);
            stmt.setString(4, salt);
            stmt.setDate(5, expiresAt);
            stmt.execute();
        }

        return new UnoLogin(address, uuid, accessToken, expiresAt);
    }

    /**
     * Verifies that an access token provided by a user from a certain IP
     * address is valid. This method should be used to authenticate users
     * before performing certain tasks, like beginning a visit.
     *
     * @param address     the IP address of the user.
     * @param uuid        the UUID of the user.
     * @param accessToken the provided access token.
     * @return {@code true} if a valid access token was provided by the user
     * from their IP address, {@code false} otherwise.
     * @throws NullPointerException if {@code address}, {@code uuid}, or
     *                              {@code accessToken} are {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean verifyAccess(@NotNull InetAddress address,
                                @NotNull UUID uuid,
                                @NotNull UUID accessToken) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(accessToken, "accessToken cannot be null");

        long currentTime = System.currentTimeMillis();
        String accessTokenHash; /* initialized by query */
        String accessTokenSalt; /* initialized by query */

        String sql = "SELECT * FROM login WHERE";
        sql += " ip_address = INET6_ATON(?) AND uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return false;
            }

            Date expiresAt = query.getDate("expires_at");
            if (currentTime > expiresAt.getTime()) {
                return false;
            }

            accessTokenHash = query.getString("access_token_hash");
            accessTokenSalt = query.getString("access_token_salt");
        }

        String saltedAccessToken = accessToken + accessTokenSalt;
        String calculatedHash = UnoDealerServer.getMD5(db, saltedAccessToken);
        return calculatedHash.equals(accessTokenHash);
    }

}
