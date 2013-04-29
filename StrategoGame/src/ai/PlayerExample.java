package ai;

import game_engine.GameEvent;
import game_engine.GameState;
import game_engine.Player;
import game_engine.PlayerGameState;
import game_engine.PlayerUnit;
import game_engine.Unit;
import game_engine.UnitAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class PlayerExample extends Player{
	
	// This probability model stores the alive enemy units and their probability assignments
	public HashMap<Integer,ProbabilisticUnit> probabilityModel;
	
	// Keeps track of how many enemy players you have killed
	int[] killed; // Indices correspond to GameState. ranks. Leave first index blank
	int[] unidentified; // Indices correspond to GameState ranks. Leave first index blank

	
	//State Constants
	public static final int LOW_KNOWLEDGE= 0;
	public static final int MODERATE_KNOWLEDGE= 1;
	public static final int LOW_POWER= 0;
	public static final int EQUAL_POWER= 1;
	public static final int HIGH_POWER= 2;

	// Keeps track of which state you are in
	int knowledgeState; // 0: low; 1: moderate
	int powerState;	// 0: low; 1: equal; 2: high
	
	
	
	public PlayerExample(int playerID){
		super(playerID);
		
		
		
	}
	
	
	public void initializePos() {
		// TODO Auto-generated method stub
		// TODO Make sure there are a correct number of players
		// Unit(playerID, x, y, rank
		this.getUnits().add(new Unit(playerID,0, 0, 7)); //  sergeant
		this.getUnits().add(new Unit(playerID,1, 0, 11)); //   bomb
		this.getUnits().add(new Unit(playerID,2, 0, 5)); //    captain
		this.getUnits().add(new Unit(playerID,3, 0, 8)); //    miner
		
		this.getUnits().add(new Unit(playerID,4, 0, 5)); //   captain 
		this.getUnits().add(new Unit(playerID,5, 0, 8)); //     miner
		this.getUnits().add(new Unit(playerID,6, 0, 8)); //    miner
		this.getUnits().add(new Unit(playerID,7, 0, 4)); //    major
		
		this.getUnits().add(new Unit(playerID,8, 0, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,9, 0, 12)); //    flag
		this.getUnits().add(new Unit(playerID,0, 1, 11)); //    bomb 	
		this.getUnits().add(new Unit(playerID,1, 1, 9)); //    scout	
		
		// For testing
	//	this.getUnits().add(new Unit(playerID,9, 0, 9)); //    scout
	//	this.getUnits().add(new Unit(playerID,5, 5, 11)); // bomb

		this.getUnits().add(new Unit(playerID,2, 1, 5)); //    captain
		this.getUnits().add(new Unit(playerID,3, 1, 5)); //    captain
		this.getUnits().add(new Unit(playerID,4, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 1, 9)); //    scout
	
		this.getUnits().add(new Unit(playerID,6, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,7, 1, 9)); //    scout
		this.getUnits().add(new Unit(playerID,8, 1, 4)); //    major
		this.getUnits().add(new Unit(playerID,9, 1, 11)); //    bomb
		
		this.getUnits().add(new Unit(playerID,0, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,1, 2, 3)); //    colonel 
		this.getUnits().add(new Unit(playerID,2, 2, 10 )); //    spy
		this.getUnits().add(new Unit(playerID,3, 2, 8)); //    miner 
			
		this.getUnits().add(new Unit(playerID,4, 2, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 2, 9)); //    scout 
		this.getUnits().add(new Unit(playerID,6, 2, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,7, 2, 8)); //    miner
		
		this.getUnits().add(new Unit(playerID,8, 2, 3)); //    colonel
		this.getUnits().add(new Unit(playerID,9, 2, 7)); //    sergeant
		this.getUnits().add(new Unit(playerID,0, 3, 11)); //    bomb
		this.getUnits().add(new Unit(playerID,1, 3, 7)); //    sergeant
	
		this.getUnits().add(new Unit(playerID,2, 3, 2)); //    general
		this.getUnits().add(new Unit(playerID,3, 3, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,4, 3, 9)); //    scout
		this.getUnits().add(new Unit(playerID,5, 3, 6)); //    lieutenant
		
		this.getUnits().add(new Unit(playerID,6, 3, 4)); //    major
		this.getUnits().add(new Unit(playerID,7, 3, 1)); //    marshal
		this.getUnits().add(new Unit(playerID,8, 3, 6)); //    lieutenant
		this.getUnits().add(new Unit(playerID,9, 3, 11)); //    bomb  
		

		

	}
	

	public UnitAction nextMove(PlayerGameState gs) {
		// These are all the attributes available to player from GameState.
	
		ArrayList<UnitAction> legalActions = gs.getLegalActions();// All Legal Actions available to the units of this player
		ArrayList<PlayerUnit> enemyUnits = gs.getEnemyUnits();// All of this player's alive enemy units
		ArrayList<PlayerUnit> myUnits = gs.getMyUnits(); // All of the player's alive units
		int[] map = gs.getMap(); 	// Terrain Map of the game world
		ArrayList<GameEvent> gameEvents = gs.getGameEvents(); // All the events that happened in the game
		
		UnitAction selectedAction = null;
		//System.out.println("At this turn, " + this.playerID + " has " + legalActions.size() + " legal Actions. ");
		
		// IF this is the first time this method is called, we will have to initialize the probability model with prior values
		if(!this.initialized){
			
			initialized=true;
			
			// Put yourself in start state (low knowledge, equal power)
			this.knowledgeState = PlayerExample.LOW_KNOWLEDGE;
			this.powerState = PlayerExample.EQUAL_POWER;
			
			// Initialize a probability model for all enemy units. This will be modified at
			// every step using Bayesian filtering
			probabilityModel = new HashMap<Integer,ProbabilisticUnit>();
			
			for(PlayerUnit pu:gs.getEnemyUnits()){
				probabilityModel.put(pu.getID(), new ProbabilisticUnit(pu.getLocatioin(),pu.getID()));// add(new ProbabilisticUnit(pu.getLocatioin(),pu.getID()));
			}
			
			killed=new int[13];
			for(int i=0;i<killed.length;i++){
				killed[i]=0;
			} 
			
			unidentified = new int[13];			
			unidentified[GameState.MARSHAL] = GameState.NUM_MARSHAL;
			unidentified[GameState.GENERAL] = GameState.NUM_GENERAL;
			unidentified[GameState.COLONEL] = GameState.NUM_COLONEL;
			unidentified[GameState.MAJOR] = GameState.NUM_MAJOR;
			unidentified[GameState.CAPTAIN] = GameState.NUM_CAPTAIN;
			unidentified[GameState.LIEUTENANT] = GameState.NUM_LIEUTENANT;
			unidentified[GameState.SERGEANT] = GameState.NUM_SERGEANT;
			unidentified[GameState.MINER] = GameState.NUM_MINER;
			unidentified[GameState.SCOUT] = GameState.NUM_SCOUT;
			unidentified[GameState.SPY] = GameState.NUM_SPY;
			unidentified[GameState.BOMB] = GameState.NUM_BOMB;
			unidentified[GameState.FLAG] = GameState.NUM_FLAG;
			
			//probabilityModel.get(0).printLikelihoods();
		}
		
		// Update the prior probabilities using Bayesian filtering P(piece's rank=x|gameEvents)
	//Build a probability model. For all the enemy units in the game, update their probabilities
		this.reassignProbabilities(gameEvents);

		
		// Now that the probabilities are updated, check what state you are in
		
		// Find out what your power state is. To do this, compare your strongest player to your opponent's strongest player (see killed array)
		int myStrongestRank = 9; // This is not right.
		for(PlayerUnit pu:myUnits){
			if(pu.getRank()<myStrongestRank){
				myStrongestRank=pu.getRank();
			}
		}
		
		// Find enemy's strongest rank here
		int enemyStrongestRank = 1;
		if(killed[GameState.MARSHAL]==GameState.NUM_MARSHAL){
			enemyStrongestRank=2;
		}
		if(killed[GameState.GENERAL]==GameState.NUM_GENERAL){
			enemyStrongestRank=3;
		}
		if(killed[GameState.COLONEL]==GameState.NUM_COLONEL){
			enemyStrongestRank=4;
		}
		if(killed[GameState.MAJOR]==GameState.NUM_MAJOR){
			enemyStrongestRank=5;
		}
		if(killed[GameState.CAPTAIN]==GameState.NUM_CAPTAIN){
			enemyStrongestRank=6;
		}
		if(killed[GameState.LIEUTENANT]==GameState.NUM_LIEUTENANT){
			enemyStrongestRank=7;
		}
		if(killed[GameState.SERGEANT]==GameState.NUM_SERGEANT){
			enemyStrongestRank=8;
		}
		if(killed[GameState.MINER]==GameState.NUM_MINER){
			enemyStrongestRank=9;
		}
		if(killed[GameState.SCOUT]==GameState.NUM_SCOUT){
			enemyStrongestRank=10;
		}
		System.out.println("Player " + this.playerID +"' s strongest rank is: " +myStrongestRank + ", and his enemy's strongest rank is " + enemyStrongestRank +".");
		
		if(enemyStrongestRank>myStrongestRank){
			this.powerState=PlayerExample.LOW_POWER;
		}
		if(enemyStrongestRank==myStrongestRank){
			this.powerState=PlayerExample.EQUAL_POWER;
		}
		if(enemyStrongestRank<myStrongestRank){
			this.powerState=PlayerExample.HIGH_POWER;
		}
		
		// Now determine your knowledge state. For now, if you know the identity of 3 enemy unit, lets say you are in moderate stage
		
		int numberOfAliveIdentifiedEnemyUnits = 0;
		//Try to find all the known players
		for(Integer i:this.probabilityModel.keySet()){
			if(this.probabilityModel.get(i).isIdentified()){
				numberOfAliveIdentifiedEnemyUnits++;
			}
		}
		
		if(numberOfAliveIdentifiedEnemyUnits>=3){
			this.knowledgeState=PlayerExample.MODERATE_KNOWLEDGE;
		}
		
		System.out.println("Player " + this.playerID +"' s current state is: " +this.knowledgeState + ", " + this.powerState +".");
		
		
		// Based on current state, pick an action
		
		// IF current state is low knowledge, equal power, use low ranking units for discovery purposes
		if(this.knowledgeState==PlayerExample.LOW_KNOWLEDGE&&this.powerState==PlayerExample.EQUAL_POWER){
			// Find the lowest ranking unit that can towards an enemy OR can attack an enemy
			int weakestRank = 1;
			ArrayList<UnitAction> weakestPlayerActions = new ArrayList<UnitAction>();
			for(UnitAction ua:legalActions){
				// Find the lowest ranked unit
				if(ua.getUnit().getRank()>weakestRank){
					weakestRank=ua.getUnit().getRank();
				}
			}
			for(UnitAction ua:legalActions){
				if(ua.getUnit().getRank()==weakestRank){
					weakestPlayerActions.add(ua);
				}
			}
			
			// Go through probability model and pick an enemy Unit most likely to be a flag
			float h = 0;
			ProbabilisticUnit m = null;
			Integer mf = null;
			for(Integer i:this.probabilityModel.keySet()){
				if((this.probabilityModel.get(i).rankLikelihoods[GameState.FLAG]-h)>0.001){
					h = this.probabilityModel.get(i).rankLikelihoods[GameState.FLAG];
					m=this.probabilityModel.get(i);
				}
			}
		//	if(m!=null){
			//	
		//	}
			
			
			// Pick the action based either
			// 1. Attacking an unidentified enemy player OR
			// 2. Moving towards the closest possible flag
			for(UnitAction ua:weakestPlayerActions){
				if((ua.getAction()==UnitAction.attack)&&(!this.probabilityModel.get(ua.getTarget().getID()).isIdentified())){
										
					selectedAction = ua;
				}
			}
			int closestDistance = 1000;
			UnitAction closestAction = weakestPlayerActions.get(0);
			System.out.println("Player " + this.playerID +"' s weakest ranking player is: " +weakestRank + ", and it has  " + weakestPlayerActions.size() +" actions.");
			for(UnitAction ua:weakestPlayerActions){
				
				if(GameState.getManhattanDistance(ua.getTargetTile(), m.getLocation())<closestDistance){
					closestDistance = GameState.getManhattanDistance(ua.getTargetTile(), m.getLocation());
					closestAction = ua;
				}
			}
			if(selectedAction==null){
				selectedAction=closestAction;
			}
			
		}
		else{
			
		
			// return random legal action
			Random randomGenerator = new Random();
					
			int randInt = randomGenerator.nextInt(legalActions.size());
			selectedAction = legalActions.get(randInt);
		}
		
		return selectedAction;
		//return legalActions.get(0);
		
	}
	
	private void reassignProbabilities(ArrayList<GameEvent> gameEvents){
		
		if(gameEvents.isEmpty()){
			// There have been no events so far. We can not adjust probabilities based on events.
			// TODO HOWEVER, we can assign probabilities based on locations/positions
			return;
		}
		
		//Right now, lets keep our depth 1 event deep. We will reassign probabilities only based on the most
		// recent event. Once we have this, we can make it go deeper TODO
		// However, the reassignments we make based on whether or not a piece has moved is simple, so we will do that
		
		// In this section of the code, we re-assign probabilities based on discoveries made by our units
		if(gameEvents.size()>=2){
		//	System.out.println("Player has made a move before");
			 GameEvent mostRecentMyGameEvent = gameEvents.get(gameEvents.size()-2);
			 
			 // IF you attacked somebody, their ID should be revealed.
			 if(mostRecentMyGameEvent.getAction()==UnitAction.attack){
		//		System.out.println("Player's previous move was attack");

				 // assert that it is your unit who attacked
				 assert(mostRecentMyGameEvent.getUnit().getPlayerID()==this.playerID);
				 int mostRecentTargetID = mostRecentMyGameEvent.getTarget().getID();
				 int mostRecentTargetRank = mostRecentMyGameEvent.getTarget().getRank();
				 
				 unidentified[mostRecentTargetRank] = unidentified[mostRecentTargetRank]-1;
				 
				 // This checks to see if the unit you attacked is still alive. If not, it just ignores it. 
				 if(probabilityModel.keySet().contains(mostRecentTargetID)){
				//		System.out.println("Attacked player still alive");

					 	// For all ranks besides target, set it to 0. For target, set it to 1.
					 	for(int i=0;i<13;i++){
					 		if(i==mostRecentTargetRank){
					 			probabilityModel.get(mostRecentTargetID).rankLikelihoods[i]=1;
					 			probabilityModel.get(mostRecentTargetID).setIdentified(true);
					 		}
					 		else{
					 			probabilityModel.get(mostRecentTargetID).rankLikelihoods[i]=0;
					 		}
					 	}
					 	probabilityModel.get(mostRecentTargetID).normalize(); // redudant, but good practice
					// 	probabilityModel.get(mostRecentTargetID).printLikelihoods();
					}
				 
				 if(mostRecentMyGameEvent.getTargetDies()){
					 this.killed[mostRecentTargetRank]=this.killed[mostRecentTargetRank]+1;
					 this.probabilityModel.remove(mostRecentTargetID);
				 } 
				 
			 }
			 
		}
		
		// In this section of the code, we reassign probabilities to all enemy players that have attacked our players		
		
		// IN this section of the code, we re-assing probabilities for all enemy players that have moved. //TODO enemy players who attacked our units
		GameEvent mostRecentEnemyGameEvent = gameEvents.get(gameEvents.size()-1);
		// This will reveal to you the results of YOUR previous move. This might also have valuable information
		
		
		
		// If the piece moved, there is 0% likelihood that it is a bomb or flag
		int recentlyMovedEnemyID = mostRecentEnemyGameEvent.getPlayerUnit().getID(); //
		// CHecks to see if the most recently  moved player is an enemy unit (it should be, but just to be sure)
		if(probabilityModel.keySet().contains(recentlyMovedEnemyID)){
			probabilityModel.get(recentlyMovedEnemyID).setMoved(true);

		}

		// IF the enemy player attacked your unit
		if(mostRecentEnemyGameEvent.getAction()==UnitAction.attack){
			int recentlyAttackedEnemyRank = mostRecentEnemyGameEvent.getUnit().getRank();
			unidentified[recentlyAttackedEnemyRank]=unidentified[recentlyAttackedEnemyRank]-1;
			
			// Now reassign probabilities so that all other ranks in this are 0 except for the enemy's rank
			// This checks to see if the unit you attacked is still alive. If not, it just ignores it. 
			 if(probabilityModel.keySet().contains(recentlyMovedEnemyID)){
			//		System.out.println("Attacked player still alive");

				 	// For all ranks besides target, set it to 0. For target, set it to 1.
				 	for(int i=0;i<13;i++){
				 		if(i==recentlyAttackedEnemyRank){
				 			probabilityModel.get(recentlyMovedEnemyID).rankLikelihoods[i]=1;
				 		}
				 		else{
				 			probabilityModel.get(recentlyMovedEnemyID).rankLikelihoods[i]=0;
				 		}
				 	}
				 	probabilityModel.get(recentlyMovedEnemyID).setIdentified(true);
				 	probabilityModel.get(recentlyMovedEnemyID).normalize(); // redudant, but good practice
				// 	probabilityModel.get(recentlyMovedEnemyID).printLikelihoods();
				}
			 
			 if(mostRecentEnemyGameEvent.getUnitDies()){
				 this.killed[recentlyAttackedEnemyRank]=this.killed[recentlyAttackedEnemyRank]+1;
				 this.probabilityModel.remove(recentlyMovedEnemyID);
			 } 
			
			
		}
	/*	else{ // it just moved. so you only know that it is not a bomb or flag
			if(probabilityModel.keySet().contains(recentlyMovedEnemyID)){
				
				probabilityModel.get(recentlyMovedEnemyID).setMoved(true);
			//	probabilityModel.get(recentlyMovedEnemyID).setLikelihood(GameState.BOMB, 0);
			//	probabilityModel.get(recentlyMovedEnemyID).setLikelihood(GameState.FLAG, 0);
			//	probabilityModel.get(recentlyMovedEnemyID).printLikelihoods();
	
			}
		} */
		
		// NOw we assing the probabilities for the unidentified pieces in the game.
		// The ones that moved should have the assignment of 0 for flag or bomb
		for(Integer i:probabilityModel.keySet()){
			// FOr all enemy units that we are CLUELESS about
			if((!probabilityModel.get(i).isIdentified())&&(!probabilityModel.get(i).isPossiblyIdentified())){
				
				// IF the enemy unit has moved, it is surely NOT a bomb or flag
				if(probabilityModel.get(i).hasMoved()){
					probabilityModel.get(i).rankLikelihoods[GameState.MARSHAL]= unidentified[GameState.MARSHAL];
					probabilityModel.get(i).rankLikelihoods[GameState.GENERAL]= unidentified[GameState.GENERAL];
					probabilityModel.get(i).rankLikelihoods[GameState.COLONEL]= unidentified[GameState.COLONEL];
					probabilityModel.get(i).rankLikelihoods[GameState.MAJOR]= unidentified[GameState.MAJOR];
					probabilityModel.get(i).rankLikelihoods[GameState.CAPTAIN]= unidentified[GameState.CAPTAIN];
					probabilityModel.get(i).rankLikelihoods[GameState.LIEUTENANT]= unidentified[GameState.LIEUTENANT];
					probabilityModel.get(i).rankLikelihoods[GameState.SERGEANT]= unidentified[GameState.SERGEANT];
					probabilityModel.get(i).rankLikelihoods[GameState.MINER]= unidentified[GameState.MINER];
					probabilityModel.get(i).rankLikelihoods[GameState.SCOUT]= unidentified[GameState.SCOUT];
					probabilityModel.get(i).rankLikelihoods[GameState.SPY]= unidentified[GameState.SPY];
					probabilityModel.get(i).rankLikelihoods[GameState.BOMB]= 0;
					probabilityModel.get(i).rankLikelihoods[GameState.FLAG]= 0;

				}
				else{
					
					probabilityModel.get(i).rankLikelihoods[GameState.MARSHAL]= unidentified[GameState.MARSHAL];
					probabilityModel.get(i).rankLikelihoods[GameState.GENERAL]= unidentified[GameState.GENERAL];
					probabilityModel.get(i).rankLikelihoods[GameState.COLONEL]= unidentified[GameState.COLONEL];
					probabilityModel.get(i).rankLikelihoods[GameState.MAJOR]= unidentified[GameState.MAJOR];
					probabilityModel.get(i).rankLikelihoods[GameState.CAPTAIN]= unidentified[GameState.CAPTAIN];
					probabilityModel.get(i).rankLikelihoods[GameState.LIEUTENANT]= unidentified[GameState.LIEUTENANT];
					probabilityModel.get(i).rankLikelihoods[GameState.SERGEANT]= unidentified[GameState.SERGEANT];
					probabilityModel.get(i).rankLikelihoods[GameState.MINER]= unidentified[GameState.MINER];
					probabilityModel.get(i).rankLikelihoods[GameState.SCOUT]= unidentified[GameState.SCOUT];
					probabilityModel.get(i).rankLikelihoods[GameState.SPY]= unidentified[GameState.SPY];
					probabilityModel.get(i).rankLikelihoods[GameState.BOMB]= unidentified[GameState.BOMB];
					probabilityModel.get(i).rankLikelihoods[GameState.FLAG]= unidentified[GameState.FLAG];
					
					
				}
				probabilityModel.get(i).normalize(); // DON'T FORGET TO NORMALIZE

				
			}
		}
		
		
		//----
	/*	ArrayList<Integer> moved = new ArrayList<Integer>();
		// If the unit apprears in gameevents, it has moved.
		for(GameEvent ge:gameEvents){
			moved.add(ge.getPlayerUnit().getID());
		}
		// Now go through the prob model and change the prob model so that the moved pieces are never bombs
		for(Integer i:probabilityModel.keySet()){
			// If the prob unit is in moved array
			if(moved.contains(i)){
				probabilityModel.get(i).setLikelihood(GameState.BOMB, 0);
				probabilityModel.get(i).printLikelihoods();
			}
			// If the prob unit is not in moved array, then it is SLIGHTLY more likely to be a bomb or flag
		} */
		
		return;
	}


}