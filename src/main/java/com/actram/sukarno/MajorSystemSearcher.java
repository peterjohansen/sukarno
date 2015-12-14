package com.actram.sukarno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	private final Iterator<Word> wordIterator;
	private final Map<Integer, Set<Character>> digitCharMap;
	private final int[] digits;

	private final List<RatedWord> results = new ArrayList<>();

	public MajorSystemSearcher(Set<Word> words, Config config, String numberString) {
		Objects.requireNonNull(words, "word set cannot be null");
		Objects.requireNonNull(config, "config cannot be null");
		Objects.requireNonNull(numberString, "number string cannot be null");
		this.wordIterator = words.stream().filter(word -> {
			return word.consonantLength() <= numberString.length();
		}).iterator();

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
	}

	public boolean isDone() {
		return !wordIterator.hasNext();
	}

	public RatedWord nextResult() {
		Word word = wordIterator.next();
		int points = 0;

		for (int i = 0; i < word.consonantLength(); i++) {
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
			if (digits.length == word.consonantLength()) {
				points += word.consonantLength();
			}

			RatedWord result = new RatedWord(word, points);
			results.add(result);
			return result;
		} else {
			return null;
		}
	}
}