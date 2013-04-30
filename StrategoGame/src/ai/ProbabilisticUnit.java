package ai;

import game_engine.GameState;

/*
 * This class represents the AI agent's knowledge of the identity of the enemy unit. Each
 * rank in the game is assigned a probability. It represents the probability that the unit
 * belongs to a certain rank based on the progress of the game.
 * P(Unit's rank is x|Sequence of actions taken by enemy in game)
 */
public class ProbabilisticUnit {


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
	public float[] rankLikelihoods; // The sum of all probabilities inside should be 1.
	private int location;
	private int ID;
	private boolean identified;
	private boolean possiblyIdentified;
	private boolean moved;
	
	public ProbabilisticUnit(int location, int ID){
		this.location = location;
		this.ID = ID;
		this.setIdentified(false); // always starts out as unidentified
		this.possiblyIdentified=false;
		this.moved=false;
		
		rankLikelihoods = new float[13];
		
		rankLikelihoods[0] = 0; // Keep this at 0 so it doesn't mess up the normalizations
		rankLikelihoods[GameState.MARSHAL] = GameState.NUM_MARSHAL;
		rankLikelihoods[GameState.GENERAL] = GameState.NUM_GENERAL;
		rankLikelihoods[GameState.COLONEL] = GameState.NUM_COLONEL;
		rankLikelihoods[GameState.MAJOR] = GameState.NUM_MAJOR;
		rankLikelihoods[GameState.CAPTAIN] = GameState.NUM_CAPTAIN;
		rankLikelihoods[GameState.LIEUTENANT] = GameState.NUM_LIEUTENANT;
		rankLikelihoods[GameState.SERGEANT] = GameState.NUM_SERGEANT;
		rankLikelihoods[GameState.MINER] = GameState.NUM_MINER;
		rankLikelihoods[GameState.SCOUT] = GameState.NUM_SCOUT;
		rankLikelihoods[GameState.SPY] = GameState.NUM_SPY;
		rankLikelihoods[GameState.BOMB] = GameState.NUM_BOMB;
		rankLikelihoods[GameState.FLAG] = GameState.NUM_FLAG;
		
		this.normalize();

		
	}
	
	public void normalize(){
		float sum = 0;
		// First find the sum;
		for(float l:rankLikelihoods){
			sum = sum+l;
		}
		// Divide each element by the sum
		for(int i=1;i<rankLikelihoods.length;i++){
			rankLikelihoods[i]=rankLikelihoods[i]/sum;
		}
	}
	
	// Returns the most likely rank for this unit
	public int getHighestLikelihood(){
		
		float highestL = 0;
		int mostLikelyRank = 1;
		for(int i=0;i<13;i++){
			if(rankLikelihoods[i]>highestL){
				highestL = rankLikelihoods[i];
				mostLikelyRank = i;
			}
		}
		return mostLikelyRank;
	}
	
	public void printLikelihoods(){
		for(float l:rankLikelihoods){
			System.out.println(l + "\n");
		}
	}
	
	// This ensures that the model stays normalized
	public void setLikelihood(int index,float value){
		this.rankLikelihoods[index]=value;
		this.normalize();
	}
	
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isIdentified() {
		return identified;
	}

	public void setIdentified(boolean identified) {
		this.identified = identified;
	}

	public boolean isPossiblyIdentified() {
		return possiblyIdentified;
	}

	public void setPossiblyIdentified(boolean possiblyIdentified) {
		this.possiblyIdentified = possiblyIdentified;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	} 


}
