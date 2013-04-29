package game_engine;

/* 
 * This class represents the abstraction that of the Unit that the player sees
 */
public class PlayerUnit {

	private int location;
	private int ID;
	
	public PlayerUnit(int location, int ID){
		this.location = location;
		this.setID(ID);
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
}
