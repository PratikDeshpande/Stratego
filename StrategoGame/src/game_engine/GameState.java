package game_engine;

import java.util.ArrayList;
import java.util.HashMap;


/*
 * This is the GameState class
 */
public class GameState {

	// Define Ranks here
	public static final int MARSHAL = 1;
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
	public static final int NUM_MARSHAL = 1;
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

	public boolean gameOver;
	public int winningPlayer;
	
	public static int unitIDs;

	// This is the terrain of Stratego. 0 is traversible and 1 is not
	private final int[] map = 
			{   0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0, 
				0,0,0,0,0,0,0,0,0,0,
				0,0,1,1,0,0,1,1,0,0, // For testing we are removing the lakes in the middle
				0,0,1,1,0,0,1,1,0,0,
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
	//These are the gamestate objects that will be passed to each respective player
	// This is done because each player can observe different things in the environment 
	private PlayerGameState player1State, player2State;

	// Keeps track of all events in the game
	private ArrayList<GameEvent> gameEvents;


	//Initialize everything. 
	public GameState(Player player1, Player player2){
		// Game will keep going until gameOver is true. gameOver should become true when
		// 1. A player captures a flag OR
		// 2. When a player has No Legal Actions //TODO
		GameState.unitIDs = 0;
		
		this.gameOver = false;
		
		// Initialize the 2 players
		this.player1 = player1;
		this.player2 = player2;
		
		// Initialize the Players' Game States
		this.player1State = new PlayerGameState(this,this.player1);
		this.player2State = new PlayerGameState(this,this.player2);
		
		// Place the units on the map according the players' initial positions
		this.units = new ArrayList<Unit>();
		this.units.addAll(player1.getUnits());
		this.units.addAll(player2.getUnits());
		
		// Populate the Navigation Map with units.
		this.navigationMap = new HashMap<Integer,MapNode>();//new ArrayList<MapNode>();
		for( int i=0;i<map.length;i++){
			
			if(map[i]==0){
				this.navigationMap.put(new Integer(i), new MapNode(true));
			}
			else{
				this.navigationMap.put(new Integer(i),new MapNode(false));
			}
		}
		for(Unit u:this.units){
			
			int index = this.getUnitIndex(u);
			this.navigationMap.get(new Integer(index)).setOccupyingUnit(u);
		}
				
		// Initialize GameEvents list. This will keep track of all the events in the game
		gameEvents = new ArrayList<GameEvent>();
				
	}
	
	// Set's the unit's x and y coordinates to the given int tile target location TODO test edge cases, verify
	private void setTargetLocation(Unit u, int tile){
		int x,y;
		if(u.getPlayerID()==1){
			x = 9-(tile%10); // 9-
			y = tile/10;		
		}
		else{
			x = tile%10;
			y = 9-(tile/10);
		}
		u.setX(x);
		u.setY(y);
		
	}
	
	// Returns the Unit's index location on map.
	public static int getUnitIndex(Unit u){
		if(u.getPlayerID()==1){
			return ((9-u.getX())+(u.getY()*10));
		}
		if(u.getPlayerID()==2){
			return  (u.getX()+((9-u.getY())*10));
		}
		
		return -1;
	}
	
	// Returns the map index for the grid dx to the right and dy up. Also handles edge cases
	private int getUnitAdjacentIndex(Unit u,int dx, int dy){
		
		int x = u.getX()+dx;
		int y = u.getY()+dy;
		
		

		// IF the the spot dx dy from u is off the edge, return -1
		if((x<0)||(y<0)||(x>9)||(y>9)){
			return -1;
		}
		if(u.getPlayerID()==1){
			return ((9-x)+(y*10));
		}
		if(u.getPlayerID()==2){
			return (x+((9-y)*10));
		}
		
		return -1;
	}
	
	//private int getTileAdjacentIndex(int playerID, int x, int y,)
	
	
	// Modifies the GameState in accordance with the game rules in response to a UnitAction
	private void executeAction(UnitAction action){
		
		//this.setMostRecentAction(action);
		GameEvent recentEvent = new GameEvent(action);
		
		Unit u = action.getUnit();
		int actionPerformed = action.getAction();
		int targetTile = action.getTargetTile();
		
		// Print what the action is so users can know.
	//	System.out.print("Player " + u.getPlayerID() + "'s " + u.getRank() + " at " + this.getUnitIndex(u));
		
		
		
		// This clears the unit's current location. No matter what the outcome, the unit's previous spot will now become unoccupied
		this.navigationMap.get(Integer.valueOf(this.getUnitIndex(u))).setOccupyingUnit(null);

		
		// If the action performed is a MOVE
		if(actionPerformed==UnitAction.move){
	//		System.out.print(" moved to "+ targetTile +".\n");
			// Update unit's location to reflect move. Navigation Map will be updated later in the code
			this.setTargetLocation(u, targetTile); // TODO check edge cases for this function. verify if it works
		}
		
		// If the action performed is an ATTACK
		if(actionPerformed==UnitAction.attack){

			Unit target = action.getTarget();
			// Removes target from map. If the target is unharmed, the code will re-insert the target into the navigation map
		
	//		System.out.print(" attacked Player " + target.getPlayerID() + " 's " + target.getRank() + " at " + targetTile + ".\n" );

			
			this.navigationMap.get(Integer.valueOf(this.getUnitIndex(target))).setOccupyingUnit(null);
			
			//Reveals target
			target.setHidden(false);
			//Reveals unit
			u.setHidden(false);
			// Check the rank of the target unit
			int rank = target.getRank();
			
			// Handle special cases here
			
			// IF the target is a bomb, the unit dies unless the unit is a miner
			if(rank==GameState.BOMB){
				if(u.getRank()==GameState.MINER){
					//Defuse bomb
					target.setAlive(false);
					this.setTargetLocation(u, this.getUnitIndex(target));
				}
				else{
					// Unit dies
					u.setAlive(false);
				}
			}
			// IF target is a flag, then game is over and unit's player has won
			else if(rank==GameState.FLAG){
				// kill flag
				target.setAlive(false);
				this.setTargetLocation(u, this.getUnitIndex(target));
				
				// Unit's player wins
				this.gameOver=true;
				this.winningPlayer = u.getPlayerID();
				System.out.println("GAME OVER!!!\nPlayer "+this.winningPlayer+" has won the match!");
			}
			// IF target is a marshall, the unit dies unless the unit is a spy
			else if(rank==GameState.MARSHAL){
				if(u.getRank()==GameState.SPY){
					// kill marshal
					target.setAlive(false);
					this.setTargetLocation(u, this.getUnitIndex(target));
				}
				else{
					// Unit dies
					u.setAlive(false);
				}
			}
			// IF target is a spy, then unit kills it, unless the unit is a marshall, then the unit dies
		/*	else if(rank==GameState.SPY){
				if(u.getRank()==GameState.MARSHAL){
					// unit dies
					u.setAlive(false);
				}
				else{
					// kill spy
					target.setAlive(false);
					this.setTargetLocation(u, this.getUnitIndex(target));
				}
			} */ // Spy can be killed by marshal
			// IF target has a lower rank value, it is a stronger than the unit, thus the unit dies
			else if(rank<u.getRank()){
				// unit dies
				u.setAlive(false);
			}
			// IF target and unit have same rank value, then they both die
			else if(rank==u.getRank()){
				// both die
				u.setAlive(false);
				target.setAlive(false);
			}
			// IF target has a higher rank value, then it dies
			else if(rank>u.getRank()){
				// target dies
				target.setAlive(false);
				this.setTargetLocation(u, this.getUnitIndex(target));
			}
			
			// IF target is alive, update navigation map with target's location
			if(target.getAlive()){
				this.navigationMap.get(Integer.valueOf(this.getUnitIndex(target))).setOccupyingUnit(target);
			}
			else{
				recentEvent.setTargetDies(true);
			}
			
		}
		
		// IF unit is still alive, update its location on the navigation map. 
		if(u.getAlive()){
			this.navigationMap.get(Integer.valueOf(this.getUnitIndex(u))).setOccupyingUnit(u);
		}
		else{
			recentEvent.setUnitDies(true);
		}

		gameEvents.add(recentEvent);

	}
	
	// Retrieves moves from both players for one turn and updates GameState based on those moves.  
	public void update()
	{
		// For Testing Purposes
	/*	for(UnitAction ua:this.getLegalActions(1)){	
			System.out.println("Player1 LegalAction: "+ua.getAction() + " to " + ua.getTargetTile());	
		} 
		for(UnitAction ua:this.getLegalActions(2)){
			System.out.println("Player2 LegalAction: "+ua.getAction() + " to " + ua.getTargetTile());
		} */

		
		// Checks to see if Player 1 has any legal moves. If not, player 1 loses.
		if(this.getLegalActions(player1.getPlayerID()).isEmpty()){
			this.gameOver=true;
			this.winningPlayer = player2.getPlayerID();			
			System.out.println("GAME OVER!!!\nPlayer "+this.winningPlayer+" has won the match!");
			return;
		}
		else{		
			// Retrieve Player 1's Next Move.
			UnitAction p1Action = player1.nextMove(this.player1State);
		//	UnitAction p1Action = this.getLegalActions(player1.playerID).get(0); // For Testing Purposes
	
			// Implement Player 1's Move.
			
				// Loops through Units to find Unit that performs move. 
			for(Unit u:this.units){
				
				if(u.equals(p1Action.getUnit())){
					
					// execute the action
					this.executeAction(p1Action);				
					
					
				}
			}
		}
		
		//	UnitAction p2Action = this.getLegalActions(2).get(0);

		// Checks to see if Player 2 has any legal moves. If not, player 2 loses.
		if(this.getLegalActions(player2.getPlayerID()).isEmpty()){
			this.gameOver=true;
			this.winningPlayer = player1.getPlayerID();			
			System.out.println("GAME OVER!!!\nPlayer "+this.winningPlayer+" has won the match!");
			return;
		}
		else{
			// Retrieve Player 2's Next Move
			UnitAction p2Action = player2.nextMove(this.player2State);
	
			// Implement Player 2's Next Move.
			
				// Loops through Units to find Unit that performs move.
			for(Unit u:this.units){
				if(u.equals(p2Action.getUnit())){
					
					// execute the action
					this.executeAction(p2Action);
					
					
				}
		}
		}
		

		
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
						
			// Checks to see if unit belongs to player
			if(u.getPlayerID()==playerID){
				
				
				// If the unit is a regular unit, it can move to any adjacent spot
				// If the unit is a scout, it can move in any direction until it hits an obstruction
				
				// Checks to see if unit is a regular unit (including miner)
				//TODO Change Gamestate.SCOUT back to GameState.MINER
				if((u.getRank()<=GameState.SCOUT)||(u.getRank()==GameState.SPY)){ 
				//	System.out.println("unit: "+ u.getRank());

					// Will store the adjacent index values
					int left,forward,right,backward;
					
					// Calculates the index values for the adjacent spots (left,forward,right,backward)
					left = this.getUnitAdjacentIndex(u,-1,0);
					forward = this.getUnitAdjacentIndex(u, 0, 1);
					right = this.getUnitAdjacentIndex(u, 1, 0);
					backward = this.getUnitAdjacentIndex(u, 0, -1);
					// NOTE: Did edge case testing on getUnitAdjacentIndex. It works!
					
					
					// For testing purposes
				/*	System.out.println("forward: "+forward);
					System.out.println("right: "+right);
					System.out.println("left: "+left);
					System.out.println("backward: "+backward); */

			
					// Reject if left is off the edge of the map OR
					// if left is occupied by unit OR
					// if left is not traversible
					if((left!=-1)&&(!this.navigationMap.get(Integer.valueOf(left)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(left)).isTraversible())){
						legalActions.add(new UnitAction(u, UnitAction.move, left));
					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((right!=-1)&&(!this.navigationMap.get(Integer.valueOf(right)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(right)).isTraversible())){
						legalActions.add(new UnitAction(u, UnitAction.move, right));

					}
					// Reject if left is off the edge of the map OR
					// if left is occupied by unit 
					if((forward!=-1)&&(!this.navigationMap.get(Integer.valueOf(forward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(forward)).isTraversible())){
						legalActions.add(new UnitAction(u, UnitAction.move, forward));

					}
					// Reject if right is off the edge of the map OR
					// if right is occupied by unit
					if((backward!=-1)&&(!this.navigationMap.get(Integer.valueOf(backward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(backward)).isTraversible())){
						legalActions.add(new UnitAction(u, UnitAction.move, backward));
					}
					
					// These are attack actions. Here we check to see if the adjacent spot on the
					// map is occupied by the enemy unit.
					
					// Reject if left is off the edge of the map OR
					// if left is not occupied OR
					// if left is occupied by friendly unit OR
					// if left is not passable
					if((left!=-1)&&(this.navigationMap.get(Integer.valueOf(left)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(left)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(left)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(left)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.attack,left,target)); // left and target will ALWAYS be same tile
					//	System.out.println("Added left attack");
					}
					// Reject if right is off the edge of the map OR
					// if right is not occupied OR
					// if right is occupied by friendly unit OR
					// if right is not passable
					if((right!=-1)&&(this.navigationMap.get(Integer.valueOf(right)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(right)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(right)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(right)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.attack,right,target));
					//	System.out.println("Added right attack");
					}
					// Reject if forward is off the edge of the map OR
					// if forward is not occupied OR
					// if forward is occupied by friendly unit OR
					// if forward is not passable
					if((forward!=-1)&&(this.navigationMap.get(Integer.valueOf(forward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(forward)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(forward)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(forward)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.attack,forward,target));
					//	System.out.println("Added forward attack");
					}
					// Reject if backward is off the edge of the map OR
					// if backward is not occupied OR
					// if backward is occupied by friendly unit OR
					// if backward is not passable
					if((backward!=-1)&&(this.navigationMap.get(Integer.valueOf(backward)).isOccupied())&&(this.navigationMap.get(Integer.valueOf(backward)).getOccupyingUnit().getPlayerID()!=u.getPlayerID())&&(this.navigationMap.get(Integer.valueOf(backward)).isTraversible())){
						Unit target = this.navigationMap.get(Integer.valueOf(backward)).getOccupyingUnit();
						legalActions.add(new UnitAction(u, UnitAction.attack,backward,target));
					//	System.out.println("Added backward attack");
					}
					
				}
				
				// IF unit is a Scout //TODO Change back to GameState.SCOUT
				if(u.getRank()==100){
					
					// In all 4 directions, go until it runs into
					//	1. Edge OR
					//	2. Enemy Unit OR
					//	3. Non Traversible Terrain 
					
					// Will store the adjacent index values
				/*	ArrayList<Integer> lefts = new ArrayList<Integer>();
					ArrayList<Integer> rights = new ArrayList<Integer>();
					ArrayList<Integer> ups = new ArrayList<Integer>();
					ArrayList<Integer> downs = new ArrayList<Integer>(); */
					
					// Finds the adjacent spots in respective directions.
					// Stop when you
					// 1. reach the edge of map OR
					// 2. reach impassable terrain OR
					// 3. reach a friendly unit OR
					// 4. reach an enemy unit (but add it to arraylist)
					int dx, dy;
					
					// Left
					dx = -1;
					dy = 0;
					int immLeft = this.getUnitAdjacentIndex(u, dx, dy);
					// While relevant tile is not off the map
					while((immLeft>-1)){
						
						//if adjacent tile is impassable then break
						if(!this.navigationMap.get(Integer.valueOf(immLeft)).isTraversible()){
							break;
						}
						
						// if the tile is occupied by friendly unit, then break
						if(this.navigationMap.get(Integer.valueOf(immLeft)).isOccupied()){
							if(this.navigationMap.get(Integer.valueOf(immLeft)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								break;
							}
							// if the tile is occupied by an enemy unit, add an attack action and THEN break
							if(this.navigationMap.get(Integer.valueOf(immLeft)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								Unit target = this.navigationMap.get(Integer.valueOf(immLeft)).getOccupyingUnit();
								legalActions.add(new UnitAction(u, UnitAction.attack,immLeft,target));
								
								break;
							}
						} 						
						legalActions.add(new UnitAction(u, UnitAction.move, immLeft));
						dx=dx-1; // move left one
						immLeft = this.getUnitAdjacentIndex(u, dx, dy);					
					}
					
					
					
					// Right
					dx = 1;
					dy = 0;
					int immRight = this.getUnitAdjacentIndex(u, dx, dy);
					// While relevant tile is not off the map
					while((immRight>-1)){
						
						//if adjacent tile is impassable then break
						if(!this.navigationMap.get(Integer.valueOf(immRight)).isTraversible()){
							break;
						}
						
						// if the tile is occupied by friendly unit, then break
						if(this.navigationMap.get(Integer.valueOf(immRight)).isOccupied()){
							if(this.navigationMap.get(Integer.valueOf(immRight)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								break;
							}
							// if the tile is occupied by an enemy unit, add an attack action and THEN break
							if(this.navigationMap.get(Integer.valueOf(immRight)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								Unit target = this.navigationMap.get(Integer.valueOf(immRight)).getOccupyingUnit();
								legalActions.add(new UnitAction(u, UnitAction.attack,immRight,target));
								
								break;
							}
						} 						

						legalActions.add(new UnitAction(u, UnitAction.move, immRight));
						dx=dx+1; // move right one
						immRight = this.getUnitAdjacentIndex(u, dx, dy);					
					}
					
					
					// Up
					dx = 0;
					dy = 1;
					int immUp = this.getUnitAdjacentIndex(u, dx, dy);
					// While relevant tile is not off the map
					while((immUp>-1)){
						
						//if adjacent tile is impassable then break
						if(!this.navigationMap.get(Integer.valueOf(immUp)).isTraversible()){
							break;
						}
						
						// if the tile is occupied by friendly unit, then break
						if(this.navigationMap.get(Integer.valueOf(immUp)).isOccupied()){
							if(this.navigationMap.get(Integer.valueOf(immUp)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								break;
							}
							// if the tile is occupied by an enemy unit, add an attack action and THEN break
							if(this.navigationMap.get(Integer.valueOf(immUp)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								Unit target = this.navigationMap.get(Integer.valueOf(immUp)).getOccupyingUnit();
								legalActions.add(new UnitAction(u, UnitAction.attack,immUp,target));
								
								break;
							}
						} 						

						legalActions.add(new UnitAction(u, UnitAction.move, immUp));
						dy=dy+1; // move up one
						immUp = this.getUnitAdjacentIndex(u, dx, dy);					
					}
					
					// Down
					dx = 0;
					dy = -1;
					int immDown = this.getUnitAdjacentIndex(u, dx, dy);
					// While relevant tile is not off the map
					while((immDown>-1)){
						
						//if adjacent tile is impassable then break
						if(!this.navigationMap.get(Integer.valueOf(immDown)).isTraversible()){
							break;
						}
						
						// if the tile is occupied by friendly unit, then break
						if(this.navigationMap.get(Integer.valueOf(immDown)).isOccupied()){
							if(this.navigationMap.get(Integer.valueOf(immDown)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								break;
							}
							// if the tile is occupied by an enemy unit, add an attack action and THEN break
							if(this.navigationMap.get(Integer.valueOf(immDown)).getOccupyingUnit().getPlayerID()==u.getPlayerID()){
								Unit target = this.navigationMap.get(Integer.valueOf(immDown)).getOccupyingUnit();
								legalActions.add(new UnitAction(u, UnitAction.attack,immDown,target));
								
								break;
							}
						} 						

						legalActions.add(new UnitAction(u, UnitAction.move, immDown));
						dy=dy-1; // move up one
						immDown = this.getUnitAdjacentIndex(u, dx, dy);					
					}
					
					
					
				}
				
			}
		}
		
		
		return legalActions;
	}

	// Gives all enemy units (some may be hidden)
	public ArrayList<Unit> getEnemyUnits(int playerID){
		ArrayList<Unit> enemyUnits = new ArrayList<Unit>();
		for(Unit u:this.getUnits()){
			if(u.getPlayerID()!=playerID){
				enemyUnits.add(u);
			}
		}
		return enemyUnits;
	}
	
	public ArrayList<Unit> getMyUnits(int playerID){
		ArrayList<Unit> myUnits = new ArrayList<Unit>();
		for(Unit u:this.getUnits()){
			if(u.getPlayerID()==playerID){
				myUnits.add(u);
			}
		}
		return myUnits;
	}
	
	
	public ArrayList<Unit> getUnits() {
		return units;
	}

	private void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}


	public ArrayList<GameEvent> getGameEvents() {
		return gameEvents;
	}

	public void setGameEvents(ArrayList<GameEvent> gameEvents) {
		this.gameEvents = gameEvents;
	}

	
	//getEnemyUnits 
	
	/*private class MapNode{
		
		public MapNode()
		
	}*/
	
}
