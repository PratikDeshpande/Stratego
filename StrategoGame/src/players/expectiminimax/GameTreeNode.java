/**
 * Represents the supposed game state that the player believes is true
 */
package players.expectiminimax;

import java.util.ArrayList;

import stratego_engine.GameState;
import stratego_engine.MapTile;
import stratego_engine.PieceAction;

/**
 * @author Pratik
 *
 */
public class GameTreeNode {
//	private ArrayList<PieceBelief> enemyPieces;
//	private ArrayList<PieceBelief> myPieces; // NOTE: Don't make a deep copy. probabilities will stay the same. Locations can change on the map[]
	private MapTile[] map; 
	
	private boolean opponent; // Is this node an adversarial node?
	private int currentDepth; // Depth of this node
	private ExpectiminimaxAI parent; // Parent AI Class that instantiated this Game Tree
	
	private ArrayList<GameTreeNode> opponentChildren; // Only initialize if adversary
	private ArrayList<GameTreeNode> myChildren; // Only initialize if NOT adversary
	
	
	// The Action that resulted in this Game Tree Node (null if this is head node)
	private GameTreeAction parentAction;
	
	
	// The optimal action for this node
	private GameTreeAction optimalAction;

	
	
	// Constructor.
	// In the constructor, build a full expectiminimax tree until depth reaches 0
	// make a deep copy of all params IN the constructor
	public GameTreeNode(ExpectiminimaxAI parent, MapTile[] map, boolean opponent, int depth, GameTreeAction gtAction){
		
	//	System.out.println("----------------------------START---------------node depth: " + depth );
		ExpectiminimaxAI.nodesExpanded++;
		this.parent=parent;
		this.map=map; 
				
		this.opponent = opponent;
		this.currentDepth = depth;
	//	System.out.println("current depth: " + this.currentDepth);

		this.parentAction=gtAction;
	//	System.out.println("parent action:: " + this.parentAction);

		// When the game tree is being built, it does not know the optimal action yet
		this.optimalAction=null;
		

		// Based on the game map and parent AI agent's belief distribution of the pieces,
		// create children nodes based on legal actions (GameTreeAction) available to this
		// Node 
		if(opponent){
		//	System.out.println("adversarial node");
			this.myChildren=null;
			this.opponentChildren = this.createChildren();
		}
		else{
		//	System.out.println("non adversarial node");
			this.opponentChildren=null;
			this.myChildren= this.createChildren();
		}
		
	/*	System.out.print("Created GameTree Node with " );
		if(this.myChildren==null){
			System.out.print(this.opponentChildren.size() + " children.\n");
		}
		else{
			System.out.print(this.myChildren.size() + " children.\n");

		}  */
	//	System.out.println("----------------------------END---------------node depth: " + depth );

	}
	
	// Evaluate this state (internatal function used by calculateReward()
	private int evaluate(){
		
		// For now, just add up the number of units you have
		int numPieces = 0; // the number of YOUR pieces
		
		for(MapTile mt:this.map){
			
			if(mt.isOccupied()){
				if(this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getPlayerID()==this.parent.getPlayerID()){
					numPieces++;
				}
				if(this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getPlayerID()!=this.parent.getPlayerID()){
					numPieces = numPieces -10;
				}
			}
		}
		
		
		// TODO: Use Anjan's algorithm
		return numPieces;
	}
	
