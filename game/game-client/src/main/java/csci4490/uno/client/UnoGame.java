package csci4490.uno.client;

import csci4490.uno.client.state.CreateAccountState;
import csci4490.uno.client.state.HomeState;
import csci4490.uno.client.state.LoginState;
import csci4490.uno.dealer.UnoDealerClient;
import csci4490.uno.dealer.UnoEndpoints;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UnoGame extends Thread {

    private static @NotNull UnoDealerClient createDealerClient() {
        InetSocketAddress official = UnoEndpoints.OFFICIAL;

        String host = official.getAddress().getHostAddress();
        int port = official.getPort();

        String addressProperty = System.getProperty("dealer_address");
        if (addressProperty != null) {
            String[] split = addressProperty.split(":");
            host = split[0];
            if (split.length > 1) {
                port = Integer.parseInt(split[1]);
            }
        }

        InetSocketAddress address = new InetSocketAddress(host, port);
        return new UnoDealerClient(address);
    }

    public static final String GAME_TITLE = "You Have Uno!";
    public static final Color BG_COLOR = new Color(242, 0, 0);

    public final @NotNull StateId homeStateId;
    public final @NotNull StateId loginStateId;
    public final @NotNull StateId createAccountStateId;

    private final UnoDealerClient dealerClient;
    private final JFrame frame;
    private final Map<StateId, UnoGameState<?>> states;

    private UnoGameState<?> currentState;
    private long lastUpdate;

    private UnoGame() {
        this.dealerClient = createDealerClient();
        this.frame = new JFrame(GAME_TITLE);

        frame.setBackground(BG_COLOR);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(525, 400));
        frame.setResizable(false);

        this.states = new HashMap<>();

        this.homeStateId = this.registerState(new HomeState(this));
        this.loginStateId = this.registerState(new LoginState(this));
        this.createAccountStateId =
                this.registerState(new CreateAccountState(this));
    }

    /**
     * @return the UNO dealer client used by this game to contact the UNO
     * dealer server.
     */
    public @NotNull UnoDealerClient getDealerClient() {
        return this.dealerClient;
    }

    private void handleGameError(Throwable cause) {
        /*
         * Since a game error has occurred, we must interrupt the thread
         * (as that will stop program execution). This will result in the
         * window closing, which will also help catch the user's attention.
         */
        this.interrupt();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream outStream = new PrintStream(out);
        cause.printStackTrace(outStream);
        outStream.close();

        Charset charset = Charset.defaultCharset();
        String causeStr = out.toString(charset);

        /*
         * After converting the stack trace into a string, show it in a
         * message dialog for the user. This ensures that they will see
         * it, as opposed to the gibberish being printed to the console.
         */
        JOptionPane.showMessageDialog(frame, causeStr, "Game error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Registers a state and initializes it. Once a state has been
     * registered, it cannot be unregistered or registered again.
     *
     * @param state the state to register.
     * @return the ID of the state.
     * @throws NullPointerException  if {@code state} is {@code null}.
     * @throws IllegalStateException if {@code state} is already registered.
     * @see #enterState(StateId)
     */
    public @NotNull StateId registerState(@NotNull UnoGameState<?> state) {
        Objects.requireNonNull(state, "state cannot be null");
        if (states.containsValue(state)) {
            throw new IllegalStateException("state already registered");
        }

        try {
            state.init();
        } catch (Throwable cause) {
            this.handleGameError(cause);
        }

        StateId id = new StateId();
        states.put(id, state);
        return id;
    }

    /**
     * Enters a game state with the specified ID. Before entering a game
     * state, it must be registered to this UNO game.
     *
     * @param id the ID of the game state.
     * @throws NullPointerException     if {@code id} is {@code null}.
     * @throws IllegalArgumentException if no such game state with the
     *                                  specified ID has been registered.
     * @see #registerState(UnoGameState)
     */
    public void enterState(@NotNull StateId id) {
        Objects.requireNonNull(id, "id cannot be null");
        if (!states.containsKey(id)) {
            throw new IllegalArgumentException("no such state with id");
        }

        if (currentState != null) {
            try {
                currentState.leave();
            } catch (Throwable cause) {
                this.handleGameError(cause);
            }

            Component component = currentState.panel;
            frame.getContentPane().remove(component);

            /*
             * After removing the current state's panel from the content
             * pane, the frame must be revalidated and repainted. Failure
             * to do so will result in artifacts from the previous state
             * remaining until they are drawn over.
             */
            frame.revalidate();
            frame.repaint();
        }

        UnoGameState<?> state = states.get(id);

        /*
         * When a new state is entered, the title of the frame must be
         * updated, the new contents must be updated, and the frame must
         * be packed. The title is more or less for show, and packing
         * the frame ensures it is the correct size.
         */
        frame.setTitle(GAME_TITLE + " - " + state.getTitle());
        frame.getContentPane().add(state.panel);
        frame.pack(); /* update frame size */

        try {
            state.enter();
            this.currentState = state;
        } catch (Throwable cause) {
            this.handleGameError(cause);
        }
    }

    private void update() throws Exception {
        long currentTime = System.currentTimeMillis();

        /*
         * The delta is used to determine how much time dependent values
         * should be updated between each frame. This guarantees that the
         * game will run at the same speed internally across all machines.
         */
        long delta = currentTime - lastUpdate;
        if (delta >= currentTime) {
            delta = 1L;
        }

        if (currentState != null) {
            currentState.update(delta);
        }

        this.lastUpdate = currentTime;
    }

    private void render() {
        if (currentState != null) {
            currentState.render();
        }
    }

    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        frame.setVisible(true);

        while (!this.isInterrupted()) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                this.interrupt();
            }

            try {
                this.update();
                this.render();
            } catch (Throwable cause) {
                this.handleGameError(cause);
            }
        }

        frame.dispose();
    }

    public static void main(String[] args) {
        UnoGame game = new UnoGame();
        game.enterState(game.homeStateId);
        game.start();
    }

}
