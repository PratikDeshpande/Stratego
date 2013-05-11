package stratego_engine;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This class represents the current state of the board (pieces and their locations)
 */
public class GameState {
	
	// Define Ranks here
	public static final int MARSHAL = 1;
	public static final int GENERAL = 2;
	public static final int COLONEL = 3;
	public static final int MAJOR = 4;
	public static final int CAPTAIN = 5;
	public static final int LIEUTENANT = 6;
	public static final int SERGEANT = 7;

	// Special Characters
	public static final int MINER = 8;
	public static final int SCOUT = 9;
	public static final int SPY = 10;
	public static final int BOMB = 11;
	public static final int FLAG = 12;

	// Number of each units available to each side at initialization
	public static final int NUM_MARSHAL = 1;
	public static final int NUM_GENERAL = 1;
	public static final int NUM_COLONEL = 2;
	public static final int NUM_MAJOR = 3;
	public static final int NUM_CAPTAIN = 4;
	public static final int NUM_LIEUTENANT = 4;
	public static final int NUM_SERGEANT = 4;
	public static final int NUM_MINER = 5;
	public static final int NUM_SCOUT = 8;
	public static final int NUM_SPY = 1;
	public static final int NUM_BOMB = 6;
	public static final int NUM_FLAG = 1;
	
	// Players
	private Player player1,player2;
	
	// These variables keep track of game statistics
	private HashMap<Integer,PieceAction> pastActions; // All the actions that have been taken by the players in the game
	private int numActions;
	private int[] player1Killed; // Keeps track of how many of player 1's pieces from each rank have been killed
	private int[] player2Killed; // Keeps track of how many of player 2's pieces from each rank have been killed	
	private boolean gameOver; // Keeps track of whether or not the game has been won or not.
	private Player winningPlayer; // Winner of the game
	public static int numPieces;	// Keeps track of the number of pieces instantiated in the game
	
	private MapTile[] map; // Represents each tile in the map
	HashMap<Integer,Piece> pieces; // Maps piece id to piece on the map
	
	public GameState(Player player1, Player player2, int[] terrain){
		
		// Initialize the game map
		this.map = new MapTile[100];
		
		for(int i=0;i<terrain.length;i++){
			if(terrain[i]==0){
				map[i] = new MapTile(true);
			}
			else{
				map[i] = new MapTile(false);
			}
		}
		
		// Assign player variables
		this.player1=player1;
		this.player2=player2;
		
		// Set up starting pieces according to player start Positions
		
		// Initialize the number of pieces to 0
		GameState.numPieces = 0;
		
		String[] p1StartPosition = this.player1.getStartPosition().split(",");
		String[] p2StartPosition = this.player2.getStartPosition().split(","); 

		this.pieces=new HashMap<Integer,Piece>();
		
		int p=0;
		int x,y;
		for(String s:p1StartPosition){
			x = p%10;
			y = p/10;
			Piece tempPiece = new Piece(1,x,y,Integer.parseInt(s));
			this.pieces.put(tempPiece.getID(), tempPiece); 
			this.map[this.getLocation(1, x, y)].setOccupyingPiece(tempPiece.getID()); // Change map occupancy to reflect pieces
			p++;
		}
		p=0;
		for(String s:p2StartPosition){
			x=p%10;
			y=p/10;
			Piece tempPiece = new Piece(2,x,y,Integer.parseInt(s));
			this.pieces.put(tempPiece.getID(), tempPiece);    
			this.map[this.getLocation(2, x, y)].setOccupyingPiece(tempPiece.getID()); // Change map occupancy to reflect pieces
			p++;
		}
		// NOTE: Code is repeated so that the Piece IDs remain consecutive for each player
				
	//	for(Integer i:this.pieces.keySet()){
	//		System.out.println("id: "+i+" rank: "+this.pieces.get(i).getRank()+" at " + this.pieces.get(i).getLocation());
	//	}
		
		// Initialize the kill count arrays
		this.player1Killed = new int[13]; // 1-based indexing. 0 is blank;
		this.player2Killed = new int[13];
		
		for(int i=0;i<player1Killed.length;i++){
			this.player1Killed[i]=0;
			this.player2Killed[i]=0;
		}
		
		this.setWinningPlayer(null); // No winner at the beginning
		this.setGameOver(false); // 
		
		// Initialize list of all events in the game (Hashmap so that it can be accessed in order)[ordered list would suffice as well]
		this.numActions=0;
		this.pastActions=new HashMap<Integer,PieceAction>();
		
		// End constructor
	}
	
	// Returns Piece given PieceID
	public Piece getPiece(int pieceID){
		return this.pieces.get(pieceID);
	}
	
	// Only returns piece's identity if it belongs to that player
	public Piece getPiece(int playerID, int pieceID){
		Piece p = this.pieces.get(pieceID);
		if(p.getPlayerID()==playerID){
			return p;
		}
		else{
			return null;
		}
	}
	
