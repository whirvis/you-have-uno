/*
 * Kal Young
 * Create Account Panel
 */

package csci4490.uno.client.gui;

import csci4490.uno.client.UnoGame;

import javax.swing.*;
import java.awt.*;

public class CreateAccountPanel extends JPanel {

    public final JLabel usernameLabel;
    public final JLabel passwordLabel;
    public final JLabel verifyLabel;

    public final JEditorPane verifyPane;
    public final JEditorPane usernamePane;
    public final JEditorPane passwordPane;

    public final JLabel instructionLabel;
    public final JButton submitButton;
    public final JLabel errorLabel;
    public final JButton returnButton;

    public CreateAccountPanel() {
        this.setLayout(null);
        this.setBackground(UnoGame.BG_COLOR);

        this.usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        usernameLabel.setBounds(31, 91, 99, 13);
        this.add(usernameLabel);

        this.passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        passwordLabel.setBounds(31, 125, 99, 13);
        this.add(passwordLabel);

        this.verifyLabel = new JLabel("Verify password:");
        verifyLabel.setForeground(Color.BLACK);
        verifyLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        verifyLabel.setBounds(31, 162, 144, 13);
        this.add(verifyLabel);

        this.verifyPane = new JEditorPane();
        verifyPane.setBounds(213, 156, 183, 19);
        this.add(verifyPane);

        this.passwordPane = new JEditorPane();
        passwordPane.setBounds(213, 125, 183, 19);
        this.add(passwordPane);

        this.usernamePane = new JEditorPane();
        usernamePane.setBounds(213, 91, 183, 19);
        this.add(usernamePane);

        this.submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        submitButton.setBounds(290, 247, 106, 43);
        this.add(submitButton);

        this.errorLabel = new JLabel();
        errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        errorLabel.setBounds(31, 193, 365, 28);
        this.add(errorLabel);

        this.returnButton = new JButton("Return");
        returnButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        returnButton.setBounds(31, 247, 120, 43);
        this.add(returnButton);

        this.instructionLabel = new JLabel("Enter username and password");
        instructionLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        instructionLabel.setForeground(Color.BLACK);
        instructionLabel.setBounds(52, 25, 328, 28);
        this.add(instructionLabel);
    }

}
