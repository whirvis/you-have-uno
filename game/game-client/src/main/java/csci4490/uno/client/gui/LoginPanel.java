package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public final JLabel loginLabel;
    public final JLabel usernameLabel;
    public final JLabel passwordLabel;
    public final JLabel responseLabel;
    
    public final JEditorPane usernamePane;
    public final JPasswordField passwordField;
    
    public final JButton loginButton;
    public final JButton returnButton;

    public LoginPanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.loginLabel = new JLabel("Enter username and password below:");
        loginLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        loginLabel.setForeground(Color.BLACK);
        loginLabel.setBounds(63, 30, 312, 19);
        this.add(loginLabel);

        this.usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        usernameLabel.setBounds(34, 105, 99, 13);
        this.add(usernameLabel);

        this.passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        passwordLabel.setBounds(34, 166, 99, 13);
        this.add(passwordLabel);

        this.responseLabel = new JLabel();
        responseLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        responseLabel.setBounds(34, 208, 342, 30);
        this.add(responseLabel);

        this.loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 10));
        loginButton.setBounds(282, 248, 132, 42);
        this.add(loginButton);

        this.returnButton = new JButton("Return");
        returnButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        returnButton.setBounds(34, 248, 132, 42);
        this.add(returnButton);

        this.usernamePane = new JEditorPane();
        usernamePane.setBounds(156, 105, 219, 19);
        this.add(usernamePane);

        this.passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setBounds(156, 160, 220, 19);
        this.add(passwordField);
    }
	
}
