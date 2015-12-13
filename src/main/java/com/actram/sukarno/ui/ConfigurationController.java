package com.actram.sukarno.ui;

import java.util.Objects;

import com.actram.sukarno.Config;
import com.actram.sukarno.ui.interfaces.ModalOwner;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ConfigurationController implements StageOwner, ModalOwner {
	@FXML private TextField zeroTextField;
	@FXML private TextField oneTextField;
	@FXML private TextField twoTextField;
	@FXML private TextField threeTextField;
	@FXML private TextField fourTextField;
	@FXML private TextField fiveTextField;
	@FXML private TextField sixTextField;
	@FXML private TextField sevenTextField;
	@FXML private TextField eightTextField;
	@FXML private TextField nineTextField;

	private Sukarno program;
	private Stage stage;

	private final Config tempConfig = new Config();

	@FXML
	void applyChanges(ActionEvent evt) {
		saveStage();
	}

	@Override
	public void cancelStage() {
		stage.hide();
	}

	@FXML
	void discardChanges(ActionEvent evt) {
		cancelStage();
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@Override
	public void saveStage() {
		stage.hide();
	}

	@Override
	public void setStage(Sukarno program, Stage stage) {
		Objects.requireNonNull(program, "program cannot be null");
		Objects.requireNonNull(stage, "stage cannot be null");
		this.program = program;
		this.stage = stage;
	}

	@Override
	public void showStage() {
		stage.show();
		stage.setMaxWidth(stage.getWidth());
		stage.setMaxHeight(stage.getHeight());
	}
}