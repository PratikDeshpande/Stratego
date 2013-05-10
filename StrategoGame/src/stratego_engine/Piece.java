/**
 * This class represents a piece on the board game stratego
 */
package stratego_engine;

/**
 * @author Pratik
 *
 */
public class Piece {
	private int ID;// The piece's ID. Used by the Game Engine
	private int playerID; // Identity of the player to whom this piece belongs
	private int x; // Coordinates used (from the player's point of view
	private int y;
//	private int location;
	private boolean alive; // Indicates whether or not the piece is alive
	private boolean hidden;	// Indicates whether or not the piece is hidden from the opponent (for GUI purposes)
	private int rank; // Rank of the piece
	
	
	public Piece(int playerID,int x, int y, int rank){
		GameState.numPieces++;
		this.ID=GameState.numPieces;

		
		// Have checks to make sure piece is legal
		if((playerID==1)||(playerID==2)){
			this.playerID=playerID;
		}
		else{
			// Throw exception: Illegal Player Number. Must be 1 or 2
		}
		if((x<10)&&(x>=0)&&(y<10)&&(y>=0)){
			this.x=x;
			this.y=y;
		}
		else{
			// Throw exception: Piece x and y location must be less than 10 and greater than 0
		}
		if((rank>=1)&&(rank<=12)){
			this.rank=rank;
		}
		else{
			// Throw exception: Illegal Rank: Must be within 1-12
		}
				
		this.alive=true;
		this.hidden=true;
		
		//System.out.println("Initialized piece "+this.ID+" with "+this.rank+ " at "+this.x+", "+this.y);

	}
	
	public int getLocation(){
		int location = -1;
		if(playerID==1){
			location= (9-this.x)+(this.y*10);
		}
		if(playerID==2){
			location = this.x + ((9-this.y)*10);
		}
		return location;
		
	}
	
	public void setLocation(int location){
		if(playerID==1){
			this.x= 9-(location%10);
			this.y= location/10;
		}
		if(playerID==2){
			this.x= location%10;
			this.y= 9-(location/10);
		}
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void kill() {
		this.hidden = false;
		this.alive = false;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void reveal() {
		this.hidden = false;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}
	
	public String toString(){
		return "[Piece: "+this.getID()+", Rank: "+this.getRank()+", Location: "+this.getLocation()+"]";

	}
	
}
