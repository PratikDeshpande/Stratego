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
	
	// for statistics
	public static int nodesExpanded;
	
	private int depth; // Depth of the expectiminimax tree
	
	private MapTile[] map;
	private ArrayList<Integer> opponentPieces;
	private ArrayList<Integer> myPieces;
	private HashMap<Integer,ProbabilisticPiece> probabilityModel;
	
	public ExpectiminimaxAI(int playerID, int depth) {
		super(playerID);
		this.depth=depth;
		this.probabilityModel = new HashMap<Integer,ProbabilisticPiece>();
		initialized=false;
	}

	/* (non-Javadoc)
	 * @see stratego_engine.Player#nextMove(stratego_engine.GameState)
	 */
	@Override
	public PieceAction nextMove(GameState gs) {
		
		ExpectiminimaxAI.nodesExpanded=0;
		
		// Retrive kill list as well

		ArrayList<PieceAction> legalActions = gs.getLegalActions(this.playerID);
		HashMap<Integer,PieceAction> pastActions = gs.getPastActions();
		
		
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
			//	p.setMostLikelyRank(7);
				p.setMostLikelyRank(gs.getPiece(i).getRank());
				this.probabilityModel.put(i, p);
			}
			// Fill prob model with own pieces as well
			for(Integer i:this.myPieces){
				ProbabilisticPiece p = new ProbabilisticPiece(i);
					
				p.setMostLikelyRank(gs.getPiece(i).getRank()); // TODO Fix when PlayerGamestate is implemented
				this.probabilityModel.put(i, p);

			}
			
		}
		
		// Reassign probabilities
		
		
		
		//find optimal legal action through minimax
		
		
		// Be sure to make a deep copy of the map before creating an expectiminimax node
		MapTile[] newMap = new MapTile[100];
		
		for(int i=0;i<newMap.length;i++){
			newMap[i] =this.map[i].getCopy();
		//	System.out.println("Map index: " + i + ", contents: " + newMap[i].getOccupyingPiece()+ ", occupied: "+newMap[i].isOccupied());
		}
		

		// Create Expectiminimax Node from current game state
		GameTreeNode en = new GameTreeNode(this,newMap,false,this.depth,null);
		float reward = en.calulateReward();
		System.out.println("Optimal Action: "+ en.getOptimalAction());
		

				
		System.out.println("Number of nodes expanded: " + ExpectiminimaxAI.nodesExpanded);
		//en.getPiece(); // piece
		//en.getAction(); // action
		// look for a legal action that matchesen.getOptimalAction()
		
		PieceAction selectedAction = legalActions.get(0);
		
		GameTreeAction optimalAction = en.getOptimalAction();
		
		// Check to see if optimal action has been repeated
		int numActions = pastActions.keySet().size();
		int repeatActionKey = numActions - 3;
		
		if(repeatActionKey>0){
			
			PieceAction yourLastAction = pastActions.get(repeatActionKey);
			if(yourLastAction.getPiece().getID()==optimalAction.getPiece()){
				if(yourLastAction.getAction()==optimalAction.getAction()){
					if(yourLastAction.getTargetTile()==optimalAction.getTargetTile()){
						// get the second optimal action
						optimalAction = en.getSecondOptimalAction();
					}
				}
			}
			
		}
		
		for(PieceAction pa:legalActions){
			
			if(pa.getPiece().getID()==optimalAction.getPiece()){ // piece matches
				
				if(pa.getAction()==optimalAction.getAction()){ // acton matches
					if(pa.getTargetTile()==optimalAction.getTargetTile()){ // targe tile matches
						selectedAction = pa;
					}
				}
			}
			
		}
		
		
		// Retrive kill list as well
		

		// Testing: Print all nodes and do a depth first search of all nodes
		
		
		return selectedAction;
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
