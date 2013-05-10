package ai;

import game_engine.GameState;
import game_engine.UnitAction;

import java.util.ArrayList;

public class ExpectiminimaxNode {

	private boolean terminal;
	private boolean adversary;
	private int heuristicValue;
	private ArrayList<ExpectiminimaxNode> children;
	private UnitAction action;
	
	private AIGameState state; // Depicts the current estimated status of the game. Used for calculating heuristics
	
	/*
	 * This should also store the supposed game state.
	 * 
	 * 1. map: key: unit ID:
	 */
	
	
	public ExpectiminimaxNode(UnitAction action, boolean adversary){
		this.action = action;
		this.adversary=adversary;
		
		// Check to see if terminal
		if((action.getAction()==UnitAction.attack)&&(action.getTarget()==GameState.FLAG)){
				this.terminal=true;
		}
		else{
			this.terminal=false;
		}
		
	/*	if(adversary){
			this.heuristicValue = Integer.MAX_VALUE;
		}
		else{
			this.heuristicValue = Integer.MIN_VALUE;
		}
	 */
		this.heuristicValue=0;
		
		// adjust children based on state
	}

	/**
	 * @return the terminal
	 */
	public boolean isTerminal() {
		return terminal;
	}

	/**
	 * @param terminal the terminal to set
	 */
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	/**
	 * @return the adversary
	 */
	public boolean isAdversary() {
		return adversary;
	}

	/**
	 * @param adversary the adversary to set
	 */
	public void setAdversary(boolean adversary) {
		this.adversary = adversary;
	}

	/**
	 * @return the heuristicValue
	 */
	public int getHeuristicValue() {
		return heuristicValue;
	}

	/**
	 * @param heuristicValue the heuristicValue to set
	 */
	public void setHeuristicValue(int heuristicValue) {
		this.heuristicValue = heuristicValue;
	}

	/**
	 * @return the children
	 */
	public ArrayList<ExpectiminimaxNode> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<ExpectiminimaxNode> children) {
		this.children = children;
	}

	/**
	 * @return the action
	 */
	public UnitAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(UnitAction action) {
		this.action = action;
	}
}
