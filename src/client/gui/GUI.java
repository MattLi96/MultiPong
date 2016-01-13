package client.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Main class for our GUI. Also sets the theme.
 * Most of the important GUI code is in ActionController
 */
public class GUI extends Application {

	/**
	 * The main. Calls launch.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Run at the start of our program. Basic theme setup type stuff
	 */
	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
		Scene scene = new Scene(root, 1280, 800);
		
		scene.getStylesheets().add(getClass().getResource("Material Theme.css").toString());
		stage.setTitle("The Return of Pong Legacy: A Game of Tears");
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	/**
	 * Called when you exit the GUI
	 */
	@Override
	public void stop() {
		Platform.exit();

		// Required, otherwise the redraw thread continues to run indefinitely
		System.exit(0);
	}

}
