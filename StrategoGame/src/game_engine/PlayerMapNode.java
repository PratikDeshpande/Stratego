package game_engine;

public class PlayerMapNode {
	private boolean traversible;
	//private boolean occupied;
	private boolean occupied;
	private PlayerUnit occupyingUnit; // Could be null
	
	public PlayerMapNode(boolean traversible){
		this.traversible = traversible;
	}
	
	/**
	 * @return the traversible
	 */
	public boolean isTraversible() {
		return traversible;
	}
	/**
	 * @param traversible the traversible to set
	 */
	public void setTraversible(boolean traversible) {
		this.traversible = traversible;
	}
	/**
	 * @return the occupied
	 */
	public boolean isOccupied() {
		return occupied;
	}
	/**
	 * @param occupied the occupied to set
	 */
	/*public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}*/
	/**
	 * @return the occupyingUnit
	 */
	public PlayerUnit getOccupyingUnit() {
		return occupyingUnit;
	}
	/**
	 * @param occupyingUnit the occupyingUnit to set
	 */
	public void setOccupyingUnit(PlayerUnit occupyingUnit) {
		this.occupyingUnit = occupyingUnit;
		if(occupyingUnit==null){
			this.occupied=false;// setOccupied(false);
		}
		else{
			this.occupied=true;// setOccupied(true);
		}
	}

}
