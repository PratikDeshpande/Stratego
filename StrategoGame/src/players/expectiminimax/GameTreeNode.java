/**
 * Represents the supposed game state that the player believes is true
 */
package players.expectiminimax;

import java.util.ArrayList;
import java.util.HashMap;

import stratego_engine.GameState;
import stratego_engine.MapTile;
import stratego_engine.PieceAction;

/**
 * @author Pratik
 *
 */
public class GameTreeNode {
	
	// Default Weights of all the pieces
	public static final float MARSHAL_WEIGHT = 400;
	public static final float GENERAL_WEIGHT = 200;
	public static final float COLONEL_WEIGHT = 100;
	public static final float MAJOR_WEIGHT = 75;
	public static final float CAPTAIN_WEIGHT = 50;
	public static final float LIEUTENANT_WEIGHT = 25;
	public static final float SERGEANT_WEIGHT = 15;
	public static final float MINER_WEIGHT = 25;
	public static final float SCOUT_WEIGHT = 30;
	public static final float SPY_WEIGHT = 200;
	public static final float BOMB_WEIGHT = 20;
	public static final float FLAG_WEIGHT = 10000;
	
	
	
//	private ArrayList<PieceBelief> enemyPieces;
//	private ArrayList<PieceBelief> myPieces; // NOTE: Don't make a deep copy. probabilities will stay the same. Locations can change on the map[]
	private MapTile[] map; 
	
	private boolean opponent; // Is this node an adversarial node?
	private int currentDepth; // Depth of this node
	private ExpectiminimaxAI parent; // Parent AI Class that instantiated this Game Tree
	
	// Changed from ArrayList to HashMap so that its value can be stored
	private HashMap<GameTreeNode,Float> opponentChildren; // Only initialize if adversary
	private HashMap<GameTreeNode,Float> myChildren; // Only initialize if NOT adversary
	
	
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
	private float evaluate(){
		
		
		// Keeps track of how many of your pieces are alive
		int[] myPieces = new int[13];
		// Keeps track of how many of the enemy pieces are alive
		int[] enemyPieces = new int[13];
		
		// Keeps track of the weights of your pieces
		float[] myWeights = new float[13];
		// Keeps track of the weights of enemy pieces
		float[] enemyWeights = new float[13];
		
		myWeights[1] = GameTreeNode.MARSHAL_WEIGHT;
		myWeights[2] = GameTreeNode.GENERAL_WEIGHT;
		myWeights[3] = GameTreeNode.COLONEL_WEIGHT;
		myWeights[4] = GameTreeNode.MAJOR_WEIGHT;
		myWeights[5] = GameTreeNode.CAPTAIN_WEIGHT;
		myWeights[6] = GameTreeNode.LIEUTENANT_WEIGHT;
		myWeights[7] = GameTreeNode.SERGEANT_WEIGHT;
		myWeights[8] = GameTreeNode.MINER_WEIGHT;
		myWeights[9] = GameTreeNode.SCOUT_WEIGHT;
		myWeights[10] = GameTreeNode.SPY_WEIGHT;
		myWeights[11] = GameTreeNode.BOMB_WEIGHT;
		myWeights[12] = GameTreeNode.FLAG_WEIGHT;

		enemyWeights[1] = GameTreeNode.MARSHAL_WEIGHT;
		enemyWeights[2] = GameTreeNode.GENERAL_WEIGHT;
		enemyWeights[3] = GameTreeNode.COLONEL_WEIGHT;
		enemyWeights[4] = GameTreeNode.MAJOR_WEIGHT;
		enemyWeights[5] = GameTreeNode.CAPTAIN_WEIGHT;
		enemyWeights[6] = GameTreeNode.LIEUTENANT_WEIGHT;
		enemyWeights[7] = GameTreeNode.SERGEANT_WEIGHT;
		enemyWeights[8] = GameTreeNode.MINER_WEIGHT;
		enemyWeights[9] = GameTreeNode.SCOUT_WEIGHT;
		enemyWeights[10] = GameTreeNode.SPY_WEIGHT;
		enemyWeights[11] = GameTreeNode.BOMB_WEIGHT;
		enemyWeights[12] = GameTreeNode.FLAG_WEIGHT;
		
		
		// Count the number of pieces on board in this node
		for(MapTile mt:this.map){
			if(mt.isOccupied()){
				// If this is your piece
				if(this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getPlayerID()==this.parent.getPlayerID()){
					
					// add to the count
					myPieces[this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getRank()]++;
					
				}
				// If this is not your piece
				if(this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getPlayerID()!=this.parent.getPlayerID()){
					
					// add to the count
					enemyPieces[this.parent.getGameState().getPiece(mt.getOccupyingPiece()).getRank()]++;
					
				}
				
			}
		}
		
		// First feature: Multiplying the weight of the Marshall by 0.8 if the opponent has a spy on board
		if(enemyPieces[GameState.SPY]>0){ // enemy has spy
			myWeights[GameState.MARSHAL] = (float) (myWeights[GameState.MARSHAL] * 0.8);
		}
		if(myPieces[GameState.SPY]>0){ // if our agent has a spy
			enemyWeights[GameState.MARSHAL] = (float) (enemyWeights[GameState.MARSHAL] * 0.8);
		}
		
		// Second feature: Multiplying the weight of the Miners with (4-#left) if the number of miners is less than 3
		if(myPieces[GameState.MINER]<3){ // if our agent has less than 3 miners
			myWeights[GameState.MINER] = myWeights[GameState.MINER] * (4 - myPieces[GameState.MINER]);
		}
		if(enemyPieces[GameState.MINER]<3){ // if our agent has less than 3 miners
			enemyWeights[GameState.MINER] = enemyWeights[GameState.MINER] * (4 - enemyPieces[GameState.MINER]);
		}
		
		// Third feature: Sets the value of the bomb
		
		// Testing: Print myPieces here
		
		/*
		for(int i=1;i<myPieces.length;i++){
			System.out.println("Number of pieces of rank " + i + ": "+ myPieces[i]);

		}
		
		// Testing: Print myWegihts here
		for(int i=1;i<myWeights.length;i++){
			System.out.println("Weight for rank " + i + ": "+ myWeights[i]);
		}
		*/
		// Total: Multiplies the weights of the pieces with the number still on board
		float myScore = 0;
		float enemyScore = 0;
		
		for(int i=1;i<myPieces.length;i++){
			
			myScore = myScore + (myWeights[i] * myPieces[i]);
			enemyScore = enemyScore + (enemyWeights[i] * enemyPieces[i]);
		}
		
		
		
			
		
	
	//	System.out.println("myScore : " + myScore);
	//	System.out.println("enemyScore : " + enemyScore);

		// TODO: Use Anjan's algorithm
		return myScore;
	}
	
