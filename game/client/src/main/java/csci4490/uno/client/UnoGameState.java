package csci4490.uno.client;

import csci4490.uno.commons.scheduler.Scheduler;
import csci4490.uno.commons.scheduler.ThreadedScheduler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * A game state for the <i>You Have Uno!</i> game client.
 * <p>
 * The GUI of an UNO game state is made up of an existing {@link JPanel}.
 * This is by design, as the team uses a window builder to create the
 * basis for GUI. Adding complex, handwritten code to these panels is
 * known to confuse the window builder (leading to errors in rendering,
 * code generation, etc.)
 *
 * @param <P> the panel type.
 * @see UnoGame#registerState(UnoGameState)
 */
public abstract class UnoGameState<P extends JPanel> {

    protected final UnoGame game;
    protected final String title;
    protected final P panel;

    private Scheduler scheduler;

    /**
     * @param game  the game that this state belongs to.
     * @param title the title of the game state. This will be reflected
     *              in the window title when this game state is entered.
     * @param panel the panel of this game state. This should be the
     *              primary source of rendering for the game state.
     * @throws NullPointerException if {@code game}, {@code title}, or
     *                              {@code panel} are {@code null}.
     */
    public UnoGameState(@NotNull UnoGame game, @NotNull String title,
                        @NotNull P panel) {
        this.game = Objects.requireNonNull(game, "game cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.panel = Objects.requireNonNull(panel, "panel cannot be null");
    }

    /**
     * @return the game that this state belongs to.
     */
    public UnoGame getGame() {
        return this.game;
    }

    /**
     * The title of the game state will be reflected in the window title
     * when this game state is entered. If {@code "Settings"} is returned
     * by this method, {@code "You Have Uno! - Settings"} will become the
     * window title.
     *
     * @return the title of the game state.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * The scheduler returned by this method is guaranteed to be not be
     * {@code null} when this game state is active. <b>Note that</b> all
     * scheduled jobs are cancelled when this game state is left. This
     * is done to prevent code from running expectedly.
     *
     * @return the scheduler for this game state.
     */
    protected @NotNull Scheduler getScheduler() {
        return this.scheduler;
    }

    /* package-private for UnoGame */
    void init() throws Exception {
        this.initState();
    }

    /**
     * Called when this state is initialized by the game. This method may
     * throw any exception. However, it will likely result in a game crash.
     *
     * @throws Exception if an error occurs.
     */
    protected void initState() throws Exception {
        /* optional implement */
    }

    /* package-private for UnoGame */
    void enter() throws Exception {
        this.scheduler = new ThreadedScheduler();
        this.enterState();
    }

    /**
     * Called when this state is entered by the game. This method may throw
     * any exception. However, it will likely result in a game crash.
     *
     * @throws Exception if an error occurs.
     */
    protected void enterState() throws Exception {
        /* optional implement */
    }

    /* package-private for UnoGame */
    void leave() throws Exception {
        this.leaveState();
        scheduler.close();
    }

    /**
     * Called when this state is left by the game. This method may throw any
     * exception. However, it will likely result in a game crash.
     *
     * @throws Exception if an error occurs.
     */
    protected void leaveState() throws Exception {
        /* optional implement */
    }

    /**
     * Called when the game itself is updated. This method may throw an
     * exception. However, it will likely result in a game crash.
     *
     * @param delta the amount of time in milliseconds that has passed since
     *              the last time this method was called. All time dependent
     *              values <i>must</i> be multiplied by this!
     * @throws Exception if an error occurs.
     */
    protected abstract void update(long delta) throws Exception;

    /**
     * Called when the game itself is rendered.
     */
    protected void render() {
        /* optional implement */
    }

}
