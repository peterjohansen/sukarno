package com.actram.sukarno.ui;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.actram.sukarno.MajorSystemSearcher;
import com.actram.sukarno.RatedResult;
import com.actram.sukarno.Word;
import com.actram.sukarno.config.Config;
import com.actram.sukarno.ui.interfaces.StageOwner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Sukarno extends Application {
	private static final String TITLE = "Sukarno";

	static Sukarno instance;

	public static void main(String[] args) throws Exception {
		final String NUMBER = "40491821";

		Config config = new Config();
		Set<Word> words = Word.loadAll(config, "/words.txt");
		MajorSystemSearcher searcher = new MajorSystemSearcher(words, config, NUMBER);
		List<RatedResult> results = new ArrayList<>();
		while (!searcher.isDone()) {
			searcher.nextPass(results);

			// @formatterOff
			results.stream()
					.sorted()
					.collect(Collectors.toCollection(ArrayDeque::new))
					.descendingIterator()
					.forEachRemaining(result -> {
						System.out.println(result);
					});
			// @formatterOn
			System.out.println();
			System.out.println("size=" + results.size());
			System.out.println();
		}

		System.exit(1);
		Application.launch(Sukarno.class, args);
	}

	private final Map<Class<?>, Object> controllers = new HashMap<>();
	private final Config config = new Config();

	public Config getConfig() {
		return config;
	}

	public <T> T getController(Class<T> type) {
		Objects.requireNonNull(type, "controller type cannot be null");
		return type.cast(controllers.get(type));
	}

	@SuppressWarnings("unchecked")
	private <T extends StageOwner> T giveStage(Object object, Stage stage) {
		Objects.requireNonNull(object, "object cannot be null");
		Objects.requireNonNull(stage, "stage cannot be null");

		stage.getIcons().add(new Image("icon.png"));
		((StageOwner) object).setStage(this, stage);
		return (T) object;
	}

	private void loadController(String fxmlFile, BiConsumer<Object, Parent> controllerAction) {
		Objects.requireNonNull(fxmlFile, "fxml file cannot be null");
		Objects.requireNonNull(controllerAction, "controller action cannot be null");

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlFile));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object controller = loader.getController();
		controllerAction.accept(controller, root);
		this.controllers.put(controller.getClass(), controller);
	}

	public Stage newModalStage(StageOwner owner, Parent root) {
		return newModalStage(owner, root, null);
	}

	public Stage newModalStage(StageOwner owner, Parent root, String title) {
		Objects.requireNonNull(owner, "stage owner cannot be null");
		Objects.requireNonNull(root, "root cannot be null");

		Stage stage = new Stage();
		stage.initOwner(owner.getStage());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(new Scene(root));
		setTitle(stage, title);
		return stage;
	}

	public void setTitle(Stage stage, String title) {
		Objects.requireNonNull(stage, "stage cannot be null");
		stage.setTitle(title == null ? TITLE : title + " - " + TITLE);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Sukarno.instance = this;

		loadController("/search.fxml", (controller, root) -> {
			giveStage(controller, primaryStage);
			primaryStage.setScene(new Scene(root));
			setTitle(primaryStage, null);
		});

		loadController("/configuration.fxml", (controller, root) -> {
			giveStage(controller, newModalStage(getController(SearchController.class), root, "Configuration"));
		});

		primaryStage.show();
		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());
	}
}