package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import game.Pong;

/**
 * Controller for the GUI, initializes the view. Contains most of the important code.
 * @author Matthew
 */
public class ActionController implements Initializable {
	/** The anchor pane that the pong game is drawn on */
	@FXML
	private AnchorPane world;
	
	/** The user title pane and it's properties */
	@FXML
	private TitledPane userTitlePane;
	@FXML
	private TextField username;
	@FXML
	private Button joinGame;
	@FXML
	private Button leaveGame;
	
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
	private GridPane scoreGrid;
	
	
	/** The pong game the GUI interacts with */
	private Pong pong;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub. Add in server chooser here
	}

	/**
	 * Adds the player to a game
	 */
	public void joinGame(){
		//TODO implement
	}
	
	/**
	 * Remove the player from a game
	 */
	public void leaveGame(){
		//TODO implement
	}
	
	/**
	 * Start a game using the given amount of lives
	 */
	public void startGame(){
		//TODO implement
	}
	
	/**
	 * End a game
	 */
	public void endGame(){
		//TODO implement
	}
	
	
	/**
	 * Generates a popup with message
	 * @param message text to display
	 */
	public void showPopUp(String message){
		final Stage newStage = new Stage();
		VBox comp = new VBox(10);
		comp.setAlignment(Pos.CENTER);
		Label errorMessage = new Label(message);
		errorMessage.setWrapText(true);
		errorMessage.setTextAlignment(TextAlignment.CENTER);
		errorMessage.setAlignment(Pos.CENTER);
		Button ok = new Button("Ok");
		ok.setPrefHeight(50);
		ok.setPrefWidth(100);
		ok.setOnMouseClicked((new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0)
			{
				newStage.hide();
			}

		}));
		comp.getChildren().add(errorMessage);
		comp.getChildren().add(ok);
		Scene stageScene = new Scene(comp, 400, 200);
		newStage.setScene(stageScene);
		newStage.getScene().getStylesheets().add(getClass().getResource("Material Theme.css").toString());
		newStage.show();
	}
}

//TODO: example of javafx timeline
//final Duration oneFrameAmt = Duration.millis(1000/60);
//final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
//   new EventHandler() {
// 
//   @Override
//   public void handle(javafx.event.ActionEvent event) {
// 
//      // update actors
//      updateSprites();
// 
//      // check for collision
//      checkCollisions();
// 
//      // removed dead things
//      cleanupSprites();
// 
//   }
//}); // oneFrame
// 
//// sets the game world's game loop (Timeline)
//TimelineBuilder.create()
//   .cycleCount(Animation.INDEFINITE)
//   .keyFrames(oneFrame)
//   .build()
//   .play();
