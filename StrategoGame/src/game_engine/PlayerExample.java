package game_engine;

import java.util.ArrayList;


public class PlayerExample extends Player{
	//private int playerID;

	public PlayerExample(int playerID){
		super(playerID);
	}
	
	
	public void initializePos() {
		// TODO Auto-generated method stub
		
		// Unit(playerID, x, y, rank
		this.getUnits().add(new Unit(playerID,0, 0, 12)); // marshall
		this.getUnits().add(new Unit(playerID,1, 0, 11)); // general
		this.getUnits().add(new Unit(playerID,2, 0, 11)); // colonel
		this.getUnits().add(new Unit(playerID,3, 0, 11)); // colonel
		
		this.getUnits().add(new Unit(playerID,4, 0, 11)); // major
		this.getUnits().add(new Unit(playerID,5, 0, 11)); //  major
		this.getUnits().add(new Unit(playerID,6, 0, 11)); // major
		this.getUnits().add(new Unit(playerID,7, 0, 10)); // captain
		
		this.getUnits().add(new Unit(playerID,8, 0, 9)); // captain
		this.getUnits().add(new Unit(playerID,9, 0, 9)); // captain
		this.getUnits().add(new Unit(playerID,0, 1, 9)); // captain
		this.getUnits().add(new Unit(playerID,1, 1, 9)); // lieutenant
		
		this.getUnits().add(new Unit(playerID,2, 1, 9)); // lieutenant
		this.getUnits().add(new Unit(playerID,3, 1, 9)); // lieutenant
		this.getUnits().add(new Unit(playerID,4, 1, 9)); // lieutenant
		this.getUnits().add(new Unit(playerID,5, 1, 9)); // sergeant
	
		this.getUnits().add(new Unit(playerID,6, 1, 8)); // sergeant
		this.getUnits().add(new Unit(playerID,7, 1, 8)); // sergeant
		this.getUnits().add(new Unit(playerID,8, 1, 8)); // sergeant
		this.getUnits().add(new Unit(playerID,9, 1, 8)); // miner
		
		this.getUnits().add(new Unit(playerID,0, 2, 8)); // miner
		this.getUnits().add(new Unit(playerID,1, 2, 7)); // miner
		this.getUnits().add(new Unit(playerID,2, 2, 7)); // miner
		this.getUnits().add(new Unit(playerID,3, 2, 7)); // miner
	
		this.getUnits().add(new Unit(playerID,4, 2, 7)); // scout
		this.getUnits().add(new Unit(playerID,5, 2, 6)); // scout
		this.getUnits().add(new Unit(playerID,6, 2, 6)); // scout
		this.getUnits().add(new Unit(playerID,7, 2, 6)); // scout
		
		this.getUnits().add(new Unit(playerID,8, 2, 6)); // scout
		this.getUnits().add(new Unit(playerID,9, 2, 5)); // scout
		this.getUnits().add(new Unit(playerID,0, 3, 5)); // scout
		this.getUnits().add(new Unit(playerID,1, 3, 5)); // scout
	
		this.getUnits().add(new Unit(playerID,2, 3, 5)); // spy
		this.getUnits().add(new Unit(playerID,3, 3, 4)); // bomb
		this.getUnits().add(new Unit(playerID,4, 3, 4)); // bomb
		this.getUnits().add(new Unit(playerID,5, 3, 4)); // bomb
		
		this.getUnits().add(new Unit(playerID,6, 3, 3)); // bomb
		this.getUnits().add(new Unit(playerID,7, 3, 3)); // bomb
		this.getUnits().add(new Unit(playerID,8, 3, 2)); // bomb
		this.getUnits().add(new Unit(playerID,9, 3, 1)); // flag


	}
	

	public UnitAction nextMove(GameState gs) {
		// TODO Auto-generated method stub
		ArrayList<UnitAction> legalActions = gs.getLegalActions(this.getPlayerID()); // Change object so that playerID is automatic
		return legalActions.get(0);
		
	}


}
