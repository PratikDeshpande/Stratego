package stratego_engine;

public class MapTile {
	private boolean traversible;
	private boolean occupied;
	private int occupyingPiece;
	
	public MapTile(boolean traversible){
		this.traversible = traversible;
	}

	/**
	 * @return the traversible
	 */
	public boolean isTraversible() {
		return traversible;
	}

	/**
	 * @return the occupyingPiece
	 */
	public int getOccupyingPiece() {
		return occupyingPiece;
	}

	/**
	 * @param occupyingPiece the occupyingPiece to set
	 */
	public void setOccupyingPiece(int occupyingPiece) {
		this.occupyingPiece = occupyingPiece;
		if(occupyingPiece==-1){
			this.occupied=false;
		}
		else{
			this.occupied=true;
		}
	}

	/**
	 * @return the occupied
	 */
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * returns a deep copy of the map tile
	 * @return
	 */
	public MapTile getCopy(){
		MapTile n = new MapTile(this.traversible);
		n.setOccupyingPiece(this.occupyingPiece);
		return n;		
	}


}
