package csci4490.uno.dealer.manager;

import csci4490.uno.commons.scheduler.JobRunnable;
import csci4490.uno.commons.scheduler.ScheduledJob;
import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.web.UnoVisit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class VisitManager {

    private static final long VISIT_TIMEOUT = 30000L;

    private final Connection db;
    private final WebVisitManager webManager;

    private LoginManager loginManager;
    private ScheduledJob removeVisitsJob;

    public VisitManager(@NotNull Connection db) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.webManager = new WebVisitManager(this);
    }

    public @NotNull WebVisitManager getWebManager() {
        return this.webManager;
    }

    private void requireLoginManager() {
        if (loginManager == null) {
            throw new IllegalStateException("");
        }
    }

    /**
     * Sets the login manager for this visit manager. This is required for
     * select operations, like beginning visit sessions.
     *
     * @param manager the login manager to use.
     * @throws NullPointerException  if {@code manager} is {@code null}.
     * @throws IllegalStateException if a login manager has already been
     *                               set for this visit manager.
     */
    public void setLoginManager(@NotNull LoginManager manager) {
        Objects.requireNonNull(manager, "manager cannot be null");
        this.loginManager = manager;
    }

    private void removeInactiveVisits() throws SQLException {
        String sql = "DELETE FROM visit WHERE";
        sql += " (((UNIX_TIMESTAMP(NOW(6)) * 1000) - keep_alive) > ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setLong(1, VISIT_TIMEOUT);
            stmt.execute();
        }
    }

    private void invalidateSession(@NotNull InetAddress address,
                                   @NotNull UUID uuid) throws SQLException {
        String sql = "DELETE FROM visit WHERE";
        sql += " ip_address = INET6_ATON(?) AND uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());
            stmt.execute();
        }
    }

    /**
     * Schedules a job which removes inactive visitors from the {@code visit}
     * table at the interval specified by {@link #VISIT_TIMEOUT}.
     *
     * @param scheduler the scheduler to run this job on.
     * @throws NullPointerException  if {@code scheduler} is {@code null}.
     * @throws IllegalStateException if the job has already been scheduled.
     */
    public void removeInactiveVisits(@NotNull Scheduler scheduler) {
        Objects.requireNonNull(scheduler, "scheduler cannot be null");
        if (removeVisitsJob != null) {
            throw new IllegalStateException("already scheduled job");
        }
        JobRunnable.NoParams job = this::removeInactiveVisits;
        this.removeVisitsJob = scheduler.scheduleForever(job,
                Duration.ZERO, Duration.ofMillis(VISIT_TIMEOUT));
    }

    /**
     * Begins a visit session for a user. This provides them a session token
     * they can use (instead of their access token) to perform actions. This
     * token can only be used from the IP address that requested it.
     * Furthermore, the user must send a keep alive request to at an interval
     * specified by {@link #VISIT_TIMEOUT} to keep their session valid.
     * <p>
     * <b>Note:</b> If a user begins a visit from the same IP address,
     * the previous session token they were assigned is invalidated.
     *
     * @param address     the IP address of the user.
     * @param uuid        the UUID of the user.
     * @param accessToken the provided access token.
     * @return the user visit session, {@code null} if an incorrect
     * {@code uuid} and {@code accessToken} combination was provided.
     * @throws NullPointerException  if {@code address}, {@code uuid}, or
     *                               {@code accessToken} are {@code null}.
     * @throws IllegalStateException if a login manager has not been
     *                               provided for this visit manager.
     * @throws SQLException          if an SQL error occurs.
     */
    /* @formatter:off */
    public @Nullable UnoVisit
            beginVisit(@NotNull InetAddress address, @NotNull UUID uuid,
                       @NotNull UUID accessToken) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(accessToken, "accessToken cannot be null");

        this.requireLoginManager();
        if (!loginManager.verifyAccess(address, uuid, accessToken)) {
            return null; /* invalid access token */
        }

        /*
         * For both security and storage reasons, a user cannot have
         * multiple session tokens on the same IP address. Invalidate
         * the previous access token (if any) for the user.
         */
        this.invalidateSession(address, uuid);

        long currentTime = System.currentTimeMillis();
        UUID sessionToken = UUID.randomUUID();

        String sql = "INSERT INTO visit VALUES(INET6_ATON(?), ?, ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());
            stmt.setString(3, sessionToken.toString());
            stmt.setLong(4, currentTime);
            stmt.execute();
        }

        return new UnoVisit(address, sessionToken, currentTime);
    }
    /* @formatter:on */

    /**
     * Gracefully ends the visit session for a user.
     *
     * @param address      the IP address of the user.
     * @param uuid         the UUID of the user.
     * @param sessionToken the provided session token.
     * @return {@code true} if the visit was ended, {@code false} if the
     * session token provided by the user was invalid.
     * @throws NullPointerException if {@code address}, {@code uuid}, or
     *                              {@code sessionToken} are {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean endVisit(@NotNull InetAddress address, @NotNull UUID uuid,
                            @NotNull UUID sessionToken) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(sessionToken, "sessionToken cannot be null");

        if (!this.verifySession(address, uuid, sessionToken)) {
            return false;
        }

        this.invalidateSession(address, uuid);
        return true;
    }

    /**
     * Verifies that a session token provided by a user from a certain IP
     * address is valid. This method should be used to authenticate users
     * before performing certain tasks, like joining a game.
     *
     * @param address      the IP address of the user.
     * @param uuid         the UUID of the user.
     * @param sessionToken the provided session token.
     * @return {@code true} if a valid session token was provided by the user
     * from their IP address, {@code false} otherwise.
     * @throws NullPointerException if {@code address}, {@code uuid}, or
     *                              {@code sessionToken} are {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean verifySession(@NotNull InetAddress address,
                                 @NotNull UUID uuid,
                                 @NotNull UUID sessionToken) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(sessionToken, "sessionToken cannot be null");

        long currentTime = System.currentTimeMillis();
        UUID storedSessionToken; /* initialized by query */

        String sql = "SELECT keep_alive, session_token FROM visit";
        sql += " WHERE INET6_ATON(ip_address) = ? AND uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostAddress());
            stmt.setString(2, uuid.toString());

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return false; /* no such session */
            }

            long keepAlive = query.getLong(1);
            if (currentTime - keepAlive > VISIT_TIMEOUT) {
                return false; /* session timeout */
            }

            String uuidStr = query.getString(2);
            storedSessionToken = UUID.fromString(uuidStr);
        }

        return storedSessionToken.equals(sessionToken);
    }

    /**
     * Updates the keep alive for an active visit session.
     *
     * @param address      the IP address of the user.
     * @param uuid         the UUID of the user.
     * @param sessionToken the provided session token.
     * @throws NullPointerException if {@code address}, {@code uuid}, or
     *                              {@code sessionToken} are {@code null}.
     * @throws SQLException         if an SQL error occurs; if the session
     *                              token provided by the user is invalid.
     */
    public void keepAlive(@NotNull InetAddress address, @NotNull UUID uuid,
                          @NotNull UUID sessionToken) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(uuid, "uuid cannot be null");
        Objects.requireNonNull(sessionToken, "sessionToken cannot be null");

        if (!this.verifySession(address, uuid, sessionToken)) {
            throw new SQLException("invalid session token");
        }

        long currentTime = System.currentTimeMillis();

        String sql = "UPDATE visit SET keep_alive = ? WHERE uuid = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setLong(1, currentTime);
            stmt.setString(2, uuid.toString());
            stmt.execute();
        }
    }

}
