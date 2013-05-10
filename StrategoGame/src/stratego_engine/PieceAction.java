package stratego_engine;

public class PieceAction {
	// Define the types of actions here
	public static final int MOVE = 0;
	public static final int ATTACK = 1;
	
	
	private Piece piece; // the piece that is moving/attacking
	private int action;	// whether or not the action is a move or attack
	private int startTile; // the start location of the piece
	private int targetTile; // the location of the destination
	private Piece target; // if the move is an attack, this piece is not null
	
	// Constructor for a Move action
	public PieceAction(Piece piece,int targetTile){
		this.piece= piece;//.getID();
		this.action=PieceAction.MOVE;
		this.startTile = piece.getLocation();
		this.targetTile = targetTile;
		this.target=null;//=1;
	}
	
	// Constructor for an attack action
	public PieceAction(Piece piece, Piece target){
		this.piece= piece;//.getID();
		this.action=PieceAction.ATTACK;
		this.startTile = piece.getLocation();
		this.targetTile = target.getLocation();
		this.target=target;//.getID();
	}
	
	/*
	// constructor for a move action using just ints (useful when agent shouldn't see the inner workings of the engine
	public PieceAction(GameState gs, int piece, int b, boolean isMoveAction){
		// TODO see if you can do this w.o passing in game state
		this.piece=piece;
		
		if(isMoveAction){
			this.action=PieceAction.MOVE;
			this.startTile = gs.getPiece(piece).getLocation();
			this.targetTile = b;
			this.target=-1;
		}
		else{
			
			this.action=PieceAction.ATTACK;
			this.startTile = gs.getPiece(piece).getLocation();
			this.targetTile = gs.getPiece(b).getLocation();
			this.target= b; 
			
		}
		
	}
	*/
	
	/**
	 * @return the piece
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @return the startTile
	 */
	public int getStartTile() {
		return startTile;
	}

	/**
	 * @return the targetTile
	 */
	public int getTargetTile() {
		return targetTile;
	}

	/**
	 * @return the target
	 */
	public Piece getTarget() {
		return target;
	}
	
	public String toString(){
		String r = this.piece + " ";
		if(this.action==PieceAction.MOVE){
			r = r+"moves to "+this.targetTile + ".";
		}
		if(this.action==PieceAction.ATTACK){
			r = r+"attacks "+this.target+".";
		}
		return r;
	}


}
