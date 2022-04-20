/*
 * Kal Young
 * Home Page Frame
 */

package csci4490.uno.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class MainFrame 
{
	int width;
	int height;
	
	public MainFrame()
	{
		int width = 525;
		int height = 400;
	}
	
	public static void main(String[] args) 
	{
		
		JFrame frame = new JFrame("You Have Uno!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(525,400));
		
		// Panels
		
		CreateAccPanel caccpanel = new CreateAccPanel();
		frame.getContentPane().add(caccpanel);
		
		GameCodePanel gcpanel = new GameCodePanel();
		frame.getContentPane().add(gcpanel);
		
		GameOptionPanel gopanel = new GameOptionPanel();
		frame.getContentPane().add(gopanel);
		
	//	HomePanel hpanel = new HomePanel();
	//	frame.getContentPane().add(hpanel);
		
		JoinGamePanel jgpanel = new JoinGamePanel();
		frame.getContentPane().add(jgpanel);
		
		LoginPanel lgpanel = new LoginPanel();
		frame.getContentPane().add(lgpanel);
		
		HomePanel hpanel = new HomePanel();
		frame.getContentPane().add(hpanel);
		
		frame.pack();
		frame.setVisible(true);
		
	}

	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public Dimension setSize(int width, int height) {
		int oldWidth = this.width;
		int oldHeight = this.height;
		this.setWidth(width);
		this.setHeight(height);
		return new Dimension(oldWidth, oldHeight);
	}
	
	public Dimension getSize()
	{
		return new Dimension(getWidth(), getHeight());
	}
	
}