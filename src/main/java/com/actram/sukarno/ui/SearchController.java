package com.actram.sukarno.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.MajorSystemSearcher;
import com.actram.sukarno.RatedWord;
import com.actram.sukarno.Word;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
	@FXML private ProgressIndicator searchProgressIndicator;
	@FXML private ListView<RatedWord> resultList;
	@FXML private Label statusLabel;

	private MajorSystemSearcher searcher;
	private Set<Word> words;
	private Task<Void> searchTask;

	private void cancelSearch() {
		if (searchTask != null && searchTask.isRunning()) {
			searchTask.cancel();
		}
		searcher = null;
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
			} else if (!newValue.isEmpty()) {
				cancelSearch();
				startSearch(newValue);
			} else {
				cancelSearch();
				resultList.getItems().clear();
			}
		});

		resultList.setCellFactory(new Callback<ListView<RatedWord>, ListCell<RatedWord>>() {
			@Override
			public ListCell<RatedWord> call(ListView<RatedWord> param) {
				return new ListCell<RatedWord>() {
					@Override
					protected void updateItem(RatedWord item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getWord() + " - " + item.getPoints());
						}
					}
				};
			}
		});

		statusLabel.setManaged(false);
		statusLabel.setVisible(false);
		searchProgressIndicator.setManaged(false);
		searchProgressIndicator.setVisible(false);

		numberInputField.setText("40491821");
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
		if (searcher != null) {
			return;
		}
		this.searcher = new MajorSystemSearcher(words, program.getConfig(), numberInputField.getText());

		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				while (searcher != null && !searcher.isDone()) {
					final RatedWord result = searcher.nextResult();
					if (result != null) {
						Platform.runLater(() -> {
							resultList.getItems().add(result);
							Collections.sort(resultList.getItems());
							statusLabel.setText(resultList.getItems().size() + " results");
						});
					}
				}
				return null;
			}
		};
		EventHandler<WorkerStateEvent> searchDone = event -> {
			cancelSearch();
		};
		searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, searchDone);
		searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, searchDone);

		resultList.getItems().clear();
		new Thread(searchTask).start();
		statusLabel.setManaged(true);
		statusLabel.setVisible(true);
		searchProgressIndicator.setManaged(true);
		searchProgressIndicator.setVisible(true);
	}
}