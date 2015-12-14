package com.actram.sukarno;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.actram.sukarno.config.Config;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class MajorSystemSearcher {
	private final Set<Word> words;
	private final Config config;

	public MajorSystemSearcher(Config config, Set<Word> words) {
		Objects.requireNonNull(config, "configuration cannot be null");
		Objects.requireNonNull(words, "words cannot be null");
		if (words.isEmpty()) {
			throw new IllegalArgumentException("set of words cannot be empty");
		}
		this.words = Collections.unmodifiableSet(words);
		this.config = new Config(config);
	}

	public void findResults(Consumer<String> onNewResult) {
		Map<Word, Integer> wordPoints = new HashMap<>(words.size());
		for (Word word : words) {
			int points = 0;
			points += word.consonantLength();
			wordPoints.put(word, points);
		}

		for (Word word : words) {
			onNewResult.accept(word.toString() + " - " + wordPoints.get(word));
		}
	}
}