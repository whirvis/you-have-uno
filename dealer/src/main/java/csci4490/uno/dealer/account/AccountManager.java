package csci4490.uno.dealer.account;

import csci4490.uno.dealer.account.DbUnoAccount;
import csci4490.uno.dealer.account.UnoAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
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

    /* @formatter:off */
    private static final char[] SALT_ALPHABET = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*',
            '(', ')', '~', '`', '+', '-', '=', '_', '{', '[',
            '}', ']', '|', '\\', ':', ';', '\"', '\'', '<',
            ',', '>', '.', '?', '/'
    };
    /* @formatter:on */

    @SuppressWarnings("SameParameterValue")
    private static @NotNull String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(SALT_ALPHABET.length);
            salt.append(SALT_ALPHABET[index]);
        }

        return salt.toString();
    }

    private final Connection db;

    /**
     * @param db the database connection.
     * @throws NullPointerException if {@code db} is {@code null}.
     */
    public AccountManager(@NotNull Connection db) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
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
     * @param uuid the UUID to check for.
     * @return {@code true} if an account with the specified UUID exists,
     * {@code false} otherwise.
     * @throws SQLException if an SQL error occurs.
     */
    public boolean uuidExists(@NotNull UUID uuid) throws SQLException {
        Objects.requireNonNull(uuid, "uuid cannot be null");

        String sql = "SELECT uuid FROM account WHERE uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            return stmt.executeQuery().next();
        }
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

        if (!this.usernameExists(username)) {
            throw new SQLException("account with username exists");
        }

        UUID uuid = this.findAvailableId();
        String salt = generateSalt(32);

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

    /**
     * This is a shorthand for {@link #verifyLogin(UUID, String)}. If no
     * account with the specified username exists, this method will simply
     * return {@code false} (rather than throwing an exception).
     *
     * @param username the username of the account.
     * @param password the password to verify. For security reasons,
     *                 this should be discarded from memory as quickly
     *                 as possible. This can be done by assigning the
     *                 password {@code String} to a value of {@code null}.
     * @return {@code true} if login was successful, {@code false} otherwise.
     * @throws NullPointerException if {@code username} or {@code password}
     *                              are {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean verifyLogin(@NotNull String username,
                               @NotNull String password) throws SQLException {
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(password, "password cannot be null");

        /*
         * This works by getting the UUID of an account by its username.
         * If the UUID is null, that means no such account exists. In this
         * situation, just return false for a failed login.
         */
        UUID uuid = this.getUUID(username);
        if (uuid == null) {
            return false; /* no such account */
        }
        return this.verifyLogin(uuid, password);
    }

}