	// based on legal actions, return child nodes TODO you may not need this
	private ArrayList<GameTreeNode> createChildren(){
	//	System.out.println("Creating children");
		// Print out the map
	//	for(int z=0;z<this.map.length;z++){
			
	//		System.out.println("Map index: " + z + ", contents: " + this.map[z].getOccupyingPiece()+ ", occupied: "+ this.map[z].isOccupied());

	//	}
		// IF this is an opposing node, it tries to find all the hypothetical legal moves available to the OPPOSING character. (and picks the lowest scoring one) TODO: Keep track of your opponent's knowledge as well
		// IF this is NOT an opposing node, it tries to find the hypothetical legal moves available to YOUR pieces (and picks the highest scoring one)
	//	System.out.println("current depth is : " + this.currentDepth);

		// GIven: Alive pieces, map, opponent(bool)
		GameState gs = this.parent.getGameState();
		ArrayList<GameTreeNode> children = new ArrayList<GameTreeNode>();
		if(this.currentDepth<=0){
		//	System.out.println("Current depth is 0");
			return children;
		}
		
	//	System.out.println("Iterating through map");

		// Go through map, find occupied nodes, see if they belong to the correct player, and then fidn its legal actions
		// Then create ExpectiminimaxNodes for this children based on the new map tiles
		for(int i=0;i<this.map.length;i++){
			
			if(this.map[i].isOccupied()){
			//	System.out.println("found occupied tile");

				// Piece's ID
				int pieceID = this.map[i].getOccupyingPiece();
				// Piece's Estimated Rank (Based on probability model of parent AI agent)
		//		System.out.println(pieceID);
		//		System.out.println(gs.getPiece(pieceID));
		//		System.out.println("Most likely rank: " + parent.getProbabilityModel().get(pieceID).getMostLikelyRank());
				int hRank = parent.getProbabilityModel().get(pieceID).getMostLikelyRank();
				// Piece's PlayerID
				int piecePlayerID = gs.getPiece(pieceID).getPlayerID();
				
			//	System.out.println("occupied by: " + pieceID);
			//	System.out.println("estimated rank: " + hRank);
			//	System.out.println("belongs to Player: " + piecePlayerID);



				
				// In this fucntion we are using universal coordinates where (0,0) is the top left corner of the map
				int pieceX = i%10;
				int pieceY = i/10;
									
				// These variables represent the location tile values of the adjacent tiles
				int below = i+10;
				int above = i-10;
				int right = i+1;
				int left = i-1;
				// NOTE: For some instances these may go over the edge of the map
				
				// if piece belongs to opposing player
		//		if(gs.getPiece(this.parent.getPlayerID(),this.map[i].getOccupyingPiece())==null){ // TODO Fix this when you implement PlayerGameState.
										
					// if this node is an opposing node
		//			if(this.opponent){
						

				int[] adjacentIndices = new int[4];
				adjacentIndices[0] = below;
				adjacentIndices[1] = above;
				adjacentIndices[2] = right;
				adjacentIndices[3] = left;
				
				for(int l=0;l<adjacentIndices.length;l++){
					
				
						// Find the legal moves availabe based on rank
						
						// If the piece is not a scout // TODO Change GameState.SCOUT back to GameState.MINER
						if((hRank<=GameState.SCOUT)||(hRank==GameState.SPY)){
							
							// Find the adjacent pieces and create child nodes from them
							
							int coordinate = 0; // these values should change in the if statements
							int bound = 0;
							
							if(l==0){ // adjacent below
								coordinate = pieceY;
								bound = 9;
							}
							if(l==1){ // adjacent right
								coordinate =(-1)* pieceY;
								bound = 0;		
							}
							if(l==2){ // adjacent left
								coordinate = pieceX;
								bound = 9;
							}
							if(l==3){
								coordinate = (-1)*pieceX;
								bound = 0;
							}
							
							// if the adjacent spot below the piece is unoccupied, the piece can move there
							// Note: First, check to see if it is not out of bounds
							if(coordinate<bound){
								// if adjacent spot is NOT occupied
								if(!this.map[adjacentIndices[l]].isOccupied()){
									// and if the adjacent spot is traversible
									if(this.map[adjacentIndices[l]].isTraversible()){
										
									//	System.out.println("piece's adjacent spot below is unoccupied and traversible");

										// Make a new deep map for this (move) action
										MapTile[] m = new MapTile[100];
										
										for(int j=0;j<m.length;j++){
											m[j] =this.map[j].getCopy();
										}
										
										// Reflects the changes of the map if agent takes this action
										m[i].setOccupyingPiece(-1); 
										m[adjacentIndices[l]].setOccupyingPiece(pieceID);
										
									//	System.out.println("creating move node");

										// Create an action that reflects the changes in the map
										GameTreeAction a = new GameTreeAction(pieceID,PieceAction.MOVE,i,adjacentIndices[l],-1);
										
										
										// determine whether or not it should be added here
										if(this.opponent){
										//	System.out.println("this is opposing node");

											if(gs.getPiece(pieceID).getPlayerID()!=this.parent.getPlayerID()){
												children.add(new GameTreeNode(this.parent,m,false,this.currentDepth-1, a));
											}

										}
										else{
										//	System.out.println("this is our node");
										//	System.out.println(pieceID);
										//	System.out.println(gs.getPiece(pieceID).);

											if(gs.getPiece(pieceID).getPlayerID()==this.parent.getPlayerID()){
										//		System.out.println("piece's player ID matches ours");

												children.add(new GameTreeNode(this.parent,m,true,this.currentDepth-1, a));
											}
												
										}
										//children.add(new GameTreeNode(this.parent,moveBelow,false,this.currentDepth-1, a));
																				
									}
								}
								
								// if adjacent spot is NOT occupied
								else{
									// if occupying piece is an enemy piece, then the main piece can attack it.
							//		System.out.println(this.map[below].getOccupyingPiece());
							//		System.out.println(this.map[below].isOccupied());
							//		System.out.println(gs.getPiece(this.map[below].getOccupyingPiece()));
							//		System.out.println("Index: "+below);

									if(gs.getPiece(this.map[adjacentIndices[l]].getOccupyingPiece()).getPlayerID()!=piecePlayerID){//(gs.getPiece(this.map[below].getOccupyingPiece())!=null){
										
										
										// Make a new deep map for this (attack) action
										MapTile[] ma = new MapTile[100];
										
										for(int j=0;j<ma.length;j++){
											ma[j] =this.map[j].getCopy();
										}
										
										ma[i].setOccupyingPiece(-1); // piece will move away from this spot regardless
										
										// see who dies/lives
										int targetRank = this.parent.getProbabilityModel().get(this.map[adjacentIndices[l]].getOccupyingPiece()).getMostLikelyRank();
										int targetID = this.map[adjacentIndices[l]].getOccupyingPiece();
										
										if(targetRank==GameState.BOMB){
											if(hRank==GameState.MINER){
												ma[adjacentIndices[l]].setOccupyingPiece(pieceID);
											}
										}
										if(targetRank==GameState.MARSHAL){
											if(hRank==GameState.SPY){
												ma[adjacentIndices[l]].setOccupyingPiece(pieceID);
											}
										}
										if(hRank==targetRank){
											ma[adjacentIndices[l]].setOccupyingPiece(-1);
										}
										if(hRank<targetRank){
											ma[adjacentIndices[l]].setOccupyingPiece(pieceID);
										}
									//	public GameTreeAction(int piece, int action, int startTile, int targetTile, int target){

										GameTreeAction a = new GameTreeAction(pieceID, PieceAction.ATTACK,i,adjacentIndices[l],targetID);
										
										// determine whether or not it should be added here
										if(this.opponent){
											
											if(gs.getPiece(pieceID).getID()!=this.parent.getPlayerID()){
												children.add(new GameTreeNode(this.parent,ma,false,this.currentDepth-1, a));
											}

										}
										else{
											if(gs.getPiece(pieceID).getID()==this.parent.getPlayerID()){
												children.add(new GameTreeNode(this.parent,ma,true,this.currentDepth-1, a));
											}
												
										}
										//children.add(new GameTreeNode(this.parent,attackBelow,false,this.currentDepth-1,a));
									}
								}
							}															
							
						}
						// if it is a scout
						
						
						
						
				}// end adjacency for loop
						
						// end
	
				//	}
					
					
					
			//	} 
				/*
				// if piece is yours
				// need exact characterization
				else if(gs.getPiece(this.map[i].getOccupyingPiece()).getPlayerID()==this.parent.getPlayerID()){ // TODO Fix when you implment PlayerGameState
				
					
					// if this node is an NOT an opposing node
					if(!this.opponent){
						
						// Piece's ID
					//	int pieceID = this.map[i].getOccupyingPiece();
						// Find the piece's expected rank
					//	int hRank = parent.getProbabilityModel().get(this.map[i].getOccupyingPiece()).getMostLikelyRank();
						
						
						// Find the legal moves availabe based on rank
						
						// If the piece is not a scout. NOTE: Hrank is 100% accurate in this case
						if((hRank<=GameState.MINER)||(hRank==GameState.SPY)){
							
							// Find the adjacent pieces and create child nodes from them
							
							// Use universal x, y coordinates (top left is 0,0)
					//		int pieceX = i%10;
					//		int pieceY = i/10;
							
					//		int below = i+10;
					//		int above = i-10;
					//		int right = i+1;
					//		int left = i-1;
							
							// if the adjacent spot below the piece is unoccupied, the piece can move there
							// Note: First, check to see if it is not out of bounds
							if(pieceY<9){
								// if adjacent spot is NOT occupied
								if(!this.map[below].isOccupied()){
									// and if the adjacent spot is traversible
									if(this.map[below].isTraversible()){
										
										// Make a new deep map for this (move) action
										MapTile[] moveBelow = new MapTile[100];
										
										for(int j=0;j<moveBelow.length;j++){
											moveBelow[j] =this.map[j].getCopy();
										}
										
										moveBelow[i].setOccupyingPiece(-1); 
										moveBelow[below].setOccupyingPiece(pieceID);
										

										children.add(new GameTreeNode(this.parent,moveBelow,pieceID,PieceAction.MOVE,i,below,-1 ,false,this.currentDepth-1));
																				
									}
								}
								// else if the adjacent spot ahead of the piece is your piece, then the opposing piece can attack it
								else{
									if(gs.getPiece(this.map[below].getOccupyingPiece())!=null){
										
										
										// Make a new deep map for this (attack) action
										MapTile[] attackBelow = new MapTile[100];
										
										for(int j=0;j<attackBelow.length;j++){
											attackBelow[j] =this.map[j].getCopy();
										}
										
										attackBelow[i].setOccupyingPiece(-1); 
										
										// see who dies/lives
										int targetRank = this.parent.getProbabilityModel().get(this.map[below].getOccupyingPiece()).getMostLikelyRank();
										int targetID = this.map[below].getOccupyingPiece();
										
										
										if(targetRank==GameState.BOMB){
											if(hRank==GameState.MINER){
												attackBelow[below].setOccupyingPiece(pieceID);
											}
										}
										if(targetRank==GameState.MARSHAL){
											if(hRank==GameState.SPY){
												attackBelow[below].setOccupyingPiece(pieceID);
											}
										}
										if(hRank==targetRank){
											attackBelow[below].setOccupyingPiece(-1);
										}
										if(hRank<targetRank){
											attackBelow[below].setOccupyingPiece(pieceID);
										}
										
										//	public ExpectiminimaxNode(ExpectiminimaxAI parent, MapTile[] map, int piece, int action, int startTile, int targetTile, int target, boolean opponent,  int depth){
										
										children.add(new GameTreeNode(this.parent,attackBelow, pieceID, PieceAction.ATTACK, i, below, targetID        ,false,this.currentDepth-1));
									}
								}
							}															
							
						}
						// if it is a scout
						
						
						
						
						
						
						// end
						
						
						
						
						
						
						
						
					}
					
					
					
				}
				*/			
			}
			//end for loop			
		} 
		
		return children;
	}
	
