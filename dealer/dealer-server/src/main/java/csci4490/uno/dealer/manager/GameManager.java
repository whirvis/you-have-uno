package csci4490.uno.dealer.manager;

import csci4490.uno.dealer.StaticUnoGame;
import csci4490.uno.dealer.UnoGame;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class GameManager {

    private static final char[] GAME_CODE_CHARS = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9'
    };

    private static @NotNull String getGameCode() {
        Random random = new Random();
        char[] code = new char[6];
        for (int i = 0; i < code.length; i++) {
            int index = random.nextInt(GAME_CODE_CHARS.length);
            code[i] = GAME_CODE_CHARS[index];
        }
        return new String(code);
    }


    private final Connection db;
    private final WebGameManager webManager;

    private VisitManager visitManager;
    private TavernManager tavernManager;

    public GameManager(Connection db) {
        this.db = db;
        this.webManager = new WebGameManager(this);
    }

    public WebGameManager getWebManager() {
        return this.webManager;
    }

    private @NotNull String findAvailableGameCode() throws SQLException {
        String sql = "SELECT code FROM game WHERE code = ?";
        PreparedStatement stmt = db.prepareStatement(sql);

        String generated;
        ResultSet query;
        do {
            generated = getGameCode();
            stmt.setString(1, generated);
            query = stmt.executeQuery();
        } while (query.next());

        stmt.close();
        return generated;
    }

    private void requireVisitManager() {
        if (visitManager == null) {
            throw new IllegalStateException("visit manager not set");
        }
    }

    /**
     * Sets the visit manager for this login manager. This is required for
     * select operations, like creating game lobbies.
     *
     * @param manager the visit manager to use.
     * @throws NullPointerException  if {@code manager} is {@code null}.
     * @throws IllegalStateException if an account manager has already been
     *                               set for this login manager.
     */
    public void setVisitManager(@NotNull VisitManager manager) {
        Objects.requireNonNull(manager, "manager cannot be null");
        this.visitManager = manager;
    }

    private void requireTavernManager() {
        if (tavernManager == null) {
            throw new IllegalStateException("tavern manager not set");
        }
    }

    /**
     * Sets the visit manager for this tavern manager. This is required for
     * select operations, like creating game lobbies.
     *
     * @param manager the tavern manager to use.
     * @throws NullPointerException  if {@code manager} is {@code null}.
     * @throws IllegalStateException if an account manager has already been
     *                               set for this login manager.
     */
    public void setTavernManager(@NotNull TavernManager manager) {
        Objects.requireNonNull(manager, "manager cannot be null");
        this.tavernManager = manager;
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static PublicKey getPublicKey(byte[] key) {
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(key));
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull UnoGame createGame(@NotNull UUID uuid,
                                       @NotNull InetSocketAddress tavernAddress,
                                       @NotNull UUID lobbyId) throws SQLException {

        String code = this.findAvailableGameCode();

        KeyPair keyPair = generateKeyPair();

        String sql = "INSERT INTO game VALUES(?, ?, ?, INET6_ATON(?), ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, uuid.toString());
            stmt.setString(3, lobbyId.toString());
            stmt.setString(4, tavernAddress.getHostString());
            stmt.setInt(5, tavernAddress.getPort());

            Blob b = db.createBlob();
            b.setBytes(1, keyPair.getPublic().getEncoded());
            stmt.setBlob(6, b);

            stmt.execute();
        }

        return new StaticUnoGame(code, uuid, lobbyId, tavernAddress,
                keyPair.getPublic(), keyPair.getPrivate());
    }

    public UnoGame getGame(String code) throws SQLException {
        String sql = "SELECT host_uuid, lobby_id, INET6_NTOA(tavern_ip), " +
                "tavern_port, public_key FROM game WHERE code = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, code);

            ResultSet query = stmt.executeQuery();
            if (!query.next()) {
                return null;
            }

            UUID host = UUID.fromString(query.getString(1));
            UUID lobbyId = UUID.fromString(query.getString(2));

            String tavernHost = query.getString(3);
            int tavernPort = query.getInt(4);
            InetSocketAddress tavernAddress =
                    new InetSocketAddress(tavernHost, tavernPort);

            Blob b = query.getBlob(5);
            byte[] publicKeyData = b.getBytes(1, (int) b.length());
            PublicKey publicKey = getPublicKey(publicKeyData);

            return new StaticUnoGame(code, host, lobbyId, tavernAddress,
                    publicKey, null);
        }
    }

}
