package csci4490.uno.dealer.account;

import csci4490.uno.dealer.SaltGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

/**
 * The account manager for the <i>You Have Uno!</i> database. This can be
 * used to create accounts, verify logins, etc.
 */
public class AccountManager {

    private final Connection db;
    private final WebAccountManager webManager;

    /**
     * @param db the database connection.
     * @throws NullPointerException if {@code db} is {@code null}.
     */
    public AccountManager(@NotNull Connection db) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.webManager = new WebAccountManager(this);
    }

    public @NotNull WebAccountManager getWebManager() {
        return this.webManager;
    }

    private @NotNull UUID findAvailableId() throws SQLException {
        String sql = "SELECT uuid FROM account WHERE uuid = ?";
        PreparedStatement stmt = db.prepareStatement(sql);

        /*
         * The code below generates a random UUID and performs a query
         * for accounts with that UUID. If no accounts exist with that
         * UUID, the loop is broken and the UUID is returned. This is
         * done to ensure the off chance a duplicate UUID is generated
         * does not cause an issue when creating user accounts.
         */
        UUID generated;
        ResultSet query;
        do {
            generated = UUID.randomUUID();
            stmt.setString(1, generated.toString());
            query = stmt.executeQuery();
        } while (query.next());

        stmt.close();
        return generated;
    }

    /**
     * @param username the username to check for.
     * @return {@code true} if an account with the specified username exists,
     * {@code false} otherwise.
     * @throws SQLException if an SQL error occurs.
     */
    public boolean usernameExists(@NotNull String username) throws SQLException {
        Objects.requireNonNull(username, "username cannot be null");

        String sql = "SELECT username FROM account WHERE username = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        }
    }

    /**
     * @param username the username of the account whose UUID to get.
     * @return the UUID of the account with the specified username,
     * {@code null} if no such account exists.
     * @throws SQLException if an SQL error occurs.
     */
    public @Nullable UUID getUUID(@NotNull String username) throws SQLException {
        Objects.requireNonNull(username, "username cannot be null");

        String sql = "SELECT uuid FROM account WHERE username = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return null; /* no such account */
            }

            String uuidStr = query.getString(1);
            return UUID.fromString(uuidStr);
        }
    }

    /**
     * @param uuid the UUID of the account.
     * @return the account with the specified UUID, {@code null} if no such
     * account exists.
     * @throws NullPointerException if {@code uuid} is {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public @Nullable UnoAccount getAccount(@NotNull UUID uuid) throws SQLException {
        Objects.requireNonNull(uuid, "uuid cannot be null");

        String sql = "SELECT * FROM account WHERE uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return null; /* no such account */
            }

            String username = query.getString("username");
            return new DbUnoAccount(uuid, username);
        }
    }

    /**
     * @param username the username of the account.
     * @param password the password. For security reasons, this should be
     *                 discarded from memory as quickly as possible. This
     *                 can be done by assigning the password {@code String}
     *                 to a value of {@code null}.
     * @return the newly created account.
     * @throws SQLException if an SQL error occurs; if an account with
     *                      {@code username} already exists.
     */
    /* @formatter:off */
    public @NotNull UnoAccount
            createAccount(@NotNull String username,
                          @NotNull String password) throws SQLException {
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        if (this.usernameExists(username)) {
            throw new SQLException("account with username exists");
        }

        UUID uuid = this.findAvailableId();
        String salt = SaltGenerator.generateSalt(32);

        String sql = "INSERT INTO account VALUES(?, ?, MD5(?), ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, username);
            stmt.setString(3, password + salt);
            stmt.setString(4, salt);
            stmt.execute();
        }

        return new DbUnoAccount(uuid, username);
    }
    /* @formatter:on */

    /**
     * @param uuid     the UUID of the account.
     * @param password the password to verify. For security reasons,
     *                 this should be discarded from memory as quickly
     *                 as possible. This can be done by assigning the
     *                 password {@code String} to a value of {@code null}.
     * @return {@code true} if login was successful, {@code false} otherwise.
     * @throws NullPointerException if {@code uuid} or {@code password}
     *                              are {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    /* @formatter:off */
    public boolean
            verifyLogin(@NotNull UUID uuid,
                        @NotNull String password) throws SQLException {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        String passwordHash = null;
        String passwordSalt = null;

        String sql = "SELECT password_hash, password_salt";
        sql += " FROM account WHERE uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet query = stmt.executeQuery();
            if (query.next()) {
                passwordHash = query.getString(1);
                passwordSalt = query.getString(2);
            }
        }

        /*
         * If the password hash is null, that means no user with the given
         * UUID exists in the database. However, if the salt is null, that
         * means something has gone wrong in the database. If one is not
         * null, then neither should be null.
         */
        if (passwordHash == null) {
            return false; /* no such user */
        } else if (passwordSalt == null) {
            throw new SQLException("missing salt, this is a bug");
        }

        /*
         * Here we are using MySQL to compute the hash. This requires
         * another query, but it ensures that it is computed correctly.
         */
        String md5 = "SELECT MD5(?)";
        try (PreparedStatement stmt = db.prepareStatement(md5)) {
            stmt.setString(1, password + passwordSalt);

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                throw new SQLException("failure to compute MD5");
            }

            String computedHash = query.getString(1);
            return computedHash.equals(passwordHash);
        }
    }
    /* @formatter:on */

}
