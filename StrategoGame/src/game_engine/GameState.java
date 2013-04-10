package game_engine;

import java.util.ArrayList;
import java.util.HashMap;


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
	private final int[] map =    {0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, // For testing we are removing the lakes in the middle
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0 };
	
	//private ArrayList<MapNode> navigationMap;
	private HashMap<Integer,MapNode> navigationMap;
	
	
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
		
		// Populate the Navigation Map with units.
		this.navigationMap = new HashMap<Integer,MapNode>();//new ArrayList<MapNode>();
		for( int i=0;i<map.length;i++){
			
			if(map[i]==0){
				this.navigationMap.put(new Integer(i), new MapNode(true));
			//	this.navigationMap.add(new MapNode(i,true));
			}
			else{
				this.navigationMap.put(new Integer(i),new MapNode(false));
			}
		}
		//System.out.println("navMap size: " + this.navigationMap.size() );
		for(Unit u:this.units){
			
			int index = this.getUnitIndex(u);
			this.navigationMap.get(new Integer(index)).setOccupyingUnit(u);
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
				
				e.printStackTrace();
			} 
		} */
		this.update();
	/*	for(int i=0;i<100;i++){
		
			if(this.navigationMap.get(Integer.valueOf(i)).getOccupyingUnit()==null){
				System.out.println(i + ": null");
			}
			else{
				System.out.println(i +" is occupied ");		
			}
		} */
	//	this.update();
	//	this.update();

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
		
		Unit u = action.getUnit();
		int actionPerformed = action.getAction();
		
		this.navigationMap.get(Integer.valueOf(this.getUnitIndex(u))).setOccupyingUnit(null);

		// If regular motion. Already checked edge cases
		if(actionPerformed==UnitAction.forward){
			u.up();
		}
		if(actionPerformed==UnitAction.backward){
			u.down();
		}
		if(actionPerformed==UnitAction.right){
			u.right();
		}
		if(actionPerformed==UnitAction.left){
			u.left();
		}
		

		
		// If regular attack action
		if((actionPerformed==UnitAction.left_attack)||(actionPerformed==UnitAction.right_attack)||(actionPerformed==UnitAction.forward_attack)||(actionPerformed==UnitAction.backward_attack)){
			
			Unit target = action.getTarget();
			// Removes target from map
			this.navigationMap.get(Integer.valueOf(this.getUnitIndex(target))).setOccupyingUnit(null);
			
			//Unhides target
			target.setHidden(false);
			//Unhides unit
			u.setHidden(false);
			// Check the rank of the target unit
			int rank = target.getRank();
			//TODO include logic for special units
			// Target is higher ranked, so attack fails and unit dies
			if(rank>u.getRank()){
				System.out.println("Target rank is higher");
				// target stays in same location, unit dies
				u.setAlive(false);
				// Removes unit from map [nvm, unit is already removed
				//this.navigationMap.get(Integer.valueOf(i))
			}
			if(rank==u.getRank()){
				System.out.println("Target rank is equal");
				u.setAlive(false);
				target.setAlive(false);
			}
			if(rank<u.getRank()){
				// If target is lower ranked than unit
				System.out.println("Target rank is lower");
				
				target.setAlive(false);

				if(actionPerformed==UnitAction.left_attack){
					u.left();
				}
				if(actionPerformed==UnitAction.right_attack){
					u.right();
				}
				if(actionPerformed==UnitAction.forward_attack){
					u.up();
				}
				if(actionPerformed==UnitAction.backward_attack){
					u.down();
				}
			}
			
			if(target.getAlive()){
				this.navigationMap.get(Integer.valueOf(this.getUnitIndex(target))).setOccupyingUnit(target);
			}
			
		}
		
		// IF it is still alive, if not, it has already been cleared from map. just don't put it back on the map
		if(u.getAlive()){
			this.navigationMap.get(Integer.valueOf(this.getUnitIndex(u))).setOccupyingUnit(u);
		}

		


	}
	
	// Retrieves moves from both players for one turn and updates GameState based on those moves.  
	public void update()
	{
		// For Testing Purposes
		for(UnitAction ua:this.getLegalActions(1)){	
			System.out.println("Player1 LegalAction: "+ua.getAction());	
		}
		for(UnitAction ua:this.getLegalActions(2)){
			System.out.println("Player2 LegalAction: "+ua.getAction());
		}

		
		
		// Retrieve Player 1's Next Move.
	//	UnitAction p1Action = player1.nextMove(this);
		UnitAction p1Action = this.getLegalActions(player1.playerID).get(0); // For Testing Purposes

		// Implement Player 1's Move.
		
			// Loops through Units to find Unit that performs move. 
		for(Unit u:this.units){
			
			if(u.equals(p1Action.getUnit())){
				
				// Perform the action
				this.performAction(p1Action);				
				
				
			}
		}
		/*
		//	UnitAction p2Action = this.getLegalActions(2).get(0);

		// Retrieve Player 2's Next Move
		UnitAction p2Action = player2.nextMove(this);

		// Implement Player 2's Next Move.
		
			// Loops through Units to find Unit that performs move.
		for(Unit u:this.units){
			if(u.equals(p2Action.getUnit())){
				
				// Perform the action
				this.performAction(p2Action);
				
				
			}
		}
		*/

		
	}
	
	// TODO: Make it return an array that represents occupied blocks as well
	public int[] getMap()
	{
		return this.map;
	}
	
	// Gives all legal actions available to player
	public ArrayList<UnitAction> getLegalActions(int playerID){
		
		// This ArrayList will contain all the legal actions available to player
		ArrayList<UnitAction> legalActions = new ArrayList<UnitAction>();
		
		// Cycle through all of the player's units and find legal actions
		for(Unit u:this.units){
			
			int x = u.getX();
			int y = u.getY();
			
			// Checks to see if unit belongs to player
			if(u.getPlayerID()==playerID){
				
				// 
				
				// If the unit is a regular unit, it can move to any adjacent  spot
				
				// Checks to see if unit is a regular unit
				if((u.getRank()<=8)||(u.getRank()==10)){ 
				//	System.out.println("unit: "+ u.getRank());

					// Will store the adjacent index values
					int left,forward,right,backward;
					
					// Calculates the raster value for the adjacent spots (used for occupancyArray
					// TODO: Use raster function
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
					if((left!=-1)&&(!this.navigationMap.get(Integer.valueOf(left)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(left)).isTraversible())){
		//				legalActions.add(new UnitAction(u, UnitAction.left));
					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((right!=-1)&&(!this.navigationMap.get(Integer.valueOf(right)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(right)).isTraversible())){
		//				legalActions.add(new UnitAction(u, UnitAction.right));

					}
					// Reject if left is off the edge of the map OR
					// if left is occupied by unit 
					if((forward!=-1)&&(!this.navigationMap.get(Integer.valueOf(forward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(forward)).isTraversible())){
		//				legalActions.add(new UnitAction(u, UnitAction.forward));

					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((backward!=-1)&&(!this.navigationMap.get(Integer.valueOf(backward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(backward)).isTraversible())){
		//				legalActions.add(new UnitAction(u, UnitAction.backward));
					}
					
					// These are attack actions. Here we check to see if the adjacent spot on the
					// map is occupied by the enemy unit.
					
					// Reject if left is off the edge of the map OR
					// if left is not occupied OR
					// if left is occupied by friendly unit OR
					// if left is not passable
					if((left!=-1)&&(this.navigationMap.get(Integer.valueOf(left)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(left)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(left)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(left)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.left_attack,target));
						System.out.println("Added left attack");
					}
					// Reject if right is off the edge of the map OR
					// if right is not occupied OR
					// if right is occupied by friendly unit OR
					// if right is not passable
					if((right!=-1)&&(this.navigationMap.get(Integer.valueOf(right)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(right)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(right)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(right)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.right_attack,target));
						System.out.println("Added right attack");
					}
					// Reject if forward is off the edge of the map OR
					// if forward is not occupied OR
					// if forward is occupied by friendly unit OR
					// if forward is not passable
					if((forward!=-1)&&(this.navigationMap.get(Integer.valueOf(forward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(forward)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(forward)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(forward)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.forward_attack,target));
						System.out.println("Added forward attack");
					}
					// Reject if backward is off the edge of the map OR
					// if backward is not occupied OR
					// if backward is occupied by friendly unit OR
					// if backward is not passable
					if((backward!=-1)&&(this.navigationMap.get(Integer.valueOf(backward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(backward)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(backward)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(backward)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.backward_attack,target));
						System.out.println("Added backward attack");
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
	
	/*private class MapNode{
		
		public MapNode()
		
	}*/
	
}
