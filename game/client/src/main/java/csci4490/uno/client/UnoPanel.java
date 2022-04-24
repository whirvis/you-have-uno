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
import java.util.ArrayList;
import java.util.Random;
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
	private JLabel topCard;
	private JButton playPile;
	
	private ArrayList<JButton> hand = new ArrayList<JButton>();
	private JButton hand00, hand01, hand02, hand03, hand04, hand05, hand06, hand07, hand08,
		hand09, hand10, hand11, hand12, hand13, hand14, hand15;
	
	

	
	public UnoPanel() {
		Image backCard = null;
		
		
		
		setBackground(new Color(222, 184, 135));
		setForeground(new Color(135, 206, 250));
		setLayout(null);
		
		gameMoves = new JTextPane();
		gameMoves.setBounds(130, 16, 283, 46);
		add(gameMoves);
		
		p1NameBox = new JTextPane();
		p1NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p1NameBox.setText("Player 1");
		p1NameBox.setBackground(new Color(220, 20, 60));
		p1NameBox.setForeground(new Color(255, 255, 255));
		p1NameBox.setBounds(584, 32, 127, 24);
		add(p1NameBox);
		
		p2NameBox = new JTextPane();
		p2NameBox.setText("Player 2");
		p2NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p2NameBox.setForeground(new Color(255, 255, 255));
		p2NameBox.setBackground(new Color(30, 144, 255));
		p2NameBox.setBounds(584, 105, 127, 24);
		add(p2NameBox);
		
		p3NameBox = new JTextPane();
		p3NameBox.setText("Player 3");
		p3NameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		p3NameBox.setBackground(new Color(60, 179, 113));
		p3NameBox.setForeground(new Color(255, 255, 255));
		p3NameBox.setBounds(584, 186, 127, 24);
		add(p3NameBox);
		
		playerNameBox = new JTextPane();
		playerNameBox.setText("Main Player");
		playerNameBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		playerNameBox.setForeground(new Color(255, 255, 255));
		playerNameBox.setBackground(new Color(0, 0, 0));
		playerNameBox.setBounds(584, 318, 127, 24);
		add(playerNameBox);
		
		drawPile = new JButton("");
		drawPile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawCard();
			}
		});
		drawPile.setBounds(313, 98, 150, 170);
		backCard = imageResize(drawPile, "back");
		drawPile.setIcon(new ImageIcon(backCard));
		add(drawPile);
		
		topCard = new JLabel("");
		topCard.setBounds(219, 98, 143, 166);
		add(topCard);
		
		hand00 = new JButton("");
		hand00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand00);
				hand00.setIcon(null);
				updateHand(0);
			}
		});
		hand00.setBounds(487, 318, 64, 95);
		add(hand00);
		hand.add(hand00);
		
		hand01 = new JButton("");
		hand01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand01);
				hand01.setIcon(null);
				updateHand(1);
			}
		});
		hand01.setBounds(418, 318, 64, 95);
		add(hand01);
		hand.add(hand01);
		
		hand02 = new JButton("");
		hand02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand02);
				hand02.setIcon(null);
				updateHand(2);
			}
		});
		hand02.setBounds(349, 318, 64, 95);
		add(hand02);
		hand.add(hand02);
		
		hand03 = new JButton("");
		hand03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand03);
				hand03.setIcon(null);
				updateHand(3);
			}
		});
		hand03.setBounds(283, 318, 64, 95);
		add(hand03);
		hand.add(hand03);
		
		hand04 = new JButton("");
		hand04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand04);
				hand04.setIcon(null);
				updateHand(4);
			}
		});
		hand04.setBounds(214, 318, 64, 95);
		add(hand04);
		hand.add(hand04);
		
		hand05 = new JButton("");
		hand05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand05);
				hand05.setIcon(null);
				updateHand(5);
			}
		});
		hand05.setBounds(145, 318, 64, 95);
		add(hand05);
		hand.add(hand05);
		
		hand06 = new JButton("");
		hand06.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand06);
				hand06.setIcon(null);
				updateHand(6);
			}
		});
		hand06.setBounds(79, 318, 64, 95);
		add(hand06);
		hand.add(hand06);
		
		hand07 = new JButton("");
		hand00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand07);
				hand07.setIcon(null);
				updateHand(7);
			}
		});
		hand07.setEnabled(false);
		hand07.setVisible(false);
		hand07.setBounds(10, 318, 64, 95);
		add(hand07);
		hand.add(hand07);
		
		hand08 = new JButton("");
		hand08.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand08);
				hand08.setIcon(null);
				updateHand(8);
			}
		});
		hand08.setEnabled(false);
		hand08.setVisible(false);
		hand08.setBounds(487, 421, 64, 95);
		add(hand08);
		hand.add(hand08);
		
		
		hand09 = new JButton("");
		hand09.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand09);
				hand09.setIcon(null);
				updateHand(9);
			}
		});
		hand09.setEnabled(false);
		hand09.setVisible(false);
		hand09.setBounds(418, 421, 64, 95);
		add(hand09);
		hand.add(hand09);
		
		hand10 = new JButton("");
		hand10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand10);
				hand10.setIcon(null);
				updateHand(10);
			}
		});
		hand10.setEnabled(false);
		hand10.setVisible(false);
		hand10.setBounds(349, 421, 64, 95);
		add(hand10);
		hand.add(hand10);
		
		hand11 = new JButton("");
		hand11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand11);
				hand11.setIcon(null);
				updateHand(11);
			}
		});
		hand11.setEnabled(false);
		hand11.setVisible(false);
		hand11.setBounds(283, 421, 64, 95);
		add(hand11);
		hand.add(hand11);
		
		hand12 = new JButton("");
		hand12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand12);
				hand12.setIcon(null);
				updateHand(12);
			}
		});
		hand12.setEnabled(false);
		hand12.setVisible(false);
		hand12.setBounds(214, 421, 64, 95);
		add(hand12);
		hand.add(hand12);
		
		hand13 = new JButton("");
		hand13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand13);
				hand13.setIcon(null);
				updateHand(13);
			}
		});
		hand13.setEnabled(false);
		hand13.setVisible(false);
		hand13.setBounds(145, 421, 64, 95);
		add(hand13);
		hand.add(hand13);
		
		hand14 = new JButton("");
		hand14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand14);
				hand14.setIcon(null);
				updateHand(14);
			}
		});
		hand14.setEnabled(false);
		hand14.setVisible(false);
		hand14.setBounds(79, 421, 64, 95);
		add(hand14);
		hand.add(hand14);
		
		hand15 = new JButton("");
		hand15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				placeCard(hand15);
				hand15.setIcon(null);
				hand15.setVisible(false);
			}
		});
		hand15.setEnabled(false);
		hand15.setVisible(false);
		hand15.setBounds(10, 421, 64, 95);
		add(hand15);
		hand.add(hand15);
		
		playPile = new JButton("");
		playPile.setBounds(35, 98, 150, 170);
		add(playPile);


	} 
	
	public void updateHand(int index) {
		for(int i = 15; i > 0; i--) {
			if (hand.get(i).getIcon() != null) {
				hand.get(index).setIcon(hand.get(i).getIcon());
				hand.get(i).setIcon(null);
				hand.get(i).setVisible(false);
				i=-1; // exit
				
			}
		}
	}
	
	public void drawCard() {
		Random rn = new Random(System.currentTimeMillis());
		int upperBound1 = 4;
		int upperBound2 = 8;
		
		
		for(int i =0; i < 16; i++) {
			System.out.println("Pressed");
			if(hand.get(i).getIcon() == null) {
				String tempCard = colors[rn.nextInt(upperBound1)] + numbers[rn.nextInt(upperBound2)];
				Image tempImg = imageResize(hand.get(i), tempCard);
				hand.get(i).setIcon(new ImageIcon(tempImg));
				hand.get(i).setEnabled(true);
				hand.get(i).setVisible(true);
				System.out.println(i + " card inserted");
				i = 16;
			}

		}
	}
	
	public void initHand() {
		Random rn = new Random(System.currentTimeMillis());
		int upperBound1 = 4;
		int upperBound2 = 8;
		System.out.println("Init started");
		
		
		for(int i =6; i >= 0; i--) {
			String tempCard = colors[rn.nextInt(upperBound1)] + numbers[rn.nextInt(upperBound2)];
			Image tempImg = imageResize(hand.get(i), tempCard);
			hand.get(i).setIcon(new ImageIcon(tempImg));
			System.out.println(i + " card inserted");
		}
	}
	
	public void placeCard(JButton card) {
		playPile.setIcon(card.getIcon());
	}
	
	public Image imageResize(JButton cardButton, String card) {
		Image temp = null;
		try {
			temp = ImageIO.read(new File("src/assets/" + card + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		temp = temp.getScaledInstance(cardButton.getWidth(), cardButton.getHeight(), java.awt.Image.SCALE_SMOOTH);
		
		return temp;
	}
}
