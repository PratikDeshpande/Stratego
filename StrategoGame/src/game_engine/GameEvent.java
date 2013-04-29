package game_engine;

public class GameEvent extends UnitAction{


	
	private boolean unitDies;
	private boolean targetDies;
	
	public GameEvent(Unit u, int action, int targetTile, Unit target,boolean unitDies,boolean targetDies){
		super(u,action,targetTile,target);
	//	this.unitDies = unitDies;
	//	this.targetDies = targetDies;
		/*this.unit = u;
		this.action = action;
		this.targetTile = targetTile;
		this.target = target; // setTarget(target); */
	}
	
	public GameEvent(Unit u, int action, int targetTile){
		this(u,action,targetTile, null,false,false);
	}
	
	public GameEvent(UnitAction action){
		this(action.getUnit(),action.getAction(),action.getTargetTile(),action.getTarget(),false,false);
	}

	public boolean getUnitDies() {
		return unitDies;
	}

	public void setUnitDies(boolean unitDies) {
		this.unitDies = unitDies;
	}

	public boolean getTargetDies() {
		return targetDies;
	}

	public void setTargetDies(boolean targetDies) {
		this.targetDies = targetDies;
	}
	
	public PlayerUnit getPlayerUnit(){
		Unit u = super.getUnit();
		return new PlayerUnit(GameState.getUnitIndex(u),u.getID());
	}
	

}
