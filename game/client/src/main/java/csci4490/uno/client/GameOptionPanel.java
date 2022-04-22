/*
 * Kal Young
 * Game Option Panel
 */

package csci4490.uno.client;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameOptionPanel extends JPanel
{
	
	private JLabel gOptionLabel;
	private JButton joinGameButton;
	private JButton hostGameButton;
	private MainFrame frame;
	
	
	public GameOptionPanel() {
		
		frame = new MainFrame();
		
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
		gOptionLabel = new JLabel("Would you like to host or join a game?");
		gOptionLabel.setForeground(new Color(255, 255, 255));
		gOptionLabel.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 20));
		gOptionLabel.setBounds(27, 51, 397, 53);
		add(gOptionLabel);
		
		joinGameButton = new JButton("Join Game");
		joinGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToJGP(e);
				
			}
		});
		joinGameButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		joinGameButton.setBounds(50, 169, 128, 53);
		add(joinGameButton);
		
		hostGameButton = new JButton("Host Game");
		hostGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToGCP(e);
				
			}
		});
		hostGameButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		hostGameButton.setBounds(267, 169, 135, 53);
		add(hostGameButton);
	}
}
