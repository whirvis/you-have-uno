package csci4490.uno.client.state;

import csci4490.uno.client.UnoGame;
import csci4490.uno.client.UnoGameState;
import csci4490.uno.client.gui.CreateAccountPanel;
import csci4490.uno.commons.scheduler.ScheduledJob;
import csci4490.uno.dealer.UnoDealerClient;
import csci4490.uno.dealer.response.account.AccountCreateResponse;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;

public class CreateAccountState extends UnoGameState<CreateAccountPanel> {

    private ScheduledJob createAccountJob;

    public CreateAccountState(@NotNull UnoGame game) {
        super(game, "Create Account", new CreateAccountPanel());
    }

    @Override
    protected void initState() {
        panel.returnButton.addActionListener(event -> {
            game.enterState(game.homeStateId);
        });
        panel.submitButton.addActionListener(event -> {
            if (createAccountJob != null && !createAccountJob.isFinished()) {
                createAccountJob.cancel();
            }
            this.createAccountJob =
                    this.getScheduler().schedule(this::createAccount);
        });
    }

    @Override
    protected void leaveState() {
        panel.usernamePane.setText("");
        panel.passwordField.setText("");
        panel.verifyField.setText("");
        panel.responseLabel.setText("");
    }

    private void createAccount() {
        JLabel responseLabel = panel.responseLabel;
        responseLabel.setText("Creating account...");
        try {
            UnoDealerClient dealerClient = game.getDealerClient();

            String username = panel.usernamePane.getText().trim();
            char[] cPassword = panel.passwordField.getPassword();
            char[] cVerify = panel.verifyField.getPassword();
            String password = new String(cPassword);

            if (!Arrays.equals(cPassword, cVerify)) {
                responseLabel.setText("Passwords must match.");
                return;
            }

            AccountCreateResponse accountCreateResponse =
                    dealerClient.createAccount(username, password);
            if (accountCreateResponse.account == null) {
                responseLabel.setText(accountCreateResponse.plainText);
            } else {
                responseLabel.setText("Created account!");
            }
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
