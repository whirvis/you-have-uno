/*
 * Kal Young
 * Score Board Panel
 */

package csci4490.uno.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreBoardPanel extends JPanel
{
	
	private JLabel winnerLabel;
	private JLabel firstPlaceLabel;
	private JLabel secondPlaceLabel;
	private JLabel thirdPlaceLabel;
	private JLabel fourthPlaceLabel;
	private JLabel finumCardsLabel;
	private JLabel firstNumCards;
	private JLabel secNumCards;
	private JLabel thirdNumCards;
	private JLabel fourthNumCards;
	private JButton playAgainButton;
	private JButton exitGameButton;
	
	public ScoreBoardPanel() {
		
		setBackground(new Color(0, 0, 0));
		setForeground(new Color(255, 255, 255));
		setLayout(null);
		
		winnerLabel = new JLabel("Player 1 Wins!");
		winnerLabel.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 18));
		winnerLabel.setForeground(new Color(255, 255, 255));
		winnerLabel.setBounds(178, 28, 137, 26);
		add(winnerLabel);
		
		firstPlaceLabel = new JLabel("1st Place: Player 1");
		firstPlaceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		firstPlaceLabel.setForeground(new Color(255, 255, 255));
		firstPlaceLabel.setBounds(36, 104, 159, 26);
		add(firstPlaceLabel);
		
		secondPlaceLabel = new JLabel("2nd Place: Player 2");
		secondPlaceLabel.setForeground(new Color(255, 255, 255));
		secondPlaceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		secondPlaceLabel.setBounds(36, 140, 159, 26);
		add(secondPlaceLabel);
		
		thirdPlaceLabel = new JLabel("3rd Place: Player 3");
		thirdPlaceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		thirdPlaceLabel.setForeground(new Color(255, 255, 255));
		thirdPlaceLabel.setBounds(36, 176, 148, 19);
		add(thirdPlaceLabel);
		
		fourthPlaceLabel = new JLabel("4th Place: Player 4");
		fourthPlaceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		fourthPlaceLabel.setForeground(new Color(255, 255, 255));
		fourthPlaceLabel.setBounds(36, 205, 147, 26);
		add(fourthPlaceLabel);
		
		finumCardsLabel = new JLabel("Cards Left");
		finumCardsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		finumCardsLabel.setForeground(new Color(255, 255, 255));
		finumCardsLabel.setBounds(349, 76, 82, 13);
		add(finumCardsLabel);
		
		firstNumCards = new JLabel("0");
		firstNumCards.setFont(new Font("Tahoma", Font.PLAIN, 14));
		firstNumCards.setForeground(new Color(255, 255, 255));
		firstNumCards.setBounds(377, 111, 45, 13);
		add(firstNumCards);
		
		secNumCards = new JLabel("1");
		secNumCards.setFont(new Font("Tahoma", Font.PLAIN, 14));
		secNumCards.setForeground(new Color(255, 255, 255));
		secNumCards.setBounds(377, 148, 45, 13);
		add(secNumCards);
		
		thirdNumCards = new JLabel("2");
		thirdNumCards.setForeground(new Color(255, 255, 255));
		thirdNumCards.setFont(new Font("Tahoma", Font.PLAIN, 14));
		thirdNumCards.setBounds(377, 181, 45, 13);
		add(thirdNumCards);
		
		fourthNumCards = new JLabel("3");
		fourthNumCards.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fourthNumCards.setForeground(new Color(255, 255, 255));
		fourthNumCards.setBounds(377, 214, 45, 13);
		add(fourthNumCards);
		
		playAgainButton = new JButton("Play Again");
		playAgainButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		playAgainButton.setBounds(70, 272, 99, 31);
		add(playAgainButton);
		
		exitGameButton = new JButton("Exit Game");
		exitGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
				
			}
		});
		exitGameButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exitGameButton.setBounds(314, 272, 108, 31);
		add(exitGameButton);
	}
}
