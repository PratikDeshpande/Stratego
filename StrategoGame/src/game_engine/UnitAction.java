package game_engine;

public class UnitAction {
	private Unit unit;
	private int action;
	
	// Neutral Movement Actions
	public static final int forward = 0;
	public static final int backward = 1;
	public static final int right = 2;
	public static final int left = 3;
	
	// Aggressive Movement Actions 
	public static final int forward_attack = 4;
	public static final int backward_attack = 5;
	public static final int right_attack = 6;
	public static final int left_attack = 7;

	
	public UnitAction(Unit u, int action){
		this.setUnit(u);
		this.setAction(action);
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

}