	// TODO: Fix. Does not Perform Minimax
	// Calculates the reward for this node
	public int calulateReward(){
		
		if(this.currentDepth==0){
			return this.evaluate();
		}
		// else calculate the value of all the children
		else if(this.opponent){
			// Pick lowest r
			int r = Integer.MAX_VALUE;
			for(GameTreeNode opponentChild:this.opponentChildren){
				if(opponentChild.calulateReward()<r){
					r = opponentChild.calulateReward();
					// Update optimal action
					this.optimalAction = opponentChild.getParentAction();
				}
			}
			return r;
		}
		else{
			// pick highest r
			int r = Integer.MIN_VALUE;
			for(GameTreeNode child:this.myChildren){
				if(child.calulateReward()>r){
					r = child.calulateReward();
					// Update optimal action
					this.optimalAction = child.getParentAction();

				}
			}
			return r;
		}		
	}

	/**
	 * @return the parentAction
	 */
	public GameTreeAction getParentAction() {
		return parentAction;
	}

	/**
	 * @param parentAction the parentAction to set
	 */
	public void setParentAction(GameTreeAction parentAction) {
		this.parentAction = parentAction;
	}

	/**
	 * @return the optimalAction
	 */
	public GameTreeAction getOptimalAction() {
		return optimalAction;
	}

	/**
	 * @param optimalAction the optimalAction to set
	 */
	public void setOptimalAction(GameTreeAction optimalAction) {
		this.optimalAction = optimalAction;
	}
	
}
