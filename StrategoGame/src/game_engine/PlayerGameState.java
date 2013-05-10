package game_engine;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	/* Returns the current terrain map */ // TODO Change this to navmap, but modify to playerUnits
	public HashMap<Integer,PlayerMapNode>[] getMap(){
		
		HashMap<Integer,MapNode> originalMap = this.gs.getMap();
		HashMap<Integer,PlayerMapNode> playerMap = new HashMap<Integer,PlayerMapNode>();
		
		for(Integer i:originalMap.keySet()){
			MapNode tempMapNode = originalMap.get(i);
			PlayerMapNode temp = new PlayerMapNode(tempMapNode);
			PlayerUnit tempUnit;
			if(this.p.)
			
			= new PlayerUnit()
			temp.setOccupyingUnit(this.gs.getMap().get(i).getOccupyingUnit())
			playerMap.put(i, new Pla)
			
		}
		HashMap<Integer,MapNode> navigationMap
		
		return this.gs.getMap();
	}
	
	/* Returns All the live enemy units */
	public ArrayList<PlayerUnit> getEnemyUnits(){
		ArrayList<Unit> enemyUnits1 = this.gs.getEnemyUnits(this.p.getPlayerID());
		ArrayList<PlayerUnit> enemyUnits2 = new ArrayList<PlayerUnit>();
		for(Unit u:enemyUnits1){
			if(u.getAlive()){
				enemyUnits2.add(new PlayerUnit(GameState.getUnitIndex(u),u.getID()));
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
				myUnits2.add(new PlayerUnit(GameState.getUnitIndex(u),u.getID(),u.getRank()));
			}
		}
		return myUnits2;
	}
	
	/* Returns All the events in that happened in the game */ // TODO: modify so that the move actions do not reveal units
	public ArrayList<GameEvent> getGameEvents(){
		return this.gs.getGameEvents();
	}
	
	/* Returns All the legal actions the player can take */
	public ArrayList<UnitAction> getLegalActions(){
	//	System.out.println("Called PGS's getLegalActions");
		return this.gs.getLegalActions(this.p.getPlayerID());
	}
	
}
