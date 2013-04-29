package game_engine;

/* 
 * This class represents the abstraction that of the Unit that the player sees
 */
public class PlayerUnit {

	private int location;
	private int ID;
	private int rank; // if known. if not it should be null
	
	public PlayerUnit(int location, int ID, int rank){
		this.location = location;
		this.ID = ID;
		this.rank=rank;
	}
	public PlayerUnit(int location, int ID){
		this(location,ID,20);
	}
	
	public int getLocatioin(){
		return this.location;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
