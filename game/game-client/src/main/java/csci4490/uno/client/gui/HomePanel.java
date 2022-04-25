/*
 * Kal Young
 * Home Page Panel
 */

package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public static final Color SAFETY_YELLOW = new Color(236, 212, 7);

    public final JLabel upperTitleLabel;
    public final JLabel lowerTitleLabel;
    public final JLabel currentAccountLabel;

    public final JButton loginButton;
    public final JButton createAccountButton;
    public final JButton playButton;

    public HomePanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.upperTitleLabel = new JLabel("You Have");
        upperTitleLabel.setForeground(Color.BLACK);
        upperTitleLabel.setFont(new Font("Comic Sans MS",
                Font.BOLD | Font.ITALIC, 35));
        upperTitleLabel.setBounds(176, 43, 181, 48);
        this.add(upperTitleLabel);

        this.lowerTitleLabel = new JLabel("Uno!");
        lowerTitleLabel.setFont(new Font("Comic Sans MS",
                Font.BOLD | Font.ITALIC, 52));
        lowerTitleLabel.setForeground(SAFETY_YELLOW);
        lowerTitleLabel.setBounds(201, 110, 108, 55);
        this.add(lowerTitleLabel);

        this.currentAccountLabel = new JLabel();
        currentAccountLabel.setFont(new Font("Tahoma", Font.ITALIC, 16));
        currentAccountLabel.setForeground(Color.BLACK);
        currentAccountLabel.setBounds(15, 0, 10008, 55);
        this.add(currentAccountLabel);

        this.loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
        loginButton.setBounds(176, 199, 161, 55);
        this.add(loginButton);

        this.createAccountButton = new JButton("Create New Account");
        createAccountButton.setFont(new Font("Dialog", Font.PLAIN, 13));
        createAccountButton.setBounds(176, 264, 161, 48);
        this.add(createAccountButton);

        this.playButton = new JButton("Play");
        playButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        playButton.setBounds(350, 230, 100, 48);
        this.add(playButton);
    }

}
