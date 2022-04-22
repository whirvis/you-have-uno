/*
 * Kal Young
 * Game Code Panel
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

public class GameCodePanel extends JPanel
{
	private JLabel gameCodeLabel;
	private JLabel numConnected;
	private JButton returnButton;
	private JButton startButton;
	private MainFrame frame;
	
	public GameCodePanel() {
		
		frame = new MainFrame();
		
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
		gameCodeLabel = new JLabel("Your Game Code is: 123456");
		gameCodeLabel.setBounds(83, 36, 279, 25);
		gameCodeLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		gameCodeLabel.setForeground(new Color(255, 255, 255));
		add(gameCodeLabel);
		
		numConnected = new JLabel("Number of Players Connected: 0");
		numConnected.setFont(new Font("Tahoma", Font.PLAIN, 15));
		numConnected.setForeground(new Color(255, 255, 255));
		numConnected.setBounds(110, 102, 229, 25);
		add(numConnected);
		
		returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToGOP(e);
				
			}
		});
		returnButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		returnButton.setBounds(55, 184, 120, 52);
		add(returnButton);
		
		startButton = new JButton("Start Game");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToUP(e);
				
			}
		});
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		startButton.setBounds(265, 184, 132, 52);
		add(startButton);
	}

}
