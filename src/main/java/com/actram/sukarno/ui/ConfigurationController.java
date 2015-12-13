package com.actram.sukarno.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.actram.sukarno.config.BadConfigValueException;
import com.actram.sukarno.config.Config;
import com.actram.sukarno.config.Type;
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
	@FXML private TextField vowelTextField;
	@FXML private TextField consonantsTextField;
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

	@FXML
	void initialize() {
		Map<Type, TextField> textConfigMap = new HashMap<>();
		textConfigMap.put(Type.VOWELS, vowelTextField);
		textConfigMap.put(Type.CONSONANTS, consonantsTextField);
		textConfigMap.put(Type.ZERO_CHARACTERS, zeroTextField);
		textConfigMap.put(Type.ONE_CHARACTERS, oneTextField);
		textConfigMap.put(Type.TWO_CHARACTERS, twoTextField);
		textConfigMap.put(Type.THREE_CHARACTERS, threeTextField);
		textConfigMap.put(Type.FOUR_CHARACTERS, fourTextField);
		textConfigMap.put(Type.FIVE_CHARACTERS, fiveTextField);
		textConfigMap.put(Type.SIX_CHARACTERS, sixTextField);
		textConfigMap.put(Type.SEVEN_CHARACTERS, sevenTextField);
		textConfigMap.put(Type.EIGHT_CHARACTERS, eightTextField);
		textConfigMap.put(Type.NINE_CHARACTERS, nineTextField);

		// Update config on text field change
		textConfigMap.forEach((type, textField) -> {
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				try {
					tempConfig.set(type, newValue);
				} catch (BadConfigValueException e) {
					textField.setText(oldValue);
				}
			});
		});

		tempConfig.addListener((type, oldValue, newValue) -> {

			// Update text field on config change
			if (textConfigMap.containsKey(type)) {
				textConfigMap.get(type).setText(newValue.toString());
			}

		});

		tempConfig.forceListenerUpdate();
	}

	@Override
	public void saveStage() {
		program.getConfig().setTo(tempConfig);
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
		tempConfig.setTo(program.getConfig());
		stage.show();
		stage.setMaxWidth(stage.getWidth());
		stage.setMaxHeight(stage.getHeight());
	}
}