package com.actram.sukarno.ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.MajorSystemSearcher;
import com.actram.sukarno.Word;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * 
 * @author Peter André Johansen
 */
public class SearchController implements StageOwner {
	private Sukarno program;
	private Stage stage;

	@FXML private TextArea numberInputField;
	@FXML private ProgressIndicator searchProgressIndicator;
	@FXML private ListView<String> resultList;
	@FXML private Label statusLabel;

	private MajorSystemSearcher majorSystemSearcher;
	private Set<Word> words;
	private Task<Void> searchTask;

	private void cancelSearch() {
		if (searchTask != null && searchTask.isRunning()) {
			searchTask.cancel();
		}
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@FXML
	void initialize() {
		this.program = Sukarno.instance;

		numberInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*") && !newValue.equals(oldValue)) {
				numberInputField.setText(oldValue);
			} else if (!newValue.isEmpty()) {
				startSearch();
			}
		});

		searchProgressIndicator.setManaged(false);
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

	private void startSearch() {
		cancelSearch();
		if (words == null) {
			try {
				words = Word.loadAll(program.getConfig(), "/words.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.majorSystemSearcher = new MajorSystemSearcher(program.getConfig(), words);
		}
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				majorSystemSearcher.findResults(result -> {
					Platform.runLater(() -> {
						resultList.getItems().add(result);
						statusLabel.setText(resultList.getItems().size() + " results");
					});
				});
				return null;
			}
		};
		EventHandler<WorkerStateEvent> searchDone = event -> {
			searchProgressIndicator.setManaged(false);
			searchProgressIndicator.setVisible(false);
		};
		searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, searchDone);
		searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, searchDone);

		resultList.getItems().clear();
		new Thread(searchTask).start();
		searchProgressIndicator.setManaged(true);
		searchProgressIndicator.setVisible(true);
	}
}