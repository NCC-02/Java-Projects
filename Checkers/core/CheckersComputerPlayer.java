package core;

import java.util.Random;

/**
 * 
 * @author Nicolas Coles
 * @version 1.0
 * Date 9/7/2022
 * Class SER 216
 * This class creates a simple checkers Computer AI that plays against the player if the player decides to.
 *
 */

public class CheckersComputerPlayer extends CheckersLogic {
	private int[] move;
	/**
	 * Constructor so that the Computer can be used in other classes.
	 */
	public CheckersComputerPlayer() {}
	/**
	 * 
	 * @param x The starting x coordinate on the board[][]
	 * @param y The starting y coordinate on the board[][]
	 * @return whether the piece selected has any available moves
	 */
	public boolean isMoveablePiece(int x, int y) {
		try {
			if (board[x][y] != 'o') { return false; } //if the piece selected is not an O for the Computer.
			if (x == 0 || y == 0) {
				if (board[x + 1][y + 1] == '_' || (board[x + 2][y + 2] == '_' && board[x + 1][y + 1] == 'x')) {
					return true;
				}
			} else if (x == 8 || y == 8) {
				return false;
				
			} else if (board[x+1][y+1] == '_' || board[x-1][y+1] == '_' || (board[x+2][y+2] == '_' && board[x + 1][y + 1] == 'x') || (board[x - 2][y + 2] == '_' && board[x - 1][y + 1] == 'x')) {
				return true; // returns true if jump is possible.
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}

		return false; // if jump is not possible, return false.
	}
	

	/**
	 * This class creates a move by using randomization and checking whether the generated move is valid or not.
	 * @return an array of the finished Computer move
	 */
	public int[] computerMove() { // returns the computer's move.
		move = new int[4];
		Random rand = new Random();
		int decision;
		while (!isMoveablePiece(move[0], move[1])) { //the idea is to first find a starting coordinate that works
													 //so that the search to find a valid end coordinate takes less resources/time.
			move[0] = rand.nextInt(8); //move[0] is startX
			move[1] = rand.nextInt(8); //move[1] is startY
		}

		
		while (!isValid(move)) {
			decision = rand.nextInt(11);
			if (move[0] > 2 || move[1] > 2) {
				if ((board[move[0] + 2][move[1] + 2] == '_' && board[move[0] + 1][move[1] + 1] == 'x')) {
					move[2] = move[0] + 2;
					move[3] = move[1] + 2;
					break;
				} else if ((board[move[0] - 2][move[1] + 2] == '_' && board[move[0] - 1][move[1] + 1] == 'x')) {
					move[2] = move[0] - 2;
					move[3] = move[1] + 2;
					break;
				}
			} 
			if(decision > 5) {
				move[2] = move[0] + 1;
				move[3] = move[1] + 1;
			} else {
				move[2] = move[0] - 1;
				move[3] = move[1] + 1;
			}
		}
		
		return move;// once a valid move is recieved, make the move.
	}
	
}