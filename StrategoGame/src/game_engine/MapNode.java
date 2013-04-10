package game_engine;

public class MapNode {
	//private int index;
	private boolean traversible;
	//private boolean occupied;
	private boolean occupied;
	private Unit occupyingUnit; // Could be null
	
	/*
	public MapNode(boolean traversible, Unit occupyingUnit){
	//	this.setIndex(index);
		this.setTraversible(traversible);
		//this.setOccupyingUnit(occupyingUnit);
		this.occupyingUnit = occupyingUnit;
	}*/
	
	public MapNode(boolean traversible){
		//this(traversible,null);
		this.traversible=traversible;
	}

	public Unit getOccupyingUnit() {
		return occupyingUnit;
	}

	public void setOccupyingUnit(Unit occupyingUnit) {
		this.occupyingUnit = occupyingUnit;
		if(occupyingUnit==null){
			this.occupied=false;// setOccupied(false);
		}
		else{
			this.occupied=true;// setOccupied(true);
		}
	}

	public boolean isTraversible() {
		return traversible;
	}

	public void setTraversible(boolean traversible) {
		this.traversible = traversible;
	}

	public boolean isOccupied() {
		return occupied;
	}

	// Shouldn't use this method. Should be encapsulated in setOccupyingUnit
/*	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}*/

	/*
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	*/
	
	
}
