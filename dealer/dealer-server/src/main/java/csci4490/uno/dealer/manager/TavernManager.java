package csci4490.uno.dealer.manager;

import csci4490.uno.commons.scheduler.JobRunnable;
import csci4490.uno.commons.scheduler.ScheduledJob;
import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.dealer.StaticUnoTavern;
import csci4490.uno.dealer.UnoTavern;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TavernManager {

    private static final long TAVERN_TIMEOUT = 30000L;

    private final Connection db;
    private final WebTavernManager webManager;

    private ScheduledJob removeTavernsJob;

    /**
     * @param db the database connection.
     * @throws NullPointerException if {@code db} is {@code null}.
     * @see #getWebManager()
     */
    public TavernManager(Connection db) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.webManager = new WebTavernManager(this);
    }

    public @NotNull WebTavernManager getWebManager() {
        return this.webManager;
    }

    private void removeInactiveTaverns() throws SQLException {
        String sql = "DELETE FROM tavern WHERE";
        sql += " (((UNIX_TIMESTAMP(NOW(6)) * 1000) - keep_alive) > ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setLong(1, TAVERN_TIMEOUT);
            stmt.execute();
        }
    }

    /**
     * Schedules a job which removes inactive taverns from the {@code tavern}
     * table at the interval specified by {@link #TAVERN_TIMEOUT}.
     *
     * @param scheduler the scheduler to run this job on.
     * @throws NullPointerException  if {@code scheduler} is {@code null}.
     * @throws IllegalStateException if the job has already been scheduled.
     */
    public void removeInactiveVisits(@NotNull Scheduler scheduler) {
        Objects.requireNonNull(scheduler, "scheduler cannot be null");
        if (removeTavernsJob != null) {
            throw new IllegalStateException("already scheduled job");
        }
        JobRunnable.NoParams job = this::removeInactiveTaverns;
        this.removeTavernsJob = scheduler.scheduleForever(job, Duration.ZERO,
                Duration.ofMillis(TAVERN_TIMEOUT));
    }

    /**
     * @param address the address of the tavern.
     * @return {@code true} if a tavern with the specified address is
     * registered, {@code false} otherwise.
     * @throws NullPointerException if {@code address} is {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean isTavernRegistered(@NotNull InetSocketAddress address) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");

        long currentTime = System.currentTimeMillis();
        long keepAlive; /* initialized by query */

        String sql = "SELECT keep_alive FROM tavern WHERE";
        sql += " ip_address = INET6_ATON(?) AND port = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostString());
            stmt.setInt(2, address.getPort());

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return false; /* no such tavern */
            }

            keepAlive = query.getLong(1);
        }

        /*
         * If a tavern was located, it must be ensured that it has not yet
         * timed out. If it has timed out, run an early call to the method
         * which removes all inactive taverns. This ensures a method which
         * is checking if a tavern exists before adding it to the database
         * does not error because it ran before the scheduled job.
         */
        if (currentTime - keepAlive < TAVERN_TIMEOUT) {
            return true;
        } else {
            this.removeInactiveTaverns();
            return false;
        }
    }

    /**
     * Registers an UNO tavern. This will make the tavern visible to dealer
     * clients needing to create a lobby. Furthermore, The tavern must send
     * a keep alive to the dealer server request at an interval specified by
     * {@link #TAVERN_TIMEOUT} in order to remain visible.
     *
     * @param address the address of the UNO tavern.
     * @return the UNO tavern descriptor.
     * @throws NullPointerException if {@code address} is {@code null}.
     * @throws SQLException         if an SQL error occurs; if a tavern with
     *                              the given address is already registered.
     */
    /* @formatter:off */
    public @NotNull StaticUnoTavern
            registerTavern(@NotNull InetSocketAddress address)
            throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");

        long currentTime = System.currentTimeMillis();

        if(this.isTavernRegistered(address)) {
            throw new SQLException("tavern already registered");
        }

        String sql = "INSERT INTO tavern VALUES(INET6_ATON(?), ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostString());
            stmt.setInt(2, address.getPort());
            stmt.setLong(3, currentTime);
            stmt.execute();
        }

        return new StaticUnoTavern(address, currentTime);
    }
    /* @formatter:on */

    /**
     * Gracefully unregisters a currently registered tavern.
     *
     * @param address the address of the tavern.
     * @return {@code true} if a tavern with the specified address was
     * unregistered, {@code false} otherwise.
     * @throws NullPointerException if {@code address} is {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public boolean unregisterTavern(@NotNull InetSocketAddress address) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");

        if (!this.isTavernRegistered(address)) {
            return false;
        }

        String sql = "DELETE FROM tavern WHERE";
        sql += " ip_address = INET6_ATON(?) AND port = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address.getHostString());
            stmt.setInt(2, address.getPort());
            stmt.execute();
        }
        return true;
    }

    /**
     * Updates the keep alive for a registered tavern.
     *
     * @param address the IP address of the tavern.
     * @throws NullPointerException if {@code address} is {@code null}.
     * @throws SQLException         if an SQL error occurs.
     */
    public void keepAlive(@NotNull InetSocketAddress address) throws SQLException {
        Objects.requireNonNull(address, "address cannot be null");

        long currentTime = System.currentTimeMillis();

        String sql = "UPDATE tavern SET keep_alive = ? WHERE";
        sql += " ip_address = INET6_ATON(?) AND port = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setLong(1, currentTime);
            stmt.setString(2, address.getHostString());
            stmt.setInt(3, address.getPort());
            stmt.execute();
        }
    }

    /**
     * @param max the maximum amount of taverns to return.
     * @return the currently registered taverns.
     * @throws SQLException if an SQL error occurs.
     */
    public @NotNull List<UnoTavern> getTaverns(int max) throws SQLException {
        List<UnoTavern> taverns = new ArrayList<>();
        if (max <= 0) {
            return taverns;
        }

        this.removeInactiveTaverns();

        String sql = "SELECT INET6_NTOA(ip_address), port, keep_alive";
        sql += " FROM tavern";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            ResultSet query = stmt.executeQuery();
            while (query.next() && taverns.size() < max) {
                String host = query.getString(1);
                int port = query.getInt(2);
                long lastKeepAlive = query.getLong(3);

                InetSocketAddress address = new InetSocketAddress(host, port);
                taverns.add(new StaticUnoTavern(address, lastKeepAlive));
            }
        }

        return taverns;
    }

}
