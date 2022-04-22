/*
 * Kal Young
 * Home Page Panel
 */

package unogame;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import unogame.LoginPanel;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class HomePanel extends JPanel
{
	
	
	private MainFrame frame;
	private Dimension oldSize;
	private JButton loginButton;
	private JButton accButton;
	private JLabel topLabel1;
	private JLabel lowLabel1;
	private JLabel loginCard1;
	private JLabel loginCard2;
	
	
	public HomePanel() 
	{
	
		frame = new MainFrame();
		
		setBackground(new Color(135, 206, 250));
		setLayout(null);
		
		// Login Button Action Listener
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToLP(e);
				
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		loginButton.setBounds(176, 199, 161, 55);
		add(loginButton);
		
		// Create New Account Action Listener
		
		accButton = new JButton("Create New Account");
		accButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.changeToCAP(e);
			}
		});
		accButton.setFont(new Font("Dialog", Font.PLAIN, 13));
		accButton.setBounds(176, 264, 161, 48);
		add(accButton);
		
		// Home Page Objects
		
		topLabel1 = new JLabel("You Have");
		topLabel1.setForeground(new Color(255, 255, 255));
		topLabel1.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 35));
		topLabel1.setBounds(176, 43, 181, 48);
		add(topLabel1);
		
		lowLabel1 = new JLabel("Uno!");
		lowLabel1.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 52));
		lowLabel1.setForeground(new Color(255, 255, 255));
		lowLabel1.setBounds(201, 110, 108, 55);
		add(lowLabel1);
		
		// Card Images
		
		loginCard1 = new JLabel("");
		loginCard1.setIcon(new ImageIcon("C:\\xkili\\Software Eng\\Group-Project\\UnoGame\\ClientGUI\\2022_card1ccexpress.jpg"));
		loginCard1.setBounds(31, 156, 101, 147);
		add(loginCard1);
		
		loginCard2 = new JLabel("New label");
		loginCard2.setBounds(370, 165, 101, 147);
		add(loginCard2);
		
	}

	
	
	
}
