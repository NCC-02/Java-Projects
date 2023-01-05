package ui;

/**
 * 
 * @author Nicolas Coles
 * @version 1.0
 * This class provides the console/text based Checker board with the number and letter grid system that indicates the name of each space
 * to the user. It also initializes the board pieces, with one side as X and the other as O.
 */

public class CheckersTextConsole {
	protected char[][] board; //piece placement storage
	/**
	 * Constructor that builds the checkers board.
	 */
	public CheckersTextConsole() {
		
		board = new char[8][8];
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = '_';
			}
		}
		for (int i = 1; i < 8; i += 2) {
			board[i][0] = 'o';
			board[i][2] = 'o';
			board[i][6] = 'x';	
		}
		for (int i = 0; i < 8; i += 2) { 
			board[i][1] = 'o';
			board[i][5] = 'x';
			board[i][7] = 'x';
		}
	}
	

	/**
	 * This method prints the board with all of the updated current piece placements.
	 */
	public void printBoard() {
		
		for (int i = 0; i < 8; i++) {
			System.out.print((1 + i) + " ");
			for (int j = 0; j < 8; j++) {
				System.out.print(" | " + board[j][i]);
				if (j == 7) 
					System.out.print(" |");
			}
			System.out.println();

		}
		System.out.println("     a   b   c   d   e   f   g   h");		
	}
}