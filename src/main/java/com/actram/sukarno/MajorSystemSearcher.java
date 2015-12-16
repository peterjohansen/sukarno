package com.actram.sukarno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.actram.sukarno.config.Config;
import com.actram.sukarno.config.Type;
import com.actram.sukarno.util.IntBox;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class MajorSystemSearcher {

	/**
	 * Set of every word that should be checked and, if appropriate, used for
	 * results.
	 */
	private final Set<Word> words;

	/**
	 * Maps the numbers {@code 0}-{@code 9} to a set of the valid consonant
	 * characters for that number.
	 */
	private final Map<Integer, Set<Character>> digitCharMap;

	/**
	 * The digits to find matching words for.
	 */
	private int[] digits;

	/**
	 * Indicates that the searcher is done and cannot be used anymore.
	 */
	private boolean done;

	private int passes;

	public MajorSystemSearcher(Set<Word> words, Config config, String numberString) {
		Objects.requireNonNull(words, "word set cannot be null");
		Objects.requireNonNull(config, "config cannot be null");
		Objects.requireNonNull(numberString, "number string cannot be null");
		this.words = words;

		this.digitCharMap = new HashMap<>();
		Type[] digitCharsTypes = config.getDigitCharactersTypes();
		for (int i = 0; i < digitCharsTypes.length; i++) {
			Set<Character> charSet = config.get(digitCharsTypes[i]);
			digitCharMap.put(i, charSet);
		}

		this.digits = new int[numberString.length()];
		for (int i = 0; i < digits.length; i++) {
			digits[i] = Integer.parseInt(String.valueOf(numberString.charAt(i)));
		}

		this.passes = 0;
		this.done = false;
	}

	public boolean isDone() {
		return done;
	}

	public void nextPass(List<RatedResult> results) {
		Objects.requireNonNull(results, "existing result list cannot be null");
		if (done) {
			throw new IllegalStateException("the searcher is done and cannot be used anymore");
		}

		int changeCount;
		if (passes == 0) {
			changeCount = processFirstPass(results);
		} else {
			changeCount = processNextPass(results);
		}

		if (changeCount == 0) {
			System.out.println("No more changes, exiting on pass " + passes + "...");
			done = true;
		}

		passes++;
	}

	private int processFirstPass(List<RatedResult> results) {
		IntBox changeCount = new IntBox(0);

		// Filter out as many words as possible
		Stream<Word> wordStream = words.stream().filter(word -> {
			return word.consonantLength() <= digits.length;
		});

		// Create and add a result for every word in
		// the stream since they are the first ones
		wordStream.forEach(word -> {
			RatedWord ratedWord = rateWord(word, 0);
			if (ratedWord.isMatch()) {
				results.add(new RatedResult(ratedWord));
				changeCount.increment();
			}
		});

		return changeCount.getValue();
	}

	private int processNextPass(List<RatedResult> results) {
		IntBox changeCount = new IntBox(0);

		// Filter out as many words as possible
		Stream<Word> wordStream = words.stream().filter(word -> {
			return word.consonantLength() <= digits.length;
		});

		// Expand existing results if valid
		// combinations are possible
		wordStream.forEach(word -> {
			for (int i = 0; i < results.size(); i++) {
				RatedResult result = results.get(i);
				int consonantMatches = result.getTotalConsonantMatches();
				RatedWord nextWord = rateWord(word, consonantMatches);
				if (nextWord.isMatch()) {
					results.set(i, result.addWord(nextWord));
					changeCount.increment();
				}
			}
		});

		return changeCount.getValue();
	}

	public RatedWord rateWord(Word word, int constonantStartIndex) {
		int matchCount = 0;
		if (word.consonantLength() - 1 + constonantStartIndex > digits.length - 1) {

		} else {
			for (int i = 0; i < word.consonantLength(); i++) {
				Set<Character> validChars = digitCharMap.get(digits[i + constonantStartIndex]);
				if (validChars.contains(word.getConsonant(i))) {
					matchCount++;
				} else {
					matchCount = 0;
					break;
				}
			}
		}
		return new RatedWord(word, matchCount != 0);
	}
}