/**
 * 
 */
package players.expectiminimax;

import stratego_engine.GameState;

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
		this.rankLikelihoods = new float[13];
		
		this.rankLikelihoods[0] = 0; // place holder so we can use 1-based indexing
		this.rankLikelihoods[1] = GameState.NUM_MARSHAL;
		this.rankLikelihoods[2] = GameState.NUM_GENERAL;
		this.rankLikelihoods[3] = GameState.NUM_COLONEL;
		this.rankLikelihoods[4] = GameState.NUM_MAJOR;
		this.rankLikelihoods[5] = GameState.NUM_CAPTAIN;
		this.rankLikelihoods[6] = GameState.NUM_LIEUTENANT;
		this.rankLikelihoods[7] = GameState.NUM_SERGEANT;
		this.rankLikelihoods[8] = GameState.NUM_MINER;
		this.rankLikelihoods[9] = GameState.NUM_SCOUT;
		this.rankLikelihoods[10] = GameState.NUM_SPY;
		this.rankLikelihoods[11] = GameState.NUM_BOMB;
		this.rankLikelihoods[12] = GameState.NUM_FLAG;
		
		this.normalize();
		
	}
	
	/*
	 * Normalize the linkelihoods so that they add up to 1.0
	 */
	private void normalize(){
		float sum = 0;
		for(float i:this.rankLikelihoods){
			sum = sum+i;
		}
		
		for(int i=0;i<this.rankLikelihoods.length;i++){
			this.rankLikelihoods[i] = this.rankLikelihoods[i]/sum;
		}
	}
	
	
	
	public void printLikelihoods(){
		for(int i=0;i<this.rankLikelihoods.length;i++){
			System.out.println("Rank: " + i +", likelihood: "+ this.rankLikelihoods[i]+ "\n");
		}
	}
	
	// Given an array of 
	
	// This ensures that the model stays normalized
	public void setLikelihood(int index,float value){
		this.rankLikelihoods[index]=value;
		this.normalize();
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
