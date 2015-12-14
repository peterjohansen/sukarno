package com.actram.sukarno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.config.Config;
import com.actram.sukarno.config.Type;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class MajorSystemSearcher {
	private final Set<Word> wordSet;
	private final Map<Integer, Set<Character>> digitCharMap;

	private int[] digits;
	private int passes;
	private boolean done;

	private final List<RatedResult> results = new ArrayList<>();

	public MajorSystemSearcher(Set<Word> words, Config config, String numberString) {
		Objects.requireNonNull(words, "word set cannot be null");
		Objects.requireNonNull(config, "config cannot be null");
		Objects.requireNonNull(numberString, "number string cannot be null");
		this.wordSet = words;

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

	public List<RatedResult> nextPass() {
		wordSet.stream().filter(word -> {
			return word.consonantLength() <= digits.length;
		}).forEach(word -> {
			if (passes == 0) {
				RatedWord ratedWord = rateWord(word, 0);
				if (ratedWord != null) {
					results.add(new RatedResult(ratedWord));
				}
			} else if (!results.isEmpty()) {
				for (int i = 0; i < results.size(); i++) {
					RatedResult result = results.get(i);
					int consonantMatches = result.getTotalConsonantMatches();
					if (consonantMatches < digits.length) {
						RatedWord nextWord = rateWord(word, consonantMatches);
						if (nextWord != null) {
							results.set(i, result.addWord(nextWord));
						}
					} else {
						continue;
					}
				}
			} else {
				done = true;
			}
		});
		passes++;
		return Collections.unmodifiableList(results);
	}

	private RatedWord rateWord(Word word, int constonantStartIndex) {
		int points = 0;

		for (int i = constonantStartIndex; i < word.consonantLength(); i++) {
			Set<Character> validChars = digitCharMap.get(digits[i]);
			char consonant = word.getConsonant(i);
			if (validChars.contains(consonant)) {
				points++;
			} else {
				points = 0;
				break;
			}
		}

		if (points > 0) {
			int consonantCount = points;
			if (digits.length == word.consonantLength() - (constonantStartIndex)) {
				points += word.consonantLength();
			}
			return new RatedWord(word, points, consonantCount);
		}

		return null;
	}
}