package game_engine;

import java.util.ArrayList;

/*
 * This class represents the abstraction that of the GameState that the player sees
 */
public class PlayerGameState{

	private GameState gs;
	private Player p;
	
	public PlayerGameState(GameState gs, Player p ){
		this.gs=gs;
		this.p=p;
	}
	
	/* Returns the current terrain map */
	public int[] getMap(){
		return this.gs.getMap();
	}
	
	/* Returns All the live enemy units */
	public ArrayList<PlayerUnit> getEnemyUnits(){
		ArrayList<Unit> enemyUnits1 = this.gs.getEnemyUnits(this.p.getPlayerID());
		ArrayList<PlayerUnit> enemyUnits2 = new ArrayList<PlayerUnit>();
		for(Unit u:enemyUnits1){
			if(u.getAlive()){
				enemyUnits2.add(new PlayerUnit(this.gs.getUnitIndex(u),u.getID()));
			}
		}
		
		return enemyUnits2;
	}
	
	/* Returns All player's alive units */
	public ArrayList<PlayerUnit> getMyUnits(){
		ArrayList<Unit> myUnits1 = this.gs.getMyUnits(this.p.getPlayerID());
		ArrayList<PlayerUnit> myUnits2 = new ArrayList<PlayerUnit>();
		for(Unit u:myUnits1){
			if(u.getAlive()){
				myUnits2.add(new PlayerUnit(this.gs.getUnitIndex(u),u.getID()));
			}
		}
		return myUnits2;
	}
	
	/* Returns All the events in that happened in the game */
	public ArrayList<GameEvent> getGameEvents(){
		return this.gs.getGameEvents();
	}
	
	/* Returns All the legal actions the player can take */
	public ArrayList<UnitAction> getLegalActions(){
	//	System.out.println("Called PGS's getLegalActions");
		return this.gs.getLegalActions(this.p.getPlayerID());
	}
	
}
