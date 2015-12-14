package com.actram.sukarno;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 *
 * @author Peter André Johansen
 */
public class RatedResult implements Comparable<RatedResult> {
	private final List<RatedWord> ratedWords;

	private RatedResult(List<RatedWord> existingRatedWords, RatedWord word) {
		Objects.requireNonNull(existingRatedWords, "rated word list cannot be null");
		Objects.requireNonNull(word, "word cannot be null");

		this.ratedWords = new ArrayList<>(existingRatedWords.size() + 1);
		ratedWords.addAll(existingRatedWords);
		if (word != null) {
			ratedWords.add(word);
		}
	}

	public RatedResult(RatedWord word) {
		this(new ArrayList<>(0), word);
	}

	public RatedResult addWord(RatedWord word) {
		return new RatedResult(ratedWords, word);
	}

	@Override
	public int compareTo(RatedResult other) {
		return (other.getTotalPoints() - this.getTotalPoints());
	}

	public void forEachWord(Consumer<Word> consumer) {
		Objects.requireNonNull(consumer, "word consumer cannot be null");
		for (RatedWord word : ratedWords) {
			consumer.accept(word.getWord());
		}
	}

	public RatedWord getLastWord() {
		return ratedWords.get(ratedWords.size() - 1);
	}

	public int getTotalConsonantMatches() {
		int sum = 0;
		for (RatedWord word : ratedWords) {
			sum += word.getConsonantMatches();
		}
		return sum;
	}

	public int getTotalPoints() {
		int sum = 0;
		for (RatedWord word : ratedWords) {
			sum += word.getPoints();
		}
		return sum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (RatedWord word : ratedWords) {
			if (builder.length() != 0) {
				builder.append(" ");
			}
			builder.append(word.getWord().getContent());
		}
		builder.append(" (" + getTotalPoints() + ")");
		return builder.toString();
	}
}