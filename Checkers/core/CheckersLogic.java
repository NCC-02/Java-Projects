/**
 * @author Nicolas Coles
 * @studentid 1223626274
 * Class SER 216
 * @version 1.1
 * This is a simple checkers game program that runs the classic game of Checkers in a text/console based format.
 * This is the main logic file for the game that has the main method and puts the game board and logic together.
 */

package core;

import java.util.Scanner;

import ui.CheckersGUI;
import ui.CheckersTextConsole;
import core.CheckersComputerPlayer;
import javafx.application.Application;

public class CheckersLogic extends CheckersTextConsole { // This class extends the console/board text interface.
	private char playerTurn;
	private int[] moves;
	private int xCount;
	private int oCount;
	CheckersTextConsole checkers;

	/**
	 * Constructor that initializes the number of pieces on the board, the first player's turn, the text console, and the move storage array.
	 */
	public CheckersLogic() {
		checkers = new CheckersTextConsole();
		playerTurn = 'x';
		xCount = 12;
		oCount = 12;
		moves = new int[4];
	}
	/**
	 * 
	 * @param move integer array that stores the move information that the user inputs.
	 * @return true or false depending on whether the inputted move is valid or not
	 * This method checks whether or not an inputted set of moves is valid or not.
	 */
	public boolean isValid(int[] move) {
		
		for (int i = 0; i < move.length; i++) {
			if (move[i] < 0 || move[i] > 7)
				return false;
		}
		
		int xStart = move[0];
		int yStart = move[1];
		int xEnd = move[2];
		int yEnd = move[3];
		
		if (Math.abs(xStart - xEnd) == 1) {
			if (playerTurn == 'o' && Math.abs(yEnd - yStart) == 1)
				return true;
			else if (playerTurn == 'x' && Math.abs(yEnd - yStart) == 1)
				return true;
		} else if (Math.abs(xStart - xEnd) == 2) {
			if (playerTurn == 'o' && Math.abs(yEnd - yStart) == 2 && board[(xStart + xEnd) / 2][(yStart + yEnd) / 2] == 'x')
				return true;
			else if (playerTurn == 'x' && Math.abs(yEnd - yStart) == 2 && board[(xStart + xEnd) / 2][(yStart + yEnd) / 2] == 'o')
				return true;
		}
		
		
		return false;
	}
	