	// Gives you the tile location given x,y,and player Id
	private int getLocation(int playerID, int x, int y){
		//int location = -1;
		if(playerID==1){
			return (9-x)+(y*10);
		}
		if(playerID==2){
			return x + ((9-y)*10);
		}
		return -1;
	}
	
	// return all legal actions available for this piece
	// TODO: For PlayerGameState, just send [pieceID, move, targettile, targetID]
	public ArrayList<PieceAction> getLegalActions(Piece piece){

		ArrayList<PieceAction> legalActions = new ArrayList<PieceAction>();
		
		// if piece is dead, just return an empty arraylist
		if(!piece.isAlive()){
			return legalActions;
		}
		
		int playerID = piece.getPlayerID(); // player id of the piece controlling the player
		int rank = piece.getRank(); // rank of the piece
		int pieceX = piece.getX(); // piece's x-coordinate from player's coordinate frame
		int pieceY = piece.getY(); // piece's y-coordinate from the player's coordinate frame
		
		
		// If the piece is not a scout
		if((rank<=GameState.MINER)||(rank==GameState.SPY)){
			
			// if the adjacent spot ahead of the piece is unoccupied, the piece can move there
			// Note: First, check to see if it is not out of bounds
			if(pieceY<9){
				// if adjacent spot is NOT occupied
				if(!this.map[this.getLocation(playerID, pieceX, pieceY+1)].isOccupied()){
					// and if the adjacent spot is traversible
					if(this.map[this.getLocation(playerID, pieceX, piece.getY()+1)].isTraversible()){
						legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()+1)));
					}
				}
				// else if the adjacent spot ahead of the piece is an opponent, then you can attack it
				else{
					if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()+1)].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
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
						//System.out.println(this.getLocation(piece.getPlayerID(), piece.getX(), piece.getY()-1));
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
		
		// If the piece is a scout
		if(piece.getRank()==GameState.SCOUT){
			
			// all legal moves ahead
			int aheadY = piece.getY()+1;
			// while y<9, is traversible, and is unoccupied
			while((aheadY<9)&&(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)].isTraversible())&&(!this.map[this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)].isOccupied())){
				legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)));
				aheadY = aheadY+1;
			}
			// if aheadY is occupied
			if(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)].isOccupied()){
				// if the occupying unit is an opponent
				if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
					legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), aheadY)].getOccupyingPiece()) ));
				}
			}
			
			// all legal moves behind
			int behindY = piece.getY()-1;
			// while y>0, is traversible, and is unoccupied
	//		System.out.println("behindY: "+behindY);
			while((behindY>0)&&(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), behindY)].isTraversible())&&(!this.map[this.getLocation(piece.getPlayerID(), piece.getX(), behindY)].isOccupied())){
				legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), piece.getX(), behindY)));
				behindY = behindY-1;
			}
			// if behindY is occupied
	//		System.out.println("behindY: "+behindY);
	//		System.out.println("piece: "+piece.getID());
	//		System.out.println("piece.getX: "+piece.getX());
	//		System.out.println("location: "+ this.getLocation(piece.getPlayerID(), piece.getX(), behindY));

			if(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), behindY)].isOccupied()){
				// if the occupying unit is an opponent
				if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), behindY)].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
					legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), piece.getX(), behindY)].getOccupyingPiece()) ));
				}
			}
			
			// all legal moves to the right
			int rightX = piece.getX()+1;
			// while x<10, is traversible, and is unoccupied
			while((rightX<10)&&(this.map[this.getLocation(piece.getPlayerID(), rightX, piece.getY())].isTraversible())&&(!this.map[this.getLocation(piece.getPlayerID(), rightX, piece.getY())].isOccupied())){
				legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), rightX, piece.getY())));
				rightX = rightX+1;
			}
			// if rightX is occupied
			if(this.map[this.getLocation(piece.getPlayerID(), rightX, piece.getY())].isOccupied()){
				// if the occupying unit is an opponent
				if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), rightX, piece.getY())].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
					legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), rightX, piece.getY())].getOccupyingPiece()) ));
				}
			}
			
			// all legal moves to the left
			int leftX = piece.getX()-1;
			// while x>=0, is traversible, and is unoccupied
			while((leftX>=0)&&(this.map[this.getLocation(piece.getPlayerID(), leftX, piece.getY())].isTraversible())&&(!this.map[this.getLocation(piece.getPlayerID(), leftX, piece.getY())].isOccupied())){
				legalActions.add(new PieceAction(piece, this.getLocation(piece.getPlayerID(), leftX, piece.getY())));
				leftX = leftX-1;
			}
			// if leftX is occupied
			if(this.map[this.getLocation(piece.getPlayerID(), leftX, piece.getY())].isOccupied()){
				// if the occupying unit is an opponent
				if(this.getPiece(this.map[this.getLocation(piece.getPlayerID(), leftX, piece.getY())].getOccupyingPiece()).getPlayerID()!=piece.getPlayerID()){
					legalActions.add(new PieceAction(piece,this.getPiece(this.map[this.getLocation(piece.getPlayerID(), leftX, piece.getY())].getOccupyingPiece()) ));
				}
			}
			
			
		}
		
		return legalActions;
	}
	
	// get all the legal actions available to player
	public ArrayList<PieceAction> getLegalActions(int playerID){
		ArrayList<PieceAction> legalActions = new ArrayList<PieceAction>();
		
		// For all of the player's pieces, add the legal actions to list
		for(Piece p:this.pieces.values()){
			if(p.getPlayerID()==playerID){
				legalActions.addAll(this.getLegalActions(p));
			}
		}
		
		
		return legalActions;
	}
	
	// returns all the opponent's live pieces
	public ArrayList<Integer> getOpponentPieces(int playerID){
	
		ArrayList<Integer> opponentPieces = new ArrayList<Integer>();
		
		for(Piece p:this.pieces.values()){
			if(p.isAlive()){
				if(p.getPlayerID()!=playerID){
					opponentPieces.add(p.getID());
				}
			}
		}
		
		return opponentPieces;
	}
	
	// returns all the player's live pieces
	public ArrayList<Integer> getMyPieces(int playerID){
	
		ArrayList<Integer> myPieces = new ArrayList<Integer>();
		
		for(Piece p:this.pieces.values()){
			if(p.isAlive()){
				if(p.getPlayerID()==playerID){
					myPieces.add(p.getID());
				}
			}
		}
		
		return myPieces;
	}
	
	// returns all actions taken by the two players in the game
	public HashMap<Integer,PieceAction> getPastActions(){
		return this.pastActions;
	}
	
	
	//change game state to reflect that this action has been taken
	public void execute(PieceAction action){
		
		
		// Adds the action to the list of past actions
		this.numActions++;
		this.pastActions.put(this.numActions, action);
		
		// The starting tile becomes unoccupied no matter what the outcome of the action
		this.map[action.getStartTile()].setOccupyingPiece(-1);
		
		// The piece's location will be changed regardless of the outcome of the action
		action.getPiece().setLocation(action.getTargetTile()); 
		
		// NOTE: IF the action is just a move, is the occupying piece set to the piece?
		
		// If the action is an attack
		if(action.getAction()==PieceAction.ATTACK){
			
			// both pieces are revealed
			action.getPiece().reveal();
			action.getTarget().reveal();
			
			// the ranks of the two competing pieces
			int attackerRank = action.getPiece().getRank();
			int targetRank = action.getTarget().getRank();
			
			// if the target happens to be a bomb
			if(targetRank==GameState.BOMB){
				System.out.println("Attacked a bomb");
				// if the attacker is a miner
				if(attackerRank==GameState.MINER){
					action.getTarget().kill();
					
				}
				else{
					System.out.println("attacker dies");

					action.getPiece().kill();
				}
			}
			// if the target is a Marshal
			if(targetRank==GameState.MARSHAL){
				if(attackerRank==GameState.SPY){
					action.getTarget().kill();
				}
			}
			// if both pieces are of equal rank
			if(attackerRank==targetRank){
				action.getPiece().kill();
				action.getTarget().kill();
			}
			// if attacker is a weaker piece
			if(attackerRank>targetRank){
				action.getPiece().kill();
			}
			// if attacker is a stronger piece
			if(attackerRank<targetRank){
				action.getTarget().kill();
			}
			

			// if target dies
			if(!action.getTarget().isAlive()){
				this.map[action.getTargetTile()].setOccupyingPiece(-1);
				// Add to killed list
				if(action.getTarget().getPlayerID()==1){
					this.player1Killed[action.getTarget().getRank()]=this.player1Killed[action.getTarget().getRank()]+1;
				}
				if(action.getTarget().getPlayerID()==2){
					this.player2Killed[action.getTarget().getRank()]=this.player2Killed[action.getTarget().getRank()]+1;
				}
			}
			
		}
		
		// if piece remains alive
		if(action.getPiece().isAlive()){
			this.map[action.getTargetTile()].setOccupyingPiece(action.getPiece().getID());
		}
		else{
			// the piece has died
			// add the piece to the killed list
			if(action.getPiece().getPlayerID()==1){
				this.player1Killed[action.getPiece().getRank()]=this.player1Killed[action.getPiece().getRank()]+1;
			}
			if(action.getPiece().getPlayerID()==2){
				this.player2Killed[action.getPiece().getRank()]=this.player2Killed[action.getPiece().getRank()]+1;
			}
			
			
			
		}
	}

	/**
	 * @return the player1Killed
	 */
	public int[] getPlayer1Killed() {
		return player1Killed;
	}

	/**
	 * @return the player2Killed
	 */
	public int[] getPlayer2Killed() {
		return player2Killed;
	}

	/**
	 * @return the gameOver
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * @param gameOver the gameOver to set
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * @return the winningPlayer
	 */
	public Player getWinningPlayer() {
		return winningPlayer;
	}

	/**
	 * @param winningPlayer the winningPlayer to set
	 */
	public void setWinningPlayer(Player winningPlayer) {
		this.winningPlayer = winningPlayer;
	}
	
	public MapTile[] getMap(){
		return this.map;
	}

}
