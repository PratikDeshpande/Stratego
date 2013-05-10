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
public class ExpectiminimaxNode {
//	private ArrayList<PieceBelief> enemyPieces;
//	private ArrayList<PieceBelief> myPieces; // NOTE: Don't make a deep copy. probabilities will stay the same. Locations can change on the map[]
	private MapTile[] map; // NOTE: Make a NEW map, dont' use this one
	
	private boolean opponent; // Is this node an adversarial node?
	private int currentDepth;
	private ExpectiminimaxAI parent;
	
	private ArrayList<ExpectiminimaxNode> opponentChildren; // Only initialize if adversary
	private ArrayList<ExpectiminimaxNode> children; // Only initialize if NOT adversary
	
	// Constructor.
	// In the constructor, build a full expectiminimax tree until depth reaches 0
	// make a deep copy of all params IN the constructor
	public ExpectiminimaxNode(ExpectiminimaxAI parent, MapTile[] map, boolean opponent,  int depth){
		
		this.parent=parent;
		this.opponent = opponent;
		this.currentDepth = depth;
		

		// Based on the map and parent's belief pieces, create populate children based on legal actions
		
		if(opponent){
			this.children=null;
			this.opponentChildren = this.createChildren();
		}
		else{
			this.opponentChildren=null;
			this.children= this.createChildren();
		}
		
	}
	
	// Evaluate this state (internatal function used by calculateReward()
	private int evaluate(){
		// TODO: Use Anjan's algorithm
		return 1;
	}
	
	// based on legal actions, return child nodes TODO you may not need this
	private ArrayList<ExpectiminimaxNode> createChildren(){
		
		// IF this is an opposing node, it tries to find all the hypothetical legal moves available to the OPPOSING character. (and picks the lowest scoring one) TODO: Keep track of your opponent's knowledge as well
		// IF this is NOT an opposing node, it tries to find the hypothetical legal moves available to YOUR pieces (and picks the highest scoring one)
		
		// GIven: Alive pieces, map, opponent(bool)
		GameState gs = this.parent.getGameState();
		ArrayList<ExpectiminimaxNode> children = new ArrayList<ExpectiminimaxNode>();
		if(this.currentDepth<=0){
			return children;
		}
		
		// Go through map, find occupied nodes, see if they belong to the correct player, and then fidn its legal actions
		// Then create ExpectiminimaxNodes for this children based on the new map tiles
		for(int i=0;i<this.map.length;i++){
			
			if(this.map[i].isOccupied()){
				
				// if piece belongs to opposing player
				if(gs.getPiece(this.map[i].getOccupyingPiece())==null){
					
					// if this node is an opposing node
					if(this.opponent){
						
						// Piece's ID
						int pieceID = this.map[i].getOccupyingPiece();
						// Find the piece's expected rank
						int hRank = parent.getProbabilityModel().get(this.map[i].getOccupyingPiece()).getMostLikelyRank();
						
						
						// Find the legal moves availabe based on rank
						
						// If the piece is not a scout
						if((hRank<=GameState.MINER)||(hRank==GameState.SPY)){
							
							// Find the adjacent pieces and create child nodes from them
							
							// Use universal x, y coordinates (top left is 0,0)
							int pieceX = i%10;
							int pieceY = i/10;
							
							int below = i+10;
							int above = i-10;
							int right = i+1;
							int left = i-1;
							
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
										
										children.add(new ExpectiminimaxNode(this.parent,moveBelow,false,this.currentDepth-1));
																				
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
										
										if(hRank==targetRank){
											attackBelow[below].setOccupyingPiece(-1);
										}
										
										children.add(new ExpectiminimaxNode(this.parent,attackBelow,false,this.currentDepth-1));
										legalActions.add(new PieceAction(piece, this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()+1)].getOccupyingPiece()) ));
									}
								}
							}
						
							// if the adjacent spot behind the piece is unoccupied, the piece can move there
							// Note: First, check to see if it is not out of bounds
							if(piece.getY()>0){
								// if adjacent spot is NOT occupied
								if(!this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1)].isOccupied()){
									if(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1)].isTraversible()){
										System.out.println(this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1));
										legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1)));
									}
								}
								// else if the adjacent spot behind the piece is an opponent, then you can attack it
								else{
									if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1)].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
										legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1)].getOccupyingPiece()) ));
									}
								}
							}
							
							// if the adjacent spot to the right of the piece is unoccupied, the piece can move there
							// Note: First, check to see if it is not out of bounds
							if(piece.getX()<9){
								// if adjacent spot is NOT occupied
								if(!this.map[this.getLocation(piece.getPlayerID(), piece.getX()+1, piece.getY())].isOccupied()){
									if(this.map[this.getLocation(piece.getPlayerID(), piece.getX()+1, piece.getY())].isTraversible()){
										legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX()+1, piece.getY())));
									}
								}
								// else if the adjacent spot to the right of the piece is an opponent, then you can attack it
								else{
									if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX()+1, piece.getY())].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
										legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX()+1, piece.getY())].getOccupyingPiece()) ));
									}
								}
							}
							
							// if the adjacent spot to the left of the piece is unoccupied, the piece can move there
							// Note: First, check to see if it is not out of bounds
							if(piece.getX()>0){
								// if adjacent spot is NOT occupied
								if(!this.map[this.getLocation(piece.getPlayerID(), piece.getX()-1, piece.getY())].isOccupied()){
									if(this.map[this.getLocation(piece.getPlayerID(), piece.getX()-1, piece.getY())].isTraversible()){
										legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX()-1, piece.getY())));
									}
								}
								// else if the adjacent spot to the left of the piece is an opponent, then you can attack it
								else{
									if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX()-1, piece.getY())].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
										legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX()-1, piece.getY())].getOccupyingPiece()) ));
									}
								}
							}
							
							
							
							
						}
						
						
						
						
						
						
						// end
						
						
						
						
						
						
						
						
					}
					
					
					
				} 
				// if piece is yours
				// need exact characterization
				else if(gs.getPiece(this.map[i].getOccupyingPiece()).getPlayerID()==this.parent.getPlayerID()){
				
					
					
					
					
				}
							
			}
						
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
			int r = 0;
			for(ExpectiminimaxNode opponentChild:this.opponentChildren){
				r = r + opponentChild.calulateReward();
			}
			return r;
		}
		else{
			int r = 0;
			for(ExpectiminimaxNode child:this.children){
				r = r + child.calulateReward();
			}
			return r;
		}		
	}
	
}
