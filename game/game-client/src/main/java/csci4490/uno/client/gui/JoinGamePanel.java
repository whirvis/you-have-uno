/*
 * Kal Young
 * Join Game Panel
 */

package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class JoinGamePanel extends JPanel {

    public final JLabel codeEntryLabel;
    public final JLabel gameCodeLabel;
    public final JLabel errorLabel;

    public final JEditorPane gameCodeEntry;

    public final JButton returnButton;
    public final JButton connectButton;

    public JoinGamePanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.codeEntryLabel = new JLabel("Enter game code below:");
        codeEntryLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        codeEntryLabel.setForeground(Color.BLACK);
        codeEntryLabel.setBounds(103, 28, 252, 27);
        this.add(codeEntryLabel);

        this.gameCodeLabel = new JLabel("Game Code:");
        gameCodeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gameCodeLabel.setForeground(Color.BLACK);
        gameCodeLabel.setBounds(23, 114, 94, 27);
        this.add(gameCodeLabel);

        this.errorLabel = new JLabel();
        errorLabel.setForeground(Color.BLACK);
        errorLabel.setBounds(23, 164, 365, 27);
        this.add(errorLabel);

        this.returnButton = new JButton("Return");
        returnButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        returnButton.setBounds(23, 217, 133, 45);
        this.add(returnButton);

        this.connectButton = new JButton("Connect");
        connectButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        connectButton.setBounds(284, 217, 133, 45);
        this.add(connectButton);

        this.gameCodeEntry = new JEditorPane();
        gameCodeEntry.setBounds(157, 114, 260, 27);
        this.add(gameCodeEntry);
    }

}
