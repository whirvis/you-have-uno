/*
 * Kal Young
 * Home Page Frame
 */

package csci4490.uno.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
public class MainFrame 
{
	private static HomePanel hp;
	private static LoginPanel lp;
	private static CreateAccPanel ca;
	private static GameOptionPanel go;
	private static JoinGamePanel jg;
	private static GameCodePanel gc;
	private static UnoPanel up;
	
	// Main - Home Page
	
	public static void main(String[] args) 
	{
		
		JFrame homeframe = new JFrame("You Have Uno! - Home");
		homeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		homeframe.setPreferredSize(new Dimension(525,400));
		
		hp = new HomePanel();
		homeframe.getContentPane().add(hp);
		
		homeframe.pack();
		homeframe.setVisible(true);
		
	}
	
	// Home Page
	
	public void changeToHP(ActionEvent e) 
	{
		
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame homeframe = new JFrame("You Have Uno! - Home");
		homeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		homeframe.setPreferredSize(new Dimension(525,400));
		
		hp = new HomePanel();
		homeframe.getContentPane().add(hp);
		
		homeframe.pack();
		homeframe.setVisible(true);
		
	}
	
	// Login
	
	public void changeToLP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame loginframe = new JFrame("You Have Uno! - Login");
		loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginframe.setPreferredSize(new Dimension(525,400));
		
		lp = new LoginPanel();
		loginframe.getContentPane().add(lp);
		
		loginframe.pack();
		loginframe.setVisible(true);
		
	}
	
	// Create Account
	
	public void changeToCAP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame caccframe = new JFrame("You Have Uno! - Create Account");
		caccframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		caccframe.setPreferredSize(new Dimension(525,400));
		
		ca = new CreateAccPanel();
		caccframe.getContentPane().add(ca);
		
		caccframe.pack();
		caccframe.setVisible(true);
		
	}
	
	// User selects to host or join game
	
	public void changeToGOP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame gopframe = new JFrame("You Have Uno! - Game Option");
		gopframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gopframe.setPreferredSize(new Dimension(525,400));
		
		go = new GameOptionPanel();
		gopframe.getContentPane().add(go);
		
		gopframe.pack();
		gopframe.setVisible(true);
		
	}
	
	// Join Game
	
	public void changeToJGP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame jgpframe = new JFrame("You Have Uno! - Join Game");
		jgpframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jgpframe.setPreferredSize(new Dimension(525,400));
		
		jg = new JoinGamePanel();
		jgpframe.getContentPane().add(jg);
		
		jgpframe.pack();
		jgpframe.setVisible(true);
		
	}
	
	// Host will have generated game code
	
	public void changeToGCP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame gcpframe = new JFrame("You Have Uno! - Host Game");
		gcpframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gcpframe.setPreferredSize(new Dimension(525,400));
		
		gc = new GameCodePanel();
		gcpframe.getContentPane().add(gc);
		
		gcpframe.pack();
		gcpframe.setVisible(true);
		
	}
	
	// Uno Game
	
	public void changeToUP(ActionEvent e)
	{
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
		
		JFrame upframe = new JFrame("You Have Uno!");
		upframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		upframe.setPreferredSize(new Dimension(525,400));
		
		up = new UnoPanel();
		upframe.getContentPane().add(up);
		
		upframe.pack();
		upframe.setVisible(true);
		
	}
	
}