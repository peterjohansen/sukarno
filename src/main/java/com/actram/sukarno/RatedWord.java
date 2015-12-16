package com.actram.sukarno;

import java.util.Objects;

/**
 *
 *
 * @author Peter André Johansen
 */
public class RatedWord {
	private final Word word;
	private boolean matches;

	public RatedWord(Word word, boolean matches) {
		Objects.requireNonNull(word, "word cannot be null");
		this.word = word;
		this.matches = matches;
	}

	public Word getWord() {
		return word;
	}

	public boolean isMatch() {
		return matches;
	}

	@Override
	public String toString() {
		return word.toString();
	}
}