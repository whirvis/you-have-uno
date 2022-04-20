package csci4490.uno.client;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JEditorPane;

public class LoginPanel extends JPanel
{
	private JLabel usrnmInput;
	private JLabel pswdInput;
	private JButton returnHome;
	private JEditorPane editorPane;
	private JEditorPane editorPane_1;
	private JLabel inputError;
	private JButton loginButton;
	private JLabel loginLabel;
	
	public LoginPanel() {
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
		usrnmInput = new JLabel("Username:");
		usrnmInput.setForeground(Color.WHITE);
		usrnmInput.setFont(new Font("Tahoma", Font.BOLD, 16));
		usrnmInput.setBounds(34, 105, 99, 13);
		add(usrnmInput);
		
		pswdInput = new JLabel("Password:");
		pswdInput.setForeground(Color.WHITE);
		pswdInput.setFont(new Font("Tahoma", Font.BOLD, 16));
		pswdInput.setBounds(34, 166, 99, 13);
		add(pswdInput);
		
		returnHome = new JButton("Return");
		returnHome.setFont(new Font("Tahoma", Font.PLAIN, 12));
		returnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		returnHome.setBounds(34, 248, 132, 42);
		add(returnHome);
		
		editorPane = new JEditorPane();
		editorPane.setBounds(156, 105, 219, 19);
		add(editorPane);
		
	    editorPane_1 = new JEditorPane();
		editorPane_1.setBounds(156, 160, 220, 19);
		add(editorPane_1);
		
		inputError = new JLabel("  ");
		inputError.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputError.setBounds(34, 208, 342, 30);
		add(inputError);
		
		loginButton = new JButton("Login");
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		loginButton.setBounds(282, 248, 132, 42);
		add(loginButton);
		
		loginLabel = new JLabel("Enter username and password below:");
		loginLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		loginLabel.setForeground(Color.WHITE);
		loginLabel.setBounds(63, 30, 312, 19);
		add(loginLabel);
	}
}
