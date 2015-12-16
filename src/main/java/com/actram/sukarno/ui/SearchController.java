package com.actram.sukarno.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.MajorSystemSearcher;
import com.actram.sukarno.RatedResult;
import com.actram.sukarno.Word;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * 
 * 
 * @author Peter André Johansen
 */
public class SearchController implements StageOwner {
	private Sukarno program;
	private Stage stage;

	@FXML private TextArea numberInputField;
	@FXML private Button searchButton;
	@FXML private ProgressIndicator searchProgressIndicator;
	@FXML private ListView<RatedResult> resultList;
	@FXML private Label statusLabel;

	private Set<Word> words;
	private MajorSystemSearcher searcher;
	private Task<Void> searchTask;

	private void cancelSearch() {
		if (searcher != null) {
			searcher.cancel();
		}
		if (searchTask != null) {
			searchTask.cancel();
		}
		numberInputField.setDisable(false);
		searchButton.setText("Search");
		searchProgressIndicator.setManaged(false);
		searchProgressIndicator.setVisible(false);
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@FXML
	void initialize() {
		this.program = Sukarno.instance;

		try {
			this.words = Word.loadAll(program.getConfig(), "/words.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		numberInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*") && !newValue.equals(oldValue)) {
				numberInputField.setText(oldValue);
			}
		});

		searchButton.disableProperty().bind(numberInputField.textProperty().isEmpty());

		resultList.setCellFactory(new Callback<ListView<RatedResult>, ListCell<RatedResult>>() {
			@Override
			public ListCell<RatedResult> call(ListView<RatedResult> param) {
				return new ListCell<RatedResult>() {
					@Override
					protected void updateItem(RatedResult item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.toString(false));
						}
					}
				};
			}
		});

		statusLabel.setManaged(false);
		statusLabel.setVisible(false);
		searchProgressIndicator.setManaged(false);
		searchProgressIndicator.setVisible(false);
	}

	@FXML
	void search(ActionEvent evt) {
		if (searchTask != null && searchTask.isRunning()) {
			cancelSearch();
		} else {
			if (!numberInputField.getText().isEmpty()) {
				resultList.getItems().clear();
				startSearch(numberInputField.getText());
			}
		}
	}

	@Override
	public void setStage(Sukarno program, Stage stage) {
		Objects.requireNonNull(program, "program cannot be null");
		Objects.requireNonNull(stage, "stage cannot be null");
		this.program = program;
		this.stage = stage;

		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
			cancelSearch();
		});
	}

	@FXML
	void showConfiguration(ActionEvent evt) {
		program.getController(ConfigurationController.class).showStage();
	}

	private void startSearch(String number) {
		if (searchTask != null && searchTask.isRunning()) {
			throw new AssertionError("search task is still running");
		}
		searcher = new MajorSystemSearcher(words, program.getConfig(), numberInputField.getText());

		searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				List<RatedResult> results = new ArrayList<>();
				while (searcher.isRunning()) {
					searcher.nextPass(results);
					Platform.runLater(() -> {
						resultList.getItems().clear();
						results.stream().filter(result -> {
							return searcher.isCompleteResult(result);
						}).forEach(result -> {
							resultList.getItems().add(result);
						});
						Collections.sort(resultList.getItems());
						statusLabel.setText(resultList.getItems().size() + " results");
					});
				}
				cancel();
				return null;
			}
		};

		searchButton.setText("Cancel");
		resultList.getItems().clear();
		new Thread(searchTask).start();
		statusLabel.setManaged(true);
		statusLabel.setVisible(true);
		searchProgressIndicator.setManaged(true);
		searchProgressIndicator.setVisible(true);
	}
}