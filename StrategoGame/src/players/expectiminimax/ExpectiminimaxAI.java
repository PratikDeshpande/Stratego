/**
 * This AI Agent uses the Expectiminimax tree to pick the best move
 */
package players.expectiminimax;

import java.util.ArrayList;
import java.util.HashMap;

import stratego_engine.GameState;
import stratego_engine.MapTile;
import stratego_engine.PieceAction;
import stratego_engine.Player;

/**
 * @author Pratik
 *
 */
public class ExpectiminimaxAI extends Player {

	private GameState gs;
	
	private int depth; // Depth of the expectiminimax tree
	
	private MapTile[] map;
	private ArrayList<Integer> opponentPieces;
	private ArrayList<Integer> myPieces;
	private HashMap<Integer,ProbabilisticPiece> probabilityModel;
	
	public ExpectiminimaxAI(int playerID, int depth) {
		super(playerID);
		this.depth=depth;
		this.probabilityModel = new HashMap<Integer,ProbabilisticPiece>();
	}

	/* (non-Javadoc)
	 * @see stratego_engine.Player#nextMove(stratego_engine.GameState)
	 */
	@Override
	public PieceAction nextMove(GameState gs) {
		
		this.gs = gs;
		
		this.map=gs.getMap();
		
		this.opponentPieces = gs.getOpponentPieces(this.playerID);
		this.myPieces = gs.getMyPieces(this.playerID);
		
		// Algorithm:
		// Based on your current probability model, guess the identities of all the enemy pieces
		// Create an expectiminimax node from the current state of the game.
		
		// Will need our own representation of the game state (What the AI believes the game state is.
		// Based on that representation, we have to guess the possible legal actions of the enemy pieces.
		// We also need to have a way to evaluate that representation of the game state
		// As actions are taken (as we go down the tree), we need to modify this representation to reflect those changes
		
		if(!initialized){
			initialized = true;
			
			// Fill the prob model. TODO: Change this to actual funciton, not filler
			for(Integer i:this.opponentPieces){
				ProbabilisticPiece p = new ProbabilisticPiece(i);
				// Testing: Rank is 7
				p.setMostLikelyRank(7);
			}
			
		}
		
		// Be sure to make a deep copy of the map before creating an expectiminimax node
		MapTile[] newMap = new MapTile[100];
		
		for(int i=0;i<newMap.length;i++){
			newMap[i] =this.map[i].getCopy();
		}
		
		// Retrive kill list as well
		
		// All objects that are available from game state
	/*	ArrayList<PieceAction> legalActions = gs.getLegalActions(this.playerID);
		ArrayList<Integer> opponentPieces = gs.getOpponentPieces(this.playerID);
		ArrayList<Integer> myPieces = gs.getMyPieces(this.playerID);

		HashMap<Integer,PieceAction> pastActions = gs.getPastActions();
		MapTile[] map = gs.getMap();
		*/
		
		
		return null;
	}

	/**
	 * @return the gs
	 */
	public GameState getGameState() {
		return gs;
	}

	/**
	 * @return the probabilityModel
	 */
	public HashMap<Integer,ProbabilisticPiece> getProbabilityModel() {
		return probabilityModel;
	}



}
