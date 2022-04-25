package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.CreateAccountPanel;
import org.jetbrains.annotations.NotNull;

public class CreateAccountState extends UnoGameState<CreateAccountPanel> {

    public CreateAccountState(@NotNull UnoGame game) {
        super(game, "Create Account", new CreateAccountPanel());
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
        panel.verifyPane.setText("");
    }

    @Override
    protected void update(long delta) {
        /* nothing to update */
    }

}
