/**
 * 
 */
package stratego_engine;

import game_renderer.StrategoGraphicsPanel;

import java.awt.Dimension;

import javax.swing.JFrame;

import players.expectiminimax.ExpectiminimaxAI;
import players.human.HumanPlayer;
import players.random.RandomAI;


/**
 * @author Pratik
 *
 */
public class EngineMain {

	
	public static void main(String[] args){
		
		// Some of the parameters of the game have to be set manually
		
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
		
	//	Player p1 = new HumanPlayer(1);
	//	Player p1 = new RandomAI(1);
		Player p1 = new ExpectiminimaxAI(1,2); // W.O Pruning, you get out of memory at depth of 5
		
	//	Player p2 = new HumanPlayer(2);
		Player p2 = new RandomAI(2);
	//	Player p2 = new ExpectiminimaxAI(1,1);
		
		GameState gs = new GameState(p1, p2,terrain);
		
		
		// Initialize the graphics window here.
		// 
		// Starts Frame and graphics panel
		JFrame mainFrame = new JFrame("Stratego, by Pratik Deshpande");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(600,600));
		
		StrategoGraphicsPanel graphicsPanel = new StrategoGraphicsPanel(gs);
		
		
		
		mainFrame.add(graphicsPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
		
		int numTurns = 100;
		int currentTurn = 1;
		while((!gs.isGameOver())/*&&(currentTurn<=numTurns)*/){
			
			
			// if p1 has no legal moves, it loses
			
			PieceAction p1Action = p1.nextMove(gs);
			System.out.println(p1Action);
			gs.execute(p1Action);
			
			graphicsPanel.repaint();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			PieceAction p2Action = p2.nextMove(gs);
			System.out.println(p2Action);
			gs.execute(p2Action);
			
			graphicsPanel.repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			currentTurn++;

		}
		
	/*	for(PieceAction pa:gs.getLegalActions(1)){
			System.out.println(pa);
		} */
		
	}

}
