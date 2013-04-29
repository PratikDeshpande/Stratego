package game_engine;

import java.util.ArrayList;


public abstract class Player {
	protected int  playerID ; // should it be static?
	protected ArrayList<Unit> units;
	protected boolean initialized;
	
	public Player(int playerID)
	{
		this.playerID = playerID;
		this.units = new ArrayList<Unit>();
		initializePos();
		
	}
	
	public void checkConflict(){
		return;
	}
	
	//public abstract void initializ
	
	public void initializePos(){
		
		// Unit(playerID, x, y, rank. // Comment out for testing. Uncomment this fragment later
		this.getUnits().add(new Unit(playerID,0, 0, 12)); //  flag
		this.getUnits().add(new Unit(playerID,1, 0, 11)); //   bomb
		this.getUnits().add(new Unit(playerID,2, 0, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,3, 0, 11)); //    bomb
		
		this.getUnits().add(new Unit(playerID,4, 0, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,5, 0, 11)); //     bomb
		this.getUnits().add(new Unit(playerID,6, 0, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,7, 0, 10)); //    spy
		
		this.getUnits().add(new Unit(playerID,8, 0, 9)); //    scout
		this.getUnits().add(new Unit(playerID,9, 0, 9)); //    scout
		this.getUnits().add(new Unit(playerID,0, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,1, 1, 9)); //    scout
		
		this.getUnits().add(new Unit(playerID,2, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,3, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,4, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 1, 9)); //    scout
	
		this.getUnits().add(new Unit(playerID,6, 1, 8)); //    miner
		this.getUnits().add(new Unit(playerID,7, 1, 8)); //    miner
		this.getUnits().add(new Unit(playerID,8, 1, 8)); //    miner
		this.getUnits().add(new Unit(playerID,9, 1, 8)); //    miner
		
		this.getUnits().add(new Unit(playerID,0, 2, 8)); //    miner
		this.getUnits().add(new Unit(playerID,1, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,2, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,3, 2, 7)); //    sergeant
	
		this.getUnits().add(new Unit(playerID,4, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,5, 2, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,6, 2, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,7, 2, 6)); //    lieutenant
		
		this.getUnits().add(new Unit(playerID,8, 2, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,9, 2, 5)); //    captain
		this.getUnits().add(new Unit(playerID,0, 3, 5)); //    captain
		this.getUnits().add(new Unit(playerID,1, 3, 5)); //    captain
	
		this.getUnits().add(new Unit(playerID,2, 3, 5)); //    captain
		this.getUnits().add(new Unit(playerID,3, 3, 4)); //    major
		this.getUnits().add(new Unit(playerID,4, 3, 4)); //    major
		this.getUnits().add(new Unit(playerID,5, 3, 4)); //    major
		
		this.getUnits().add(new Unit(playerID,6, 3, 3)); //    colonel
		this.getUnits().add(new Unit(playerID,7, 3, 3)); //    colonel
		this.getUnits().add(new Unit(playerID,8, 3, 2)); //    general
		this.getUnits().add(new Unit(playerID,9, 3, 1)); //    marshal
		
		
		
	}
	
/*	public void initializeUnits()
	{
		
	} */
	
	public abstract UnitAction nextMove(PlayerGameState gs);

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}
	
	
	
}
