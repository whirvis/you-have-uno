/*
 * Kal Young
 * Join Game Panel
 */

package csci4490.uno.client;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JoinGamePanel extends JPanel
{
	
	private JLabel codeEntryLabel;
	private JLabel gameCodeLabel;
	private JEditorPane gameCodeEntry;
	private JLabel errorLabel;
	private JButton returnButton;
	private JButton connectButton;
	
	public JoinGamePanel() {
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
	    codeEntryLabel = new JLabel("Enter the Game Code below:");
		codeEntryLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		codeEntryLabel.setForeground(new Color(255, 255, 255));
		codeEntryLabel.setBounds(103, 28, 252, 27);
		add(codeEntryLabel);
		
		gameCodeLabel = new JLabel("Game Code:");
		gameCodeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gameCodeLabel.setForeground(new Color(255, 255, 255));
		gameCodeLabel.setBounds(23, 114, 94, 27);
		add(gameCodeLabel);
		
		gameCodeEntry = new JEditorPane();
		gameCodeEntry.setBounds(157, 114, 260, 27);
		add(gameCodeEntry);
		
		errorLabel = new JLabel("");
		errorLabel.setForeground(new Color(255, 255, 255));
		errorLabel.setBounds(23, 164, 365, 27);
		add(errorLabel);
		
		returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		returnButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		returnButton.setBounds(23, 217, 133, 45);
		add(returnButton);
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		connectButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		connectButton.setBounds(284, 217, 133, 45);
		add(connectButton);
	}
}
