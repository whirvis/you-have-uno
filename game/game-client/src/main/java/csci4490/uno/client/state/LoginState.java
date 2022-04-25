package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.LoginPanel;
import org.jetbrains.annotations.NotNull;

public class LoginState extends UnoGameState<LoginPanel> {

    public LoginState(@NotNull UnoGame game) {
        super(game, "Login", new LoginPanel());
    }

    @Override
    protected void initState() {
        panel.returnButton.addActionListener(event -> {
            game.enterState(game.homeStateId);
        });
    }

    @Override
    protected void leaveState() {
        panel.usernamePane.setText("");
        panel.passwordPane.setText("");
    }

    @Override
    protected void update(long delta) {
        /* nothing to update */
    }

}
