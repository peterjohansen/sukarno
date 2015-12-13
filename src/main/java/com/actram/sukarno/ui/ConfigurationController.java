package com.actram.sukarno.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.config.BadConfigValueException;
import com.actram.sukarno.config.Config;
import com.actram.sukarno.config.Type;
import com.actram.sukarno.ui.interfaces.ModalOwner;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ConfigurationController implements StageOwner, ModalOwner {
	private Sukarno program;
	private Stage stage;

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
	@FXML private Label digitErrorLabel;

	private final Map<Type, TextField> textConfigMap = new HashMap<>();

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

	@FXML
	void exportConfiguration(ActionEvent evt) {

	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@FXML
	void importConfiguration(ActionEvent evt) {

	}

	@FXML
	void initialize() {
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

		digitErrorLabel.setText(null);

		// Update config on text field change
		textConfigMap.forEach((type, textField) -> {
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				try {
					tempConfig.set(type, Config.toCharacterSet(newValue));
				} catch (BadConfigValueException e) {
					digitErrorLabel.setText("Invalid value! Hover to view.");
					digitErrorLabel.setTooltip(new Tooltip("Error message: " + e.getMessage()));
					new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							try {
								Thread.sleep(5000);
							} catch (Exception e2) {} finally {
								Platform.runLater(() -> {
									digitErrorLabel.setText(null);
								});
							}
							return null;
						}
					}).start();
				}
				updateConfigTextFields();
			});
		});

		tempConfig.addListener((type, oldValue, newValue) -> {
			updateConfigTextFields();
		});

		tempConfig.forceListenerUpdate();

	}

	@Override
	public void saveStage() {
		try {
			program.getConfig().setTo(tempConfig);
		} catch (BadConfigValueException e) {
			e.printStackTrace();
		}
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
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	private void updateConfigTextFields() {
		textConfigMap.forEach((type, textField) -> {
			Set<Character> chars = tempConfig.get(type);
			StringBuilder builder = new StringBuilder(chars.size());
			chars.forEach(character -> {
				builder.append(character);
			});
			textField.setText(builder.toString());
		});
	}
}