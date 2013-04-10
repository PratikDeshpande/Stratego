package game_engine;


public  class Unit{
	private int playerID;
	private int x; 
	private int y; 
	private boolean alive;
	private int rank; // From 
	private boolean hidden;
	
	/* Player instantiates unit. The Player class checks to make
	 * sure that no two units are in the same location, and that there aren't
	 * too many units with the same rank
	 */
	public Unit(int playerID, int x, int y, int rank){
		this.playerID = playerID;
		this.x = x;
		this.y = y;
		this.rank = rank;
		this.alive = true;
		this.hidden = true;
	}
	
	//NOTE: Move this action to the GameState class
	// Be sure to update occupancyMap as well
/*	public void performAction(int action){
		if(action==UnitAction.forward){
			this.up();
		}
		if(action==UnitAction.backward){
			this.down();
		}
		if(action==UnitAction.right){
			this.right();
		}
		if(action==UnitAction.left){
			this.left();
		}
	}
*/
	
	//TODO See if y++ is up or down
	public void up(){
		y++;
	}
	
	public void down(){
		y--;
	}
	
	public void right(){
		x++;
	}
	
	public void left(){
		x--;
	}
	
	public void specialMove(int rank){
		//depending on the rank, the action would be choosen. 
		if(rank<4)
		{
			
		}
		
		
	}
	/*
	 * 
	 * 	private int playerID;
	private int x; 
	private int y; 
	private boolean alive;
	private int rank; 

	 */
	public int getPlayerID()
	{
		return playerID;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x){
		this.x = x;
	}
	
	public int getY()
	{
		return y; 
	}
	public void setY(int y){
		this.y = y;
	}
	
	public int getRank()
	{
		return rank; 
	}
	
	public boolean getAlive()
	{
		return alive;
	}
	
	public void setAlive(boolean alive)
	{
		this.alive = alive;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	
}