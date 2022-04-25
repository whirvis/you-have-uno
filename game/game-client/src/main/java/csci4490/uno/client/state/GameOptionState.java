package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.GameOptionPanel;
import org.jetbrains.annotations.NotNull;

public class GameOptionState extends UnoGameState<GameOptionPanel> {

    public GameOptionState(@NotNull UnoGame game) {
        super(game, "Game Options", new GameOptionPanel());
    }

    @Override
    protected void initState() {
        panel.returnButton.addActionListener(event -> {
            game.enterState(game.homeStateId);
        });
    }

    @Override
    protected void update(long delta) {
        /* nothing to update */
    }
}
