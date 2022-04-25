package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.HomePanel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HomeState extends UnoGameState<HomePanel> {

    public HomeState(@NotNull UnoGame game) {
        super(game, "Home", new HomePanel());
    }

    @Override
    protected void initState() throws IOException {
        panel.loginButton.addActionListener((event -> {
            game.enterState(game.loginStateId);
        }));

        panel.createAccountButton.addActionListener(event -> {
            game.enterState(game.createAccountStateId);
        });
    }

    @Override
    protected void update(long delta) {
        /* nothing to update */
    }

}
