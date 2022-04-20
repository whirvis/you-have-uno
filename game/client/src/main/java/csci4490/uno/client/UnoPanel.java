package csci4490.uno.client;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UnoPanel extends JPanel
{
	private String[] colors = new String[] {"blue", "yellow", "red", "green"};
	private int[] numbers = new int[] {1,2,3,4,5,6,7,8,9};
	private String[] special = new String[] {"Skip", "DrawTwo", "Reverse"};
	private String wildCard = "wildCard";
	private String wildDrawFour = "wildDrawFour";
	
	private JTextPane gameMoves;
	private JTextPane p1NameBox, p2NameBox, p3NameBox, playerNameBox;
	private JButton drawPile;

	private JTextPane cardPlace;
	private JLabel topCard;
	private JButton cardInHand;
	
	public UnoPanel() {
		Image backCard = null;
		
		
		
		setBackground(new Color(222, 184, 135));
		setForeground(new Color(135, 206, 250));
		setLayout(null);
		
		gameMoves = new JTextPane();
		gameMoves.setBounds(153, 10, 283, 46);
		add(gameMoves);
		
		p1NameBox = new JTextPane();
		p1NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p1NameBox.setText("Player 1");
		p1NameBox.setBackground(new Color(220, 20, 60));
		p1NameBox.setForeground(new Color(255, 255, 255));
		p1NameBox.setBounds(10, 98, 127, 24);
		add(p1NameBox);
		
		p2NameBox = new JTextPane();
		p2NameBox.setText("Player 2");
		p2NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p2NameBox.setForeground(new Color(255, 255, 255));
		p2NameBox.setBackground(new Color(30, 144, 255));
		p2NameBox.setBounds(445, 98, 127, 24);
		add(p2NameBox);
		
		p3NameBox = new JTextPane();
		p3NameBox.setText("Player 3");
		p3NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p3NameBox.setBackground(new Color(60, 179, 113));
		p3NameBox.setForeground(new Color(255, 255, 255));
		p3NameBox.setBounds(10, 240, 127, 24);
		add(p3NameBox);
		
		cardPlace = new JTextPane();
		cardPlace.setBackground(new Color(0, 0, 0));
		cardPlace.setBounds(219, 98, 150, 170);
		add(cardPlace);
		
		playerNameBox = new JTextPane();
		playerNameBox.setText("Main Player");
		playerNameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		playerNameBox.setForeground(new Color(255, 255, 255));
		playerNameBox.setBackground(new Color(0, 0, 0));
		playerNameBox.setBounds(445, 389, 127, 24);
		add(playerNameBox);
		
		drawPile = new JButton("");
		drawPile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Sup hoes");
			}
		});
		drawPile.setBounds(442, 172, 150, 170);
		backCard = imageResize("back");
		drawPile.setIcon(new ImageIcon(backCard));
		add(drawPile);
		
		topCard = new JLabel("");
		topCard.setBounds(219, 98, 143, 166);
		add(topCard);
		
		cardInHand = new JButton("New button");
		cardInHand.setBounds(27, 311, 88, 113);
		add(cardInHand);
	}
	
	public Image imageResize(String card) {
		Image temp = null;
		try {
			temp = ImageIO.read(new File("src/assets/" + card + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		temp = temp.getScaledInstance(150, 170, java.awt.Image.SCALE_SMOOTH);
		
		return temp;
	}
	
	public void drawCardToHand() {
		
	}
}