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

	public RatedWord(Word word, int points) {
		Objects.requireNonNull(word, "word cannot be null");
		this.word = word;
		this.points = points;
	}
	@Override
	public int compareTo(RatedWord other) {
		return (other.points - points);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		RatedWord other = (RatedWord) obj;
		if (points != other.points) return false;
		if (word == null) {
			if (other.word != null) return false;
		} else if (!word.equals(other.word)) return false;
		return true;
	}

	public int getPoints() {
		return points;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + points;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}
}