	/**
	 * 
	 * @param move
	 * Executes the move given a valid array of user inputs.
	 * It makes the place in which the piece is moved blank by default, then checks whether or not the move is a capture or a simple move.
	 * 
	 */
	public void move(int[] move) {
		int xStart = move[0];
		int yStart = move[1];
		int xEnd = move[2];
		int yEnd = move[3];

		board[xStart][yStart] = '_';
		board[xEnd][yEnd] = playerTurn;
		
		if (Math.abs(xStart - xEnd) == 2) { // If the move is two spaces away, take the piece in between.
			board[(xStart + xEnd) / 2][(yStart + yEnd) / 2] = '_'; //piece in between gets taken
			if (playerTurn == 'o')
				xCount--;
			else
				oCount--;
			
			
			if (board[xEnd + 2][yEnd + 2] == '_' || board[xEnd - 2][yEnd + 2] == '_') { // allows multiple jumps if possible
				System.out.println("Double jump available.");
				return;
			} 	
		}
		if (playerTurn == 'x') {
			playerTurn = 'o';
		} else
			playerTurn = 'x';
	}
	/**
	 * This method retrieves the move from the user in the format of
	 * for example 3a-4b. The method then turns this input into actual coordinates
	 * on the checkers board and stores it in the moves array.
	 */
	public void getMove() {
		Scanner in = new Scanner(System.in);
			
		if (playerTurn == 'o') {
			System.out.println("Player O - your turn.");
		} else
			System.out.println("Player X - your turn.");
		
		boolean valid = false;
		
		while (!valid) {
			try {
			
				System.out.println("Choose a cell position of piece to be moved and the new position. e.g., 3a-4b");				
				String input = in.nextLine();
				
				System.out.println(input);
				String start = input.substring(0, input.indexOf("-"));
				String end = input.substring(input.indexOf("-") + 1);
				
				for (int i = 0; i < 8; i++) {
					
					if (((int)start.charAt(1) - 97) == i) {
						moves[0] = i;
					}
					if (Integer.parseInt(start.substring(0, 1)) == i) {
						moves[1] = i - 1;
					}
				}
				
				for (int i = 0; i < 8; i++) {
					if (((int)end.charAt(1) - 97) == i) {
						moves[2] = i;
					}
					if (Integer.parseInt(end.substring(0, 1)) == i) { //last coordinate
						moves[3] = i - 1;
					}
				}
				if (isValid(moves)) {
					valid = true;
					move(moves); 
				}	
			
			} catch (Exception e) {
				System.out.println("Input Error. Try again.");
				in.next();
			}
		}
	}
	/**
	 * 
	 */
	public void getMoveComputer() {
		Scanner in = new Scanner(System.in);
		System.out.println("You are Player X. It is your turn.");
		
		boolean valid = false;
		
		while (!valid) { 
			try {
			
				System.out.println("Choose a cell position of piece to be moved and the new position. e.g., 3a-4b");				
				String input = in.nextLine();
				
				System.out.println(input);
				String start = input.substring(0, input.indexOf("-"));
				String end = input.substring(input.indexOf("-") + 1);
				
				for (int i = 0; i < 8; i++) {
					
					if (((int)start.charAt(1) - 97) == i) {
						moves[0] = i;
					}
					if (Integer.parseInt(start.substring(0, 1)) == i) {
						moves[1] = i - 1;
					}
				}
				
				for (int i = 0; i < 8; i++) {
					if (((int)end.charAt(1) - 97) == i) {
						moves[2] = i;
					}
					if (Integer.parseInt(end.substring(0, 1)) == i) { //last coordinate
						moves[3] = i - 1;
					}
				}
				if (isValid(moves)) {
					valid = true;
					move(moves); 
				}	
			
			} catch (Exception e) {
				System.out.println("Input Error. Try again.");
//				in.next();
			}
		}
		CheckersComputerPlayer com = new CheckersComputerPlayer();
		int[] computerMove = com.computerMove();
		move(computerMove);
		
	}
	/**
	 * 
	 * @return whether the game is over or not
	 * This class determines whether or not a condition has been met to end the game and announces who the winner is.
	 */
	public boolean gameOverCheck() {
		if (oCount == 0) {
			System.out.println("Player X Won the Game");
			return true;
		} else if (xCount == 0) {
			System.out.println("Player O Won the Game");
			return true;
		}
		return false;
			
	}
	
	
	
	/**
	 * 
	 * @param args
	 * Main class that loops the game to keep making moves until a win/lose condition has been met by one of the players.
	 * User has the option to use a GUI for a more interactive experience, or a console-based interface.
	 */
	public static void main(String[] args) {
		CheckersLogic c = new CheckersLogic();
		Scanner in = new Scanner(System.in);
		System.out.println("Enter 'G' for JavaFX GUI or 'T' for text UI:");
		char selection = in.next().charAt(0);
		if (selection == 'G') {
			Application.launch(CheckersGUI.class);
		} else if (selection == 'T') {
		char playerVersus = ' ';
		CheckersComputerPlayer com = new CheckersComputerPlayer();
		
		c.printBoard();
		System.out.print("Begin Game. " );
		System.out.println("Enter 'P' if you want to play against another player; enter 'C' to play against computer.");
		
		if (playerVersus != 'C' || playerVersus != 'P') { 
			playerVersus = in.next().toUpperCase().charAt(0);
		}
		
		if (playerVersus == 'C') {
			while (!c.gameOverCheck()) {
				c.getMoveComputer();
				com.computerMove();
				c.printBoard();
				
			}
		} else {
			while (!c.gameOverCheck()) {
				c.getMove();
				c.printBoard();
			}
		}
		} else
			System.out.println("Invalid Entry!\n");
	}
}
