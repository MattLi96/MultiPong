package client.gui;

import game.Pong;
import game.PongImpl;
import game.state.State;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import client.http.HttpPong;

/**
 * Controller for the GUI, initializes the view. Contains most of the important
 * code.
 * 
 * @author Matthew
 */
public class ActionController implements Initializable {
	@FXML
	private Label helpText;

	/** The anchor pane that the pong game is drawn on */
	@FXML
	private StackPane world;

	/** The user title pane and it's properties */
	@FXML
	private TitledPane userTitlePane;
	@FXML
	private TextField username;
	@FXML
	private Button joinGame;
	@FXML
	private Button leaveGame;
	/**
	 * The current username of the player for the GUI in the pong game. If the
	 * player is not part of the game then it's the empty string. Used to helps
	 * determine if the player has joined the game already or not.
	 */
	private String currUsername;

	@FXML
	private TitledPane gameTitlePane;
	@FXML
	private TextField initialLives;
	@FXML
	private Button startGame;
	@FXML
	private Button endGame;

	/** Write out the winner here */
	@FXML
	private Label winner;
	@FXML
	/** The score grid, row 0 is saved for the winner */
	private GridPane livesGrid;

	/** The pong game the GUI interacts with */
	private volatile Pong pong;
	private PongGraphics pongGraphics;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currUsername = ""; // set username to none
		leaveGame.setDisable(true); // cannot leave the game yet

		// Setup the pong game
		setupHttpPong();

		// Startup graphics and key reading
		pongGraphics = new PongGraphics(this, world, winner, livesGrid);
		pongGraphics.start();
		setupKeyEvents();
	}

	/**
	 * These fields are for helping move the paddles
	 */
	volatile private boolean moveRight = false;
	volatile private boolean moveLeft = false;

	private void setupKeyEvents() {
		final EventHandler<KeyEvent> pressedEventHandler = new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.RIGHT) {
					moveRight = true;
				} else if (keyEvent.getCode() == KeyCode.LEFT) {
					moveLeft = true;
				} else if (keyEvent.getCode() == KeyCode.R) {
					pongGraphics.rotate();
				} else if (keyEvent.getCode() == KeyCode.T
						&& !currUsername.equals("")) {
					pongGraphics.rotate(currUsername);
				}

				movePaddle();
				keyEvent.consume();
			}
		};

		final EventHandler<KeyEvent> releasedEventHandler = new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.RIGHT) {
					moveRight = false;
				} else if (keyEvent.getCode() == KeyCode.LEFT) {
					moveLeft = false;
				}

				movePaddle();
				keyEvent.consume();
			}
		};

		world.addEventFilter(MouseEvent.ANY, (e) -> world.requestFocus());
		world.setOnKeyPressed(pressedEventHandler);
		world.setOnKeyReleased(releasedEventHandler);
	}

	private synchronized void movePaddle() {
		if (currUsername.equals(""))
			return;

		if (moveRight ^ moveLeft) {
			if (moveRight) {
				pong.moveRight(currUsername);
			} else {
				pong.moveLeft(currUsername);
			}
		} else {
			pong.moveNone(currUsername);
		}
	}

	public void controlsPopup() {
		String text = "Controls: \n r: rotate the field once \n t: rotate your side to the bottom"
				+ "\n right arrow: move paddle right \n left arrow: move paddle left";
		showPopUp(text);
	}

	/**
	 * Adds the player to a game
	 */
	public synchronized void joinGame() {
		String name = username.getText();
		if (pong.addPlayer(name)) {
			currUsername = name;
			username.setDisable(true);
			joinGame.setDisable(true);
			leaveGame.setDisable(false);
			userTitlePane.setExpanded(false);
		} else {
			showPopUp("Was unable to join the game, likely due to the same username already being used");
			currUsername = "";
			username.setDisable(false);
			joinGame.setDisable(false);
			leaveGame.setDisable(true);
		}
	}

	/**
	 * Remove the player from a game
	 */
	public synchronized void leaveGame() {
		if (!checkJoined())
			return;

		// Remove the player from the game
		pong.removePlayer(currUsername);
		currUsername = "";
		username.setDisable(false);
		joinGame.setDisable(false);
		leaveGame.setDisable(true);
	}

	/**
	 * Start a game using the given amount of lives
	 */
	public synchronized void startGame() {
		if (!checkJoined())
			return;

		boolean started = false;
		if (initialLives.getText().equals("")) {
			// start game with default lives
			started = pong.start();
		} else {
			try {
				int lives = Integer.parseInt(initialLives.getText());
				started = pong.start(lives);
			} catch (NumberFormatException e) {
				showPopUp("Please enter a valid integer for the starting lives");
				return;
			}
		}

		if (!started) {
			showPopUp("Unable to start game, it may already be running or there are not enough players");
		} else {
			gameTitlePane.setExpanded(false);
		}
	}

	/**
	 * End a game
	 */
	public synchronized void endGame() {
		if (!checkJoined())
			return;

		pong.end();
	}

	/**
	 * Used to see if the player has joined the game. Shows a popup if the
	 * player has not joined
	 * 
	 * @return if the player has joined the game yet
	 */
	private synchronized boolean checkJoined() {
		// If player has not joined the game
		if (currUsername.equals("")) {
			showPopUp("You have not joined the game yet");
			return false;
		}
		return true;
	}

	public State getPongState() {
		return pong.getState();
	}

	private void setupHttpPong() {
		// Temporary pong, will get overwritten once button is clicked
		pong = new PongImpl();

		// Stage and Vbox
		final Stage newStage = new Stage();
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 10, 0, 10));

		// Add message
		Label message = new Label("Please enter the server URL");
		message.setWrapText(true);
		message.setTextAlignment(TextAlignment.CENTER);
		message.setAlignment(Pos.CENTER);

		// Textfield
		final TextField url = new TextField();
		url.setPromptText("URL");

		// Button
		Button ok = new Button("Ok");
		ok.setPrefHeight(50);
		ok.setPrefWidth(100);
		ok.setOnMouseClicked((new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				try {
					pong = new HttpPong(url.getText());
					newStage.close();
				} catch (MalformedURLException e) {
					showPopUp("Invalid URL");
				}
			}

		}));

		// Add to Vbox
		vbox.getChildren().add(message);
		vbox.getChildren().add(url);
		vbox.getChildren().add(ok);

		Scene stageScene = new Scene(vbox, 400, 200);
		newStage.setScene(stageScene);
		newStage.getScene().getStylesheets()
				.add(getClass().getResource("Material Theme.css").toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							newStage.show();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Generates a popup with message
	 * 
	 * @param message
	 *            text to display
	 */
	public void showPopUp(String message) {
		final Stage newStage = new Stage();

		// Vbox
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);

		// Message
		Label errorMessage = new Label(message);
		errorMessage.setWrapText(true);
		errorMessage.setTextAlignment(TextAlignment.CENTER);
		errorMessage.setAlignment(Pos.CENTER);

		// Ok button
		Button ok = new Button("Ok");
		ok.setPrefHeight(50);
		ok.setPrefWidth(100);
		ok.setOnMouseClicked((new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				newStage.close();
			}

		}));

		// Add to Vbox
		vbox.getChildren().add(errorMessage);
		vbox.getChildren().add(ok);

		Scene stageScene = new Scene(vbox, 400, 200);
		newStage.setScene(stageScene);
		newStage.getScene().getStylesheets()
				.add(getClass().getResource("Material Theme.css").toString());
		newStage.show();
	}
}
