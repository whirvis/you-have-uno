package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.LoginPanel;
import csci4490.uno.commons.scheduler.ScheduledJob;
import csci4490.uno.dealer.UnoAccount;
import csci4490.uno.dealer.UnoDealerClient;
import csci4490.uno.dealer.UnoLogin;
import csci4490.uno.dealer.response.account.AccountUUIDResponse;
import csci4490.uno.dealer.response.login.LoginResponse;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;

public class LoginState extends UnoGameState<LoginPanel> {

    private ScheduledJob loginJob;

    public LoginState(@NotNull UnoGame game) {
        super(game, "Login", new LoginPanel());
    }

    @Override
    protected void initState() {
        panel.loginButton.addActionListener(event -> {
            if (loginJob != null && !loginJob.isFinished()) {
                loginJob.cancel();
            }
            this.loginJob = this.getScheduler().schedule(this::login);
        });
        panel.returnButton.addActionListener(event -> {
            game.enterState(game.homeStateId);
        });
    }

    @Override
    protected void leaveState() {
        panel.usernamePane.setText("");
        panel.passwordField.setText("");
        panel.responseLabel.setText("");
    }

    private void login(ScheduledJob job) {
        JLabel responseLabel = panel.responseLabel;
        responseLabel.setText("Logging in...");
        try {
            UnoDealerClient dealerClient = game.getDealerClient();

            String username = panel.usernamePane.getText().trim();
            char[] cPassword = panel.passwordField.getPassword();
            String password = new String(cPassword);

            AccountUUIDResponse uuidResponse = dealerClient.getUUID(username);
            if (uuidResponse.uuid == null) {
                if (uuidResponse.status.getStatusCode() == HttpStatus.SC_OK) {
                    responseLabel.setText("No such user \"" + username + "\"");
                } else {
                    responseLabel.setText(uuidResponse.plainText);
                }
                return;
            }

            LoginResponse loginResponse =
                    dealerClient.loginAccount(uuidResponse.uuid, password);
            if (loginResponse.login == null) {
                responseLabel.setText(loginResponse.plainText);
                return;
            }

            UnoLogin login = loginResponse.login;

            responseLabel.setText("Saving login...");
            game.setLogin(login);

            UnoAccount account = login.getAccount();
            responseLabel.setText("Welcome, " + account.getUsername() + "!");
        } catch (ConnectException e) {
            responseLabel.setText("Failed to reach server.");
        } catch (IOException e) {
            responseLabel.setText("Server I/O error");
            e.printStackTrace();
        }
    }

    @Override
    protected void update(long delta) {
        /* nothing to update */
    }

}
