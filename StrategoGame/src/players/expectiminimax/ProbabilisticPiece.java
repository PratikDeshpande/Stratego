/**
 * 
 */
package players.expectiminimax;

/**
 * @author Pratik
 *
 */
public class ProbabilisticPiece {
	private int ID;// The piece's ID. Used by the Game Engine
	//private int playerID; // Identity of the player to whom this piece belongs
	//private int x; // Coordinates used (from the player's point of view
	//private int y;
//	private int location;
	//private boolean alive; // Indicates whether or not the piece is alive
	//private boolean hidden;	// Indicates whether or not the piece is hidden from the opponent (for GUI purposes)
	
	private boolean identified;
	private boolean moved;
	
	/*
	 * Rank Likelihoods. The likelihood that the unit is of a certain rank.
	 * 
	 * index	rank
	 * 0		Blank
	 * 1		Marshal		
	 * 2		General
	 * 3		Colonel
	 * 4		Major
	 * 5		Captain
	 * 6		Lieutenant
	 * 7		Sergeant
	 * 8		Miner	: Should increase if it is going towards a known bomb
	 * 9		Scout	: Should become 1.0 if it takes more than one step at a time
	 * 10		Spy		: Should increase if it goes towards Marshall but runs away from everyone else
	 * 11		Bomb	: Should increase during every turn that it doesn't move
	 * 12		Flag	: Should increase during every turn that it doesn't move
	 */
	
	private int mostLikelyRank; 
	private float[] rankLikelihoods;
	
	public ProbabilisticPiece(int id){
		this.ID = id;
				
		// set all likelihoods to equal TODO: Probability function not created yet
	}
	
	/**
	 * @return the identified
	 */
	public boolean isIdentified() {
		return identified;
	}
	/**
	 * @param identified the identified to set
	 */
	public void setIdentified(boolean identified) {
		this.identified = identified;
	}
	/**
	 * @return the moved
	 */
	public boolean hasMoved() {
		return moved;
	}
	/**
	 * @param moved the moved to set
	 */
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	/**
	 * @return the mostLikelyRank
	 */
	public int getMostLikelyRank() {
		return mostLikelyRank;
	}
	/**
	 * Iterate through likelihoods and find the most likely rank for this piece
	 */
	private void calculateMostLikelyRank() {
		return;
	}
	
	// Delete this function once you are done testing
	public void setMostLikelyRank(int rank){
		this.mostLikelyRank=rank;
	}
	
}
