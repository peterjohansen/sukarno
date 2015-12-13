package com.actram.sukarno.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Config {
	private final Map<Type, Object> data = new HashMap<>();
	private final List<ConfigListener> listeners = new ArrayList<>();

	public Config() {
		reset();
	}

	public void addListener(ConfigListener listener) {
		Objects.requireNonNull(listener, "change listener cannot be null");
		this.listeners.add(listener);
	}

	public void forceListenerUpdate() {
		for (Type type : data.keySet()) {
			listeners.forEach(listener -> listener.configUpdated(type, get(type), get(type)));
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Type type) {
		Objects.requireNonNull(type, "type cannot be null");
		return (T) data.get(type);
	}

	private void manualValidate(Type type, Object value) {
		if (type == Type.VOWELS || type == Type.CONSONANTS) {
			String s1 = (String) value;
			String s2 = (type == Type.VOWELS ? get(Type.CONSONANTS) : get(Type.VOWELS));
			for (int i = 0; i < s1.length(); i++) {
				if (s2.indexOf(s1.charAt(i)) != -1) {
					throw new BadConfigValueException("vowels cannot contain characters from consonants (and vice versa): " + s1.charAt(i));
				}
			}
		}
	}

	public void removeListener(ConfigListener listener) {
		Objects.requireNonNull(listener, "change listener cannot be null");
		this.listeners.remove(listener);
	}

	public void reset() {
		uncheckedSet(Type.VOWELS, "aehiouxy");
		uncheckedSet(Type.CONSONANTS, "bcdfgjklmnpqrstvwz");
		uncheckedSet(Type.ZERO_CHARACTERS, "csz");
		uncheckedSet(Type.ONE_CHARACTERS, "t");
		uncheckedSet(Type.TWO_CHARACTERS, "n");
		uncheckedSet(Type.THREE_CHARACTERS, "m");
		uncheckedSet(Type.FOUR_CHARACTERS, "r");
		uncheckedSet(Type.FIVE_CHARACTERS, "l");
		uncheckedSet(Type.SIX_CHARACTERS, "gj");
		uncheckedSet(Type.SEVEN_CHARACTERS, "k");
		uncheckedSet(Type.EIGHT_CHARACTERS, "dfv");
		uncheckedSet(Type.NINE_CHARACTERS, "bp");
	}

	public void set(Type type, Object value) {
		Objects.requireNonNull(type, "type cannot be null");
		type.validate(data, value);
		manualValidate(type, value);
		uncheckedSet(type, value);
	}

	public void setTo(Config config) {
		Objects.requireNonNull(config, "config cannot be null");
		for (Type type : data.keySet()) {
			if (!get(type).equals(config.get(type))) {
				listeners.forEach(listener -> listener.configUpdated(type, config.get(type), get(type)));
				config.uncheckedSet(type, get(type));
			}
		}
	}

	private void uncheckedSet(Type type, Object value) {
		data.put(type, value);
	}
}