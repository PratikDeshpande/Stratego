package game_engine;

import java.util.ArrayList;


/*
 * This is the GameState class
 */
public class GameState {

	// Define Ranks here
	public static final int MARSHALL = 1;
	public static final int GENERAL = 2;
	public static final int COLONEL = 3;
	public static final int MAJOR = 4;
	public static final int CAPTAIN = 5;
	public static final int LIEUTENANT = 6;
	public static final int SERGEANT = 7;

	// Special Characters
	public static final int MINER = 8;
	public static final int SCOUT = 9;
	public static final int SPY = 10;
	public static final int BOMB = 11;
	public static final int FLAG = 12;

	// Number of each units available to each side at initialization
	public static final int NUM_MARSHALL = 1;
	public static final int NUM_GENERAL = 1;
	public static final int NUM_COLONEL = 2;
	public static final int NUM_MAJOR = 3;
	public static final int NUM_CAPTAIN = 4;
	public static final int NUM_LIEUTENANT = 4;
	public static final int NUM_SERGEANT = 4;
	public static final int NUM_MINER = 5;
	public static final int NUM_SCOUT = 8;
	public static final int NUM_SPY = 1;
	public static final int NUM_BOMB = 6;
	public static final int NUM_FLAG = 1;



	// This is the terrain of Stratego. 0 is traversible and 1 is not
	private int[] map =    {0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,1,1,0,0,1,1,0,0, 
				0,0,1,1,0,0,1,1,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0 };
	
	// This is used to find legal actions. Shows which spot of the map is occupied
	private int[] occupancyMap = new int[100]; // 0: empty, 1: player 1, 2: player 2 is occupying

	
	// List of all units on the map
	private ArrayList<Unit> units;
	
	// The two AI agents who are playing the game 
	private Player player1,player2;



	//Initialize everything. 
	public GameState(Player player1, Player player2){
		// Initialize the 2 players
		this.player1 = player1;
		this.player2 = player2;
		
		// Place the units on the map according the players' initial positions
		this.units = new ArrayList<Unit>();
		this.units.addAll(player1.getUnits());
		this.units.addAll(player2.getUnits());
		
		// Fill the occupancy map
		
		// First fill it with all 0s
		for(int i:occupancyMap){
			i = 0;
		}
		// Now mark spots occupied with Player 1's Units with a '1'
		// and mark spots occupied with Player 2's Units with a '2'
		// TODO Implement raster function to make the index calculation less error prone
		for(Unit u:this.units){
			if(u.getPlayerID()==1){
				int index = (9-u.getX())+ (10*u.getY());
			//	System.out.println(index);
				occupancyMap[index] = 1;
			}
			if(u.getPlayerID()==2){
				int index = u.getX() + (10*(9-u.getY()));
				//System.out.println(index);
				occupancyMap[index] = 2;
			}
		}

		
		
		//System.out.println(this.units.get(0).getPlayerID()); // This 0 when it should be 1 or 2
	//	System.out.println("num legal actions 1: " + this.getLegalActions(1).size());
	//	System.out.println("num legal actions 2: " + this.getLegalActions(2).size());
		
		// Once instantuated, keep running Update (wait a few seconds)
	/*	while(true){
			this.update();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} */
		this.update();
		this.update();
		this.update();

	}
	
	// Returns the Unit's index location on map.
	public int getUnitIndex(Unit u){
		//int raster = 
		if(u.getPlayerID()==1){
			return ((9-u.getX())+(u.getY()*10));
		}
		if(u.getPlayerID()==2){
			return  (u.getX()+((9-u.getY())*10));
		}
		
		return -1;
	}
	
	// Performs a UnitAction
	public void performAction(UnitAction action){
		
		if(action.getAction()==UnitAction.forward){
			
			//occupancyMap[]
			action.getUnit().up();
		}
		
		/*
		if(action==UnitAction.backward){
			this.down();
		}
		if(action==UnitAction.right){
			this.right();
		}
		if(action==UnitAction.left){
			this.left();
		} */
	}
	