	// based on legal actions, return child nodes TODO you may not need this
	private HashMap<GameTreeNode,Float> createChildren(){
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
		HashMap<GameTreeNode,Float> children = new HashMap<GameTreeNode,Float>();
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
												children.put(new GameTreeNode(this.parent,m,false,this.currentDepth-1, a),(float) 0);
											}

										}
										else{
										//	System.out.println("this is our node");
										//	System.out.println(pieceID);
										//	System.out.println(gs.getPiece(pieceID).);

											if(gs.getPiece(pieceID).getPlayerID()==this.parent.getPlayerID()){
										//		System.out.println("piece's player ID matches ours");

												children.put(new GameTreeNode(this.parent,m,true,this.currentDepth-1, a),(float) 0);
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
												children.put(new GameTreeNode(this.parent,ma,false,this.currentDepth-1, a),(float) 0);
											}

										}
										else{
											if(gs.getPiece(pieceID).getID()==this.parent.getPlayerID()){
												children.put(new GameTreeNode(this.parent,ma,true,this.currentDepth-1, a),(float) 0);
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
	public float calulateReward(){
		
		if(this.currentDepth==0){
			return this.evaluate();
		}
		// else calculate the value of all the children
		else if(this.opponent){
			// Pick lowest r
			float r = Integer.MAX_VALUE; // we intentionally do not use Float.MAX_VALUE. We don't need to
			for(GameTreeNode opponentChild:this.opponentChildren.keySet()){
				float cr = opponentChild.calulateReward() + this.evaluate();
				this.opponentChildren.put(opponentChild, cr);
				if(cr<r){
					r = cr;
					// Update optimal action
					this.optimalAction = opponentChild.getParentAction();
				}
			}
			return r;
		}
		else{
			// pick highest r
			float r = Integer.MIN_VALUE;
			for(GameTreeNode child:this.myChildren.keySet()){
				float chr = child.calulateReward()+ this.evaluate();
				this.myChildren.put(child, chr);
				if(chr>r){
					r = chr;
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
	
	public GameTreeAction getSecondOptimalAction(){
		
		// Go through all myChildren and find the second highest valued child
		float highest = Integer.MIN_VALUE;
		GameTreeNode highestNode = null;
		float secondHighest = Integer.MIN_VALUE;
		GameTreeNode secondHighestNode = null;
		if(!opponent){
			

			for(GameTreeNode child:this.myChildren.keySet()){
				
				if(this.myChildren.get(child)>highest){
					secondHighest = highest;
					secondHighestNode = highestNode;
					highest = this.myChildren.get(child);
					highestNode = child;
				}
				
			}
			
			if(secondHighestNode==null){
				
				secondHighest = Integer.MIN_VALUE;

				for(GameTreeNode child:this.myChildren.keySet()){
					
					if(this.myChildren.get(child)<highest){
						
						if(this.myChildren.get(child)>secondHighest){
							secondHighest = this.myChildren.get(child);
							secondHighestNode = child;
						}
						
					}
					
				}
			}
			
		}
		
		return secondHighestNode.getParentAction();
		
		
	}

	/**
	 * @param optimalAction the optimalAction to set
	 */
	public void setOptimalAction(GameTreeAction optimalAction) {
		this.optimalAction = optimalAction;
	}
	
	// For testing purposes. Prints the depth, parent action, game map, adversary statusnumber of children
	public void printNode(){
		
	}
	
}
