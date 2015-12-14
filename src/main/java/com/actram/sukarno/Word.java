package com.actram.sukarno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Word {
	public static Set<Word> loadAll(String consonantDefinition, String fileName) throws IOException {
		Objects.requireNonNull(fileName, "file name cannot be null");
		Set<Word> words = new HashSet<>();
		InputStream is = Word.class.getResourceAsStream(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			words.add(new Word(consonantDefinition, line));
		}
		br.close();
		return words;
	}

	private final String content;

	private final String consonants;

	public Word(String consonantDefinition, String content) {
		Objects.requireNonNull(consonantDefinition, "consonant definition string cannot be null");
		Objects.requireNonNull(content, "content cannot be null");
		this.content = content;
		StringBuilder builder = new StringBuilder(content.length());
		for (int i = 0; i < content.length(); i++) {
			final char character = content.charAt(i);
			if (consonantDefinition.indexOf(character) != -1) {
				builder.append(character);
			}
		}
		this.consonants = builder.toString();
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

	public String getConsonants() {
		return consonants;
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
}