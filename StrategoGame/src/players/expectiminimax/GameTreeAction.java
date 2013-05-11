/**
 * 
 */
package players.expectiminimax;

import stratego_engine.PieceAction;

/**
 * @author Pratik
 *
 */
public class GameTreeAction {
	private int piece;
	private int action;
	private int startTile; 
	private int targetTile;
	private int target;
	
	public GameTreeAction(int piece, int action, int startTile, int targetTile, int target){
		this.piece = piece;
		this.action = action;
		this.startTile = startTile;
		this.targetTile = targetTile;
		this.target=target;
		
	}
	
	
	/**
	 * @return the piece
	 */
	public int getPiece() {
		return piece;
	}
	/**
	 * @param piece the piece to set
	 */
	public void setPiece(int piece) {
		this.piece = piece;
	}
	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}
	/**
	 * @return the startTile
	 */
	public int getStartTile() {
		return startTile;
	}
	/**
	 * @param startTile the startTile to set
	 */
	public void setStartTile(int startTile) {
		this.startTile = startTile;
	}
	/**
	 * @return the targetTile
	 */
	public int getTargetTile() {
		return targetTile;
	}
	/**
	 * @param targetTile the targetTile to set
	 */
	public void setTargetTile(int targetTile) {
		this.targetTile = targetTile;
	}
	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}	

	public String toString(){
		String r = this.piece + " at " + this.startTile;
		if(this.action==PieceAction.MOVE){
			r = r+" moves to "+this.targetTile + ".";
		}
		if(this.action==PieceAction.ATTACK){
			r = r+" attacks "+this.target+" at " + this.targetTile + ".";
		}
		return r;	}
	
}
