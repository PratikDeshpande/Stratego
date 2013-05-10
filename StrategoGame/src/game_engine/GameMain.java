package game_engine;

import game_renderer.StrategoGraphicsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ai.ExpectiminimaxAI;
import ai.PlayerExample;
import ai.ProbabilisticUnit;


public class GameMain {
	//public GameState gs;
	public static void main(String args[])
	{
		
			
		// Set the terrain of the game here. 0 is traversible, 1 is not.
		int[] terrain = 
			{   0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,1,1,0,0,1,1,0,0, // For testing we are removing the lakes in the middle
				0,0,1,1,0,0,1,1,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0 };
		
		
		Player p1 = new PlayerExample(1);
		Player p2 = new ExpectiminimaxAI(2);
		GameState gs = new GameState(p1, p2,terrain);
		//Player asdf = new PlayerExample();
		
		
		
		// Starts Frame and graphics panel
		JFrame mainFrame = new JFrame("Stratego, by Pratik Deshpande and Won Taek Chung");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(600,600));
		
		StrategoGraphicsPanel graphicsPanel = new StrategoGraphicsPanel(gs);
		
		
		
		mainFrame.add(graphicsPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
	
		
		int numTurns = 200;
		for(int t=0;t<numTurns;t++){
			if(!gs.gameOver)
			//	gs.update();
			//	graphicsPanel.repaint();
			//	System.out.println("Update");
				try {
					gs.update(1);
					graphicsPanel.repaint();
					Thread.sleep(1000);
					gs.update(2);
					graphicsPanel.repaint();


				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// Atter n number of turns, lets look at player 1's prob model
	//	System.out.println(((PlayerExample)p1).probabilityModel.keySet().size());
	/*	for(ProbabilisticUnit pu:((PlayerExample)p1).probabilityModel.values()){
			System.out.println("---------------");
			pu.printLikelihoods();
			System.out.println(pu.hasMoved());
			System.out.println(pu.isIdentified() ); 

		} */
	} 
		
		
		
	}


