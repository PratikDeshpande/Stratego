/**
 * This class is the superclass of all AI Agents that programmers can make
 */
package stratego_engine;


/**
 * @author Pratik
 *
 */
public abstract class Player {
	protected int playerID;
	protected boolean initialized;
	protected String startPosition; // String representation of player's starting position
	
	public Player(int playerID){
		this.playerID=playerID;
		this.setStartPosition();
	}
	
	/*
	 * Programmers will override this function to establish start position of pieces.
	 * Comes with a default start position
	 */
	private void setStartPosition(){
		this.startPosition = "7,11,5,8,5,8,8,4,11,12,11,9,5,5,9,9,9,9,4,11,11,3,10,8,9,9,9,8,3,11,7,7,2,6,6,6,4,1,6,7";
		//this.startPosition = "7,11,5,8,5,8,8,4,11,12,11,9,5,5,4,1,6,6,4,11,11,3,10,8,9,9,9,8,3,11,7,7,2,6,9,9,9,9,6,7";

	}
	
	public abstract PieceAction nextMove(GameState gs);


	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @param playerID the playerID to set
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * @return the startPosition
	 */
	public String getStartPosition() {
		return startPosition;
	}

}
