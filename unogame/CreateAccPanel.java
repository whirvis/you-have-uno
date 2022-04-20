/*
 * Kal Young
 * Create Account Panel
 */

package unogame;

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
import javax.swing.JTextField;
import javax.swing.JEditorPane;

public class CreateAccPanel extends JPanel
{
	private JTextField caccLabel;
	private JLabel usrnmLabel;
	private JLabel pswdLabel;
	private JLabel verifyLabel;
	private JEditorPane verifyPane;
	private JEditorPane pswdPane;
	private JEditorPane usrnmPane;
	private JButton submitButton;
	private JLabel errorLabel;
	private JButton returnButton;
	
	public CreateAccPanel() {
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
		caccLabel = new JTextField();
		caccLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		caccLabel.setText("Enter a username and password below");
		caccLabel.setBounds(101, 10, 245, 19);
		add(caccLabel);
		caccLabel.setColumns(10);
		
		usrnmLabel = new JLabel("Username:");
		usrnmLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		usrnmLabel.setBounds(31, 91, 99, 13);
		add(usrnmLabel);
		
		pswdLabel = new JLabel("Password:");
		pswdLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		pswdLabel.setBounds(31, 125, 99, 13);
		add(pswdLabel);
		
		verifyLabel = new JLabel("Verify Password:");
		verifyLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		verifyLabel.setBounds(31, 162, 144, 13);
		add(verifyLabel);
		
		verifyPane = new JEditorPane();
		verifyPane.setBounds(213, 156, 183, 19);
		add(verifyPane);
		
		pswdPane = new JEditorPane();
		pswdPane.setBounds(213, 125, 183, 19);
		add(pswdPane);
		
		usrnmPane = new JEditorPane();
		usrnmPane.setBounds(213, 91, 183, 19);
		add(usrnmPane);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		submitButton.setBounds(290, 247, 106, 43);
		add(submitButton);
		
		errorLabel = new JLabel("  \r\n");
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorLabel.setBounds(31, 193, 365, 28);
		add(errorLabel);
		
		returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		returnButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		returnButton.setBounds(31, 247, 120, 43);
		add(returnButton);
	}

}
