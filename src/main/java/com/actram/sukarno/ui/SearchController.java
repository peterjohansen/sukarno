package com.actram.sukarno.ui;

import java.util.Objects;

import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Peter André Johansen
 */
public class SearchController implements StageOwner {
	private Sukarno program;
	private Stage stage;

	@FXML private TextField numberInputField;
	@FXML private ListView<String> resultList;
	@FXML private Label statusLabel;

	@Override
	public Stage getStage() {
		return stage;
	}

	@Override
	public void setStage(Sukarno program, Stage stage) {
		Objects.requireNonNull(program, "program cannot be null");
		Objects.requireNonNull(stage, "stage cannot be null");
		this.program = program;
		this.stage = stage;
	}

	@FXML
	void showConfiguration(ActionEvent evt) {
		program.getController(ConfigurationController.class).showStage();
	}
}