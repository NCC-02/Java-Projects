/**
 * 
 * @author Nicolas Coles
 * Client for the Server-Client connection in the Rock-Paper-Scissors game.
 * Connects to the server in RPSServer.java
 * 
 *
 */
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class RPSClient extends Application implements RPSConstants {
  // Indicate whether the player has the turn
  private boolean myTurn = false;

  // Indicate the token for the player
  private int myToken = 0;

  // Indicate the token for the other player
  private int otherToken = 0;

  // Create and initialize cells

  // Create and initialize a title label
  private Label lblTitle = new Label();

  // Create and initialize a status label
  private Label lblStatus = new Label();

  // Indicate selected row and column by the current move
  private int moveSelected = 0;

  // Input and output streams from/to server
  private DataInputStream fromServer;
  private DataOutputStream toServer;

  // Continue to play?
  private boolean continueToPlay = true;

  // Wait for the player to mark a cell
  private boolean waiting = true;

  // Host name or ip
  private String host = "localhost";

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Pane to hold cell
    GridPane pane = new GridPane(); 
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(lblTitle);
    borderPane.setCenter(pane);
    borderPane.setBottom(lblStatus);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(borderPane, 320, 350);
    primaryStage.setTitle("RPSClient"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage   

    // Connect to the server
    connectToServer();
  }

  private void connectToServer() {
    try {
      // Create a socket to connect to the server
      Socket socket = new Socket(host, 8000);

      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    // Control the game on a separate thread
    new Thread(() -> {
      try {
        // Get notification from the server
        int player = fromServer.readInt();
  
        // Am I player 1 or 2?
        if (player == PLAYER1) {
          myToken = PLAYER1;
          otherToken = PLAYER2;
          Platform.runLater(() -> {
            lblTitle.setText("Player 1");
            lblStatus.setText("Waiting for player 2 to join");
          });
  
          // Receive startup notification from the server
          fromServer.readInt(); // Whatever read is ignored
  
          // The other player has joined
          Platform.runLater(() -> 
            lblStatus.setText("Player 2 has joined. I start first"));
  
          // It is my turn
          myTurn = true;
        }
        else if (player == PLAYER2) {
          myToken = PLAYER2;
          otherToken = PLAYER1;
          Platform.runLater(() -> {
            lblTitle.setText("Player 2");
            lblStatus.setText("Waiting for player 1 to move");
          });
        }
  
        // Continue to play
        while (continueToPlay) {      
          if (player == PLAYER1) {
            waitForPlayerAction(); // Wait for player 1 to move
            sendMove(); // Send the move to the server
            receiveInfoFromServer(); // Receive info from the server
          }
          else if (player == PLAYER2) {
            receiveInfoFromServer(); // Receive info from the server
            waitForPlayerAction(); // Wait for player 2 to move
            sendMove(); // Send player 2's move to the server
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  /** Wait for the player to mark a cell */
  private void waitForPlayerAction() throws InterruptedException {
    while (waiting) {
      Thread.sleep(100);
    }

    waiting = true;
  }

  /** Send this player's move to the server */
  private void sendMove() throws IOException {
    toServer.writeInt(moveSelected); // Send the selected row
    // Send the selected column
  }

  /** Receive info from the server */
  private void receiveInfoFromServer() throws IOException {
    // Receive game status
    int status = fromServer.readInt();

    if (status == PLAYER1_WON) {
      // Player 1 won, stop playing
      continueToPlay = false;
      if (myToken == PLAYER1) {
        Platform.runLater(() -> lblStatus.setText("I won!"));
      }
      else if (myToken == PLAYER2) {
        Platform.runLater(() -> 
          lblStatus.setText("Player 1 has won!"));
        receiveMove();
      }
    }
    else if (status == PLAYER2_WON) {
      // Player 2 won, stop playing
      continueToPlay = false;
      if (myToken == PLAYER2) {
        Platform.runLater(() -> lblStatus.setText("I won!"));
      }
      else if (myToken == PLAYER1) {
        Platform.runLater(() -> 
          lblStatus.setText("Player 2 has won!"));
        receiveMove();
      }
    }
    else if (status == DRAW) {
      // No winner, game is over
      continueToPlay = true;
      Platform.runLater(() -> 
        lblStatus.setText("Draw! Continue."));
    }
    else {
      receiveMove();
      Platform.runLater(() -> lblStatus.setText("My turn"));
      myTurn = true; // It is my turn
    }
  }

  private void receiveMove() throws IOException {
    // Get the other player's move
    int move = fromServer.readInt();
    Platform.runLater(() -> moveSelected = move);
  }

  // An inner class for a cell
  public class RPS extends Pane {
    // Indicate the row and column of this cell in the board
    private int move;


    public RPS(int move) {
      this.move = move;
      this.setPrefSize(2000, 2000); // What happens without this?
      setStyle("-fx-border-color: black"); // Set cell's border
      this.setOnMouseClicked(e -> handleMouseClick());  
    }

    /** Return token */
    public int getMove() {
      return move;
    }

    /** Set a new token */
    public void setMove(int m) {
      move = m;
      repaint();
    }

    protected void repaint() {
      if (myToken == PLAYER1) {
        Line line1 = new Line(10, 10, 
          this.getWidth() - 10, this.getHeight() - 10);
        line1.endXProperty().bind(this.widthProperty().subtract(10));
        line1.endYProperty().bind(this.heightProperty().subtract(10));
        Line line2 = new Line(10, this.getHeight() - 10, 
          this.getWidth() - 10, 10);
        line2.startYProperty().bind(
          this.heightProperty().subtract(10));
        line2.endXProperty().bind(this.widthProperty().subtract(10));
        
        // Add the lines to the pane
        this.getChildren().addAll(line1, line2); 
      }
      else {
        Button rock = new Button("ROCK");
        Button paper = new Button("PAPER");
        Button scissors = new Button("SCISSORS");

        
        getChildren().addAll(rock, paper, scissors); // Add the ellipse to the pane
      }
    }

    /* Handle a mouse click event */
    private void handleMouseClick() {
      // If the player has the turn
      if (myTurn) {
        myTurn = false;
        moveSelected = move;
        lblStatus.setText("Waiting for the other player to select their move");
        waiting = false; // Just completed a successful move
      }
    }
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
