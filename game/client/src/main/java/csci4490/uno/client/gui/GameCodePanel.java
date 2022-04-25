/*
 * Kal Young
 * Game Code Panel
 */

package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class GameCodePanel extends JPanel {

    public final JLabel gameCodeLabel;
    public final JLabel numConnected;

    public final JButton returnButton;
    public final JButton startButton;

    public GameCodePanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.gameCodeLabel = new JLabel();
        gameCodeLabel.setBounds(83, 36, 279, 25);
        gameCodeLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        gameCodeLabel.setForeground(Color.BLACK);
        this.add(gameCodeLabel);

        this.numConnected = new JLabel();
        numConnected.setFont(new Font("Tahoma", Font.PLAIN, 15));
        numConnected.setForeground(Color.BLACK);
        numConnected.setBounds(110, 102, 229, 25);
        this.add(numConnected);

        this.returnButton = new JButton("Return");
        returnButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        returnButton.setBounds(55, 184, 120, 52);
        this.add(returnButton);

        this.startButton = new JButton("Start Game");
        startButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        startButton.setBounds(265, 184, 132, 52);
        this.add(startButton);
    }

}
