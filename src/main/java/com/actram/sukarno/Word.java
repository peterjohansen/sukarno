package com.actram.sukarno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.actram.sukarno.config.Config;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Word {
	public static Set<Word> loadAll(Config config, String fileName) throws IOException {
		Objects.requireNonNull(config, "config cannot be null");
		Objects.requireNonNull(fileName, "file name cannot be null");

		StringBuilder consonants = new StringBuilder();
		config.forEachDigitCharacterMap((type, value) -> {
			Set<Character> chars = Config.cast(value);
			for (Character character : chars) {
				consonants.append(character);
			}
		});

		Set<Word> words = new HashSet<>();
		InputStream is = Word.class.getResourceAsStream(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			words.add(new Word(consonants.toString(), line));
		}
		br.close();
		return Collections.unmodifiableSet(words);
	}

	private final String content;
	private final Character[] consonants;

	public Word(String consonantDefinition, String content) {
		Objects.requireNonNull(consonantDefinition, "consonant definition string cannot be null");
		Objects.requireNonNull(content, "content cannot be null");
		this.content = content;
		List<Character> consonantList = new ArrayList<>();
		for (int i = 0; i < content.length(); i++) {
			final char character = content.charAt(i);
			if (consonantDefinition.indexOf(character) != -1) {
				consonantList.add(character);
			}
		}
		this.consonants = consonantList.toArray(new Character[consonantList.size()]);
	}

	public int consonantLength() {
		return consonants.length;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Word other = (Word) obj;
		if (consonants == null) {
			if (other.consonants != null) return false;
		} else if (!consonants.equals(other.consonants)) return false;
		if (content == null) {
			if (other.content != null) return false;
		} else if (!content.equals(other.content)) return false;
		return true;
	}

	public char getConsonant(int index) {
		return consonants[index];
	}

	public String getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consonants == null) ? 0 : consonants.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	public boolean isConsonantIndex(int index) {
		return (index >= 0 && index <= consonantLength() - 1);
	}

	public int length() {
		return content.length();
	}

	@Override
	public String toString() {
		return content;
	}
}