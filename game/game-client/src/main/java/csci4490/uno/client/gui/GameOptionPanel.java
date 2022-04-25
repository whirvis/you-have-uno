/*
 * Kal Young
 * Game Option Panel
 */

package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class GameOptionPanel extends JPanel {

    public final JLabel optionLabel;

    public final JButton joinGameButton;
    public final JButton hostGameButton;
    public final JButton returnButton;

    public GameOptionPanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.optionLabel = new JLabel("Would you like to host or join a game?");
        optionLabel.setForeground(Color.BLACK);
        optionLabel.setFont(new Font("Comic Sans MS",
                Font.BOLD | Font.ITALIC, 20));
        optionLabel.setBounds(27, 51, 397, 53);
        this.add(optionLabel);

        this.joinGameButton = new JButton("Join Game");
        joinGameButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        joinGameButton.setBounds(50, 170, 135, 50);
        this.add(joinGameButton);

        this.hostGameButton = new JButton("Host Game");
        hostGameButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        hostGameButton.setBounds(267, 170, 135, 50);
        this.add(hostGameButton);


        this.returnButton = new JButton("Return");
        returnButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        returnButton.setBounds(50, 250, 135, 50);
        this.add(returnButton);
    }

}
