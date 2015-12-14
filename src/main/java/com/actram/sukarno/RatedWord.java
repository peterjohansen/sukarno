package com.actram.sukarno;

import java.util.Objects;

/**
 *
 *
 * @author Peter André Johansen
 */
public class RatedWord implements Comparable<RatedWord> {
	private final Word word;
	private final int points;
	private final int consonantMatches;

	public RatedWord(Word word, int points, int consonantMatches) {
		Objects.requireNonNull(word, "word cannot be null");
		this.word = word;
		this.points = points;
		this.consonantMatches = consonantMatches;
	}

	@Override
	public int compareTo(RatedWord other) {
		return (other.points - points);
	}

	public int getConsonantMatches() {
		return consonantMatches;
	}

	public int getPoints() {
		return points;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public String toString() {
		return word.toString();
	}
}