package com.actram.sukarno.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Sukarno extends Application {
	public static void main(String[] args) {
		Application.launch(Sukarno.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/search.fxml"));
		stage.setScene(new Scene(root));
		stage.show();
		stage.setMaxWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}
}