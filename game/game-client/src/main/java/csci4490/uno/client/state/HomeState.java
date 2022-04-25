package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.HomePanel;
import csci4490.uno.dealer.UnoAccount;
import csci4490.uno.dealer.UnoLogin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HomeState extends UnoGameState<HomePanel> {

    public HomeState(@NotNull UnoGame game) {
        super(game, "Home", new HomePanel());
    }

    @Override
    protected void initState() {
        panel.loginButton.addActionListener((event -> {
            game.enterState(game.loginStateId);
        }));

        panel.createAccountButton.addActionListener(event -> {
            game.enterState(game.createAccountStateId);
        });

        panel.playButton.addActionListener(event -> {
            game.enterState(game.gameOptionsId);
        });
    }

    @Override
    protected void update(long delta) {
        UnoLogin login = game.getLogin();
        if(game.isVerifyingLogin()) {
            panel.currentAccountLabel.setText("Logging in...");
        } else if(login == null) {
            panel.currentAccountLabel.setText("Not logged in");
        } else {
            UnoAccount account = login.getAccount();
            String text = "Logged in as: " + account.getUsername();
            panel.currentAccountLabel.setText(text);
        }
    }

}
