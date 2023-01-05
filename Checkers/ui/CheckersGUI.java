/**
 * @author Nicolas Coles
 * @version 1.0
 * This class provides a JavaFX GUI for the checkers game. When launching the CheckersLogic.java file, it will prompt the user
 * with the question of whether they would like to use the GUI or the text console version of checkers.
 * 
 */
package ui;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import core.CheckersLogic;

public class CheckersGUI extends Application {
	private Group tiles = new Group();
	private Group pieces = new Group();
	protected Tile[][] board = new Tile[8][8];
	
	/**
	 * This method creates all of the piece objects and makes them interact-able
	 * @param type Color of the piece
	 * @param x Coordinate to place piece initially
	 * @param y Coordinate
	 * @return Piece to be displayed and interacted with on the board
	 */
    public Piece makePiece (int type, int x, int y) {
    	Piece p = new Piece(type, x, y);
    	p.setOnMouseReleased(e -> {
    		int endX = boardConversion(p.getLayoutX());
    		int endY = boardConversion(p.getLayoutY());
    		
    		if (endX < 0 || endY < 0 || endX >= 8 || endY >= 8) {
    			System.out.println("Error");
    		} else {
    			int validMove = isValidMove(p, endX, endY);
    			if (validMove == 0) {
    				System.out.println("Not a valid move!"); 
    				
    			}
    			
    			else if (validMove == 1)
    				System.out.println("Valid move");
    			else if (validMove == 2) 
    				System.out.println("Capture move"); // Develop this further later
    		}
    		
    		int oldX = boardConversion(p.getStartX());
    		int oldY = boardConversion(p.getStartY());
    		
    		int playerTurn = 1;
    		switch (playerTurn) {
    			case 0:
    				
    				p.move(endX, endY);
    				board[oldX][oldY].setPiece(null);
    				board[endX][endY].setPiece(p);  
    				playerTurn = 1;
    				break;
    			case 1:
    				p.move(endX, endY);
    				board[oldX][oldY].setPiece(null);
    				board[endX][endY].setPiece(p);
    				playerTurn = 0;
    				break;
    				
    			default:
    				System.out.println("Error.");

    		}
    	});
    	
    	return p;
    }
    
    
    /**
     * This class tests a possible move and returns a code based on whether the move is possible or not, and whether it is a 
     * double jump move/capture move or not.
     * @param p Piece to try to move
     * @param endX coordinate to move the piece to
     * @param endY coordinate to move the piece to
     * @return 0 if the move cannot be executed, 1 if it can be, and 2 if it is a double jump.
     */
    public int isValidMove(Piece p, int endX, int endY) {
    	if (board[endX][endY].piece != null || (endX + endY) % 2 == 0) {
    		return 0; // If the move cannot be executed
    	}
    	int startX = boardConversion(p.getStartX());
    	int startY = boardConversion(p.getStartY());
    	
    	if (Math.abs(endX - startX) == 1) {
    		return 1; // if the move can be executed
    	} else if (Math.abs(endX - startX) == 2) {
    		int newX = startX + (endX - startX) / 2;
    		int newY = startY + (endY - startY) / 2;
    		if (board[newX][newY].getPiece() != null && board[newX][newY].getPiece().pieceType != p.pieceType) {
    			return 2; // if the move is a double jump
    		}
    	}
    	
    	return 0;
    }

    /**
     * Repeated conversion that converts the size of the board
     * @param size
     * @return
     */
    public int boardConversion(double size) {
    	return (int)(size + 100 / 2) / 100;
    }
    /**
     * 
     * @return root Creates the root Pane with Tiles and Pieces.
     */
	public Parent create() {
		Pane root = new Pane();
		root.setPrefSize(800, 800);
		root.getChildren().addAll(tiles, pieces);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Tile t = new Tile((i + j) % 2 == 0, i, j);
				board[i][j] = t;
				
				tiles.getChildren().add(t);
				Piece p = null;
				
				if (j <= 2 && (i + j) % 2 != 0) {
					p = makePiece(0, i, j);
				}
				if (j >= 5 && (i + j) % 2 != 0) {
					p = makePiece(1, i, j);
				}
				if (p != null) {
					t.setPiece(p);
					pieces.getChildren().add(p);
				}
				
			}
		}
		
		return root;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Scene scene = new Scene(create());
			primaryStage.setTitle("Checkers");
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * 
	 * This class creates the Tile object that the Piece objects will lay upon in javafx. It includes getter methods
	 * and sets the color of the tiles.
	 */
	private class Tile extends Rectangle {
		private Piece piece;
		/**
		 * Creates the Tile object on which Piece objects can lay upon.
		 * @param light Whether the tile is a light tile or a dark tile
		 * @param x Coordinate
		 * @param y Coordinate
		 */
		public Tile(boolean light, int x, int y) {
			setWidth(100);
			setHeight(100);
			relocate(x * 100, y * 100);
			setFill(light ? Color.ANTIQUEWHITE : Color.CORNFLOWERBLUE); // creates a loop inside to color every other tile two different colors.
			
			
		}
		public Piece getPiece() {
			return piece;
		}
		
		public void setPiece(Piece p) {
			piece = p;
		}		
		
	}
	/**
	 * 
	 * This class creates the Piece object. It includes the logic to make the pieces moveable via mouse click-and-drag.
	 * 
	 *
	 */
	private class Piece extends StackPane {
		protected int pieceType; // if 0 : Black, if 1 : Red
		private double startX, startY;
		private double mouseX, mouseY;
		/**
		 * Constructor for the Piece object.
		 * @param type whether a black piece or a red piece.
		 * @param x X Coordinate location
		 * @param y Y Coordinate location
		 */
		public Piece(int type, int x, int y) {
			pieceType = type;
			move(x, y);
			
			
			Ellipse ellipse = new Ellipse(31.25, 26);
			ellipse.setFill(type == 0 ? Color.BLACK : Color.RED);
			ellipse.setStroke(type == 0 ? Color.WHITE: Color.BLACK);
			ellipse.setStrokeWidth(3);
			
			ellipse.setTranslateX((100 - 31.25 * 2) / 2);
			ellipse.setTranslateY((100 - 26 * 2) / 2);
			getChildren().add(ellipse);
			
			setOnMousePressed(e -> {
				mouseX = e.getSceneX();
				mouseY = e.getSceneY();
			});
			setOnMouseDragged(e -> {
				relocate(e.getSceneX() - mouseX + startX, e.getSceneY() - mouseY + startY);
			});
		}
		/**
		 * 
		 * @param x X coordinate for where to relocate the current piece
		 * @param y Y coordinate
		 * Moves the piece into the given x and y coordinates on the GUI screen.
		 */
		public void move(int x, int y) {
			startX = x * 100;
			startY = y * 100;
			relocate(startX, startY);
		}
		//getter methods
		public int getPieceColor() { return pieceType; }
		public double getStartX() { return startX; }
		public double getStartY() { return startY; }
		

	}
	
	
}

