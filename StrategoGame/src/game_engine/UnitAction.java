package game_engine;

public class UnitAction {
	private Unit unit;
	private int action;
	private int targetTile;
	private Unit target; // Can be null if there is no target
	
	public static final int move = 0;
	public static final int attack = 1;
	
	// Neutral Movement Actions
/*	public static final int forward = 0;
	public static final int backward = 1;
	public static final int right = 2;
	public static final int left = 3;
	
	// Aggressive Movement Actions 
	public static final int forward_attack = 4;
	public static final int backward_attack = 5;
	public static final int right_attack = 6;
	public static final int left_attack = 7; */

	
	public UnitAction(Unit u, int action, int targetTile, Unit target){
		this.unit = u;
		this.action = action;
		this.targetTile = targetTile;
		this.target = target; // setTarget(target);
	}
	
	public UnitAction(Unit u, int action, int targetTile){
		this(u,action,targetTile, null);
	}


	public Unit getUnit() {
		return unit;
	}


	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	public int getAction() {
		return action;
	}


	public void setAction(int action) {
		this.action = action;
	}

	public Unit getTarget() {
		return target;
	}

	public void setTarget(Unit target) {
		this.target = target;
	}

	public int getTargetTile() {
		return targetTile;
	}

	public void setTargetTile(int targetTile) {
		this.targetTile = targetTile;
	} 

}
