package game_engine;

import game_renderer.StrategoGraphicsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ai.PlayerExample;
import ai.ProbabilisticUnit;


public class GameMain {
	//public GameState gs;
	public static void main(String args[])
	{
		
				
		Player p1 = new PlayerExample(1);
		Player p2 = new PlayerExample(2);
		GameState gs = new GameState(p1, p2);
		//Player asdf = new PlayerExample();
		
		
		
		// Starts Frame and graphics panel
		JFrame mainFrame = new JFrame("Stratego, by Pratik Deshpande and Won Taek Chung");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(600,600));
		
		StrategoGraphicsPanel graphicsPanel = new StrategoGraphicsPanel(gs);
		
		
		
		mainFrame.add(graphicsPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
	
		
		int numTurns = 100;
		for(int t=0;t<numTurns;t++){
			if(!gs.gameOver)
			//	gs.update();
			//	graphicsPanel.repaint();
			//	System.out.println("Update");
				try {
					gs.update();
					graphicsPanel.repaint();
					Thread.sleep(1000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// Atter n number of turns, lets look at player 1's prob model
		for(ProbabilisticUnit pu:((PlayerExample)p1).probabilityModel.values()){
			System.out.println("---------------");
			pu.printLikelihoods();
			System.out.println(pu.hasMoved());
			System.out.println(pu.isIdentified() );

		}
	} 
		
		
		
	}


