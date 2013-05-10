/**
 * This class represents what the AI Agents will acess when they see pieces.
 */
package stratego_engine.player_interface;

import stratego_engine.Piece;

/**
 * @author Pratik
 *
 */

//TODO Make it a child class of Piece (but override constructor so the ID count doesn't rise
// TODO You might not need this. INstead of passing in a PlayerPiece, just pass in an int. When the player calls PGameState, it simply won't return the rank
public class PlayerPiece extends Piece {

	public PlayerPiece(Piece p) {
		//super(playerID, x, y, rank);
		
	}

}