	// Retrieves moves from both players for one turn and updates GameState based on those moves.  
	public void update()
	{
		
	//	UnitAction p1Action = this.getLegalActions(1).get(0); // For Testing Purposes
		
		// Retrieve Player 1's Next Move.
		UnitAction p1Action = player1.nextMove(this);

		// Implement Player 1's Move.
		
			// Loops through Units to find Unit that performs move. 
		for(Unit u:this.units){
			
			if(u.equals(p1Action.getUnit())){
				
				// Implement the action
				// TODO: occupancy Map changes should be encapsulated in PerformAction()
				// 1. Clear the occupancy map so that the Unit's original spot is not unoccupied
			//	this.occupancyMap[(9-u.getX())+(u.getY()*10)]=0; // TODO: Use the raster function instead
				this.occupancyMap[this.getUnitIndex(u)] = 0;
				// 2. Perform the action
				u.performAction(p1Action.getAction()); // TODO: GameState should have a performAction, not Unit
				// 3. Mark the Unit's new spot with '1' to say that it is occupied by Player 1's unit
				this.occupancyMap[(9-u.getX())+(u.getY()*10)]=1; // TODO: Use raster function
				
				// raster function: Gives index given player ID, x, and y
				//int U// playerID, x, y;
				
				
				
				
				
			}
		}
		
		//	UnitAction p2Action = this.getLegalActions(2).get(0);

		// Retrieve Player 2's Next Move
		UnitAction p2Action = player2.nextMove(this);

		// Implement Player 2's Next Move.
		
			// Loops through Units to find Unit that performs move.
		for(Unit u:this.units){
			if(u.equals(p2Action.getUnit())){
				
				// Implement the action
				this.occupancyMap[u.getX()+((9-u.getY())*10)]=0; // TODO: Use raster function
				u.performAction(p2Action.getAction()); // TODO: Use gameState's performAction function
				// Change occupancyMap
				this.occupancyMap[u.getX()+((9-u.getY())*10)]=1; // TODO: Use raster function
				
				
			}
		}
		// Find the Unit from p1Action
	/*	for(Unit u:this.units){
			if(u.equals(p1Action.getUnit())){
			//	System.out.println("Found Same Unit");
			//Implement the action
				this.occupancyMap[(9-u.getX())+(u.getY()*10)]=0;
				u.performAction(p1Action.getAction()); // Change occ map
				// Change occupancyMap
				this.occupancyMap[(9-u.getX())+(u.getY()*10)]=1;
				
			}
			if(u.equals(p2Action.getUnit())){
				//	System.out.println("Found Same Unit");
				//Implement the action
					this.occupancyMap[u.getX()+((9-u.getY())*10)]=0;
					u.performAction(p2Action.getAction()); // Change occ map
					// Change occupancyMap
					this.occupancyMap[u.getX()+((9-u.getY())*10)]=1;
					
				}
			
		} */
		
	}
	
	public int[] getMap()
	{
		return this.map;
	}
	
	// Gives all legal actions available to player
	public ArrayList<UnitAction> getLegalActions(int playerID){
		
		ArrayList<UnitAction> legalActions = new ArrayList<UnitAction>();
		// Cycle through all of the player's units and find legal actions
		for(Unit u:this.units){
			int x = u.getX();
			int y = u.getY();
			
			if(u.getPlayerID()==playerID){
				
				// 
				
				// If the player is a regular player, it can move to any adjacnet unoccupied spot
				if((u.getRank()<=8)||(u.getRank()==10)){ 
				//	System.out.println("unit: "+ u.getRank());

					int left,forward,right,backward;
					
					// Calculates the raster value for the adjacent spots (used for occupancyArray
					if(playerID==1){
						left = (y*10)+(9-x+1);
						forward = ((y+1)*10)+(9-x);
						right = (y*10)+(9-x-1);
						backward = ((y-1)*10)+(9-x);
					}
					else{
						left = ((9-y)*10)+(x-1);
						forward = ((9-y-1)*10)+x;
						right = ((9-y)*10)+(x+1);
						backward = ((9-y+1)*10)+x;
					}
					
					// Find out if u is on the edge
					if(x==0){
						left = -1;
					}
					if(x==9){
						right = -1;
					}
					if(y==0){
						backward = -1;
					}
					if(y==9){
						forward = -1;
					}
					
				/*	System.out.println("forward: "+forward);
					System.out.println("right: "+right);
					System.out.println("left: "+left);
					System.out.println("backward: "+backward); */

			
					// Check the occupancy array to see if adjacent spot is occupied
					// Reject if left is off the edge of the map OR
					// if left is occupied by unit 
					if((left!=-1)&&(occupancyMap[left]==0)&&(map[left]==0)){
						legalActions.add(new UnitAction(u, UnitAction.left));
					//	System.out.println("Added left");
					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((right!=-1)&&(occupancyMap[right]==0)&&(map[right]==0)){
						legalActions.add(new UnitAction(u, UnitAction.right));
					//	System.out.println("Added right");

					}
					// Reject if left is off the edge of the map OR
					// if left is occupied by unit 
					if((forward!=-1)&&(occupancyMap[forward]==0)&&(map[forward]==0)){
						legalActions.add(new UnitAction(u, UnitAction.forward));
					//	System.out.println("Added forward");

					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((backward!=-1)&&(occupancyMap[backward]==0)&&(map[backward]==0)){
						legalActions.add(new UnitAction(u, UnitAction.backward));
					//	System.out.println("Added backward");

					}
					 
					
				}
				
			}
		}
		return legalActions;
	}

	// Gives all enemy units (some may be hidden)
	public ArrayList<Unit> getEnemyUnits(int playerID){
		return null;
	}
	
	
	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}

	
	//getEnemyUnits 
}
