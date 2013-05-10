/**
 * 
 */
package players.random;

import java.util.ArrayList;
import java.util.Random;

import stratego_engine.GameState;
import stratego_engine.PieceAction;
import stratego_engine.Player;

/**
 * @author Pratik
 *
 */
public class RandomAI extends Player {
	private Random rgen;
	
	public RandomAI(int playerID){
		super(playerID);
	}
	
	/* (non-Javadoc)
	 * @see stratego_engine.Player#nextMove(stratego_engine.GameState)
	 */
	@Override
	public PieceAction nextMove(GameState gs) {
		
		ArrayList<PieceAction> legalActions = gs.getLegalActions(this.playerID);
		
		if(!initialized){
			
			rgen = new Random();
			
			this.initialized=true;
		}
		
		
		PieceAction selectedAction = legalActions.get(rgen.nextInt(legalActions.size()));
		
		return selectedAction;
	}

}
