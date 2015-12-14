package com.actram.sukarno;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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
		this.config = new Config(config);
		this.words = Collections.unmodifiableSet(words);
	}
}