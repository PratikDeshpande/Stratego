package game_engine;

import game_renderer.StrategoGraphicsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameMain {
	public static void main(String args[])
	{
		
				
		Player p1 = new PlayerExample(1);
		Player p2 = new PlayerExample2(2);
		GameState gs = new GameState(p1, p2);
		//Player asdf = new PlayerExample();
		System.out.println("Testing");
		
		
		// Starts Frame and graphics panel
		JFrame mainFrame = new JFrame("Stratego, by Pratik Deshpande and Won Taek Chung");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(600,600));
		
		StrategoGraphicsPanel graphicsPanel = new StrategoGraphicsPanel(gs);
		
		mainFrame.add(graphicsPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
		
	}

}
