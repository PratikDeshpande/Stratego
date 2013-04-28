package game_engine;

import java.util.ArrayList;
import java.util.Random;


public class PlayerExample extends Player{
	//private int playerID;

	public PlayerExample(int playerID){
		super(playerID);
	}
	
	
	public void initializePos() {
		// TODO Auto-generated method stub
		// TODO Make sure there are a correct number of players
		// Unit(playerID, x, y, rank
		this.getUnits().add(new Unit(playerID,0, 0, 7)); //  sergeant
		this.getUnits().add(new Unit(playerID,1, 0, 11)); //   bomb
		this.getUnits().add(new Unit(playerID,2, 0, 5)); //    captain
		this.getUnits().add(new Unit(playerID,3, 0, 8)); //    miner
		
		this.getUnits().add(new Unit(playerID,4, 0, 5)); //   captain 
		this.getUnits().add(new Unit(playerID,5, 0, 8)); //     miner
		this.getUnits().add(new Unit(playerID,6, 0, 8)); //    miner
		this.getUnits().add(new Unit(playerID,7, 0, 4)); //    major
		
		this.getUnits().add(new Unit(playerID,8, 0, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,9, 0, 12)); //    flag
		this.getUnits().add(new Unit(playerID,0, 1, 11)); //    bomb 	
		this.getUnits().add(new Unit(playerID,1, 1, 9)); //    scout	
		
		// For testing
	//	this.getUnits().add(new Unit(playerID,9, 0, 9)); //    scout
	//	this.getUnits().add(new Unit(playerID,5, 5, 11)); // bomb

		this.getUnits().add(new Unit(playerID,2, 1, 5)); //    captain
		this.getUnits().add(new Unit(playerID,3, 1, 5)); //    captain
		this.getUnits().add(new Unit(playerID,4, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 1, 9)); //    scout
	
		this.getUnits().add(new Unit(playerID,6, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,7, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,8, 1, 4)); //    major
		this.getUnits().add(new Unit(playerID,9, 1, 11)); //    bomb
		
		this.getUnits().add(new Unit(playerID,0, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,1, 2, 3)); //    colonel 
		this.getUnits().add(new Unit(playerID,2, 2, 10 )); //    spy
		this.getUnits().add(new Unit(playerID,3, 2, 8)); //    miner 
			
		this.getUnits().add(new Unit(playerID,4, 2, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 2, 9)); //    scout 
		this.getUnits().add(new Unit(playerID,6, 2, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,7, 2, 8)); //    miner
		
		this.getUnits().add(new Unit(playerID,8, 2, 3)); //    colonel
		this.getUnits().add(new Unit(playerID,9, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,0, 3, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,1, 3, 7)); //    sergeant
	
		this.getUnits().add(new Unit(playerID,2, 3, 2)); //    general
		this.getUnits().add(new Unit(playerID,3, 3, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,4, 3, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 3, 6)); //    lieutenant
		
		this.getUnits().add(new Unit(playerID,6, 3, 4)); //    major
		this.getUnits().add(new Unit(playerID,7, 3, 1)); //    marshal
		this.getUnits().add(new Unit(playerID,8, 3, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,9, 3, 11)); //    bomb  
		

		

	}
	

	public UnitAction nextMove(GameState gs) {
		// These are all the attributes available to player from GameState
		
		ArrayList<UnitAction> legalActions = gs.getLegalActions(this.getPlayerID()); // Change object so that playerID is automatic
		ArrayList<Unit> enemyUnits = gs.getEnemyUnits(this.getPlayerID());
		ArrayList<Unit> myUnits = gs.getMyUnits(this.getPlayerID());
		
		
		// return random legal action
		Random randomGenerator = new Random();
				
		int randInt = randomGenerator.nextInt(legalActions.size());
		return legalActions.get(randInt);
		//return legalActions.get(0);
		
	}


}
