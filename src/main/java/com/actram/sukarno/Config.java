package com.actram.sukarno;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Config {
	public static enum Type {
		// @formatterOff
		VOWELS(String.class, (data, value) -> ((String) value).isEmpty() ? "" : null),
		CONSONANTS(String.class, (data, value) -> ((String) value).isEmpty() ? "" : null),
		ZERO_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "zero characters cannot be empty" : null),
		ONE_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "one characters cannot be empty" : null),
		TWO_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "two characters cannot be empty" : null),
		THREE_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "three characters cannot be empty" : null),
		FOUR_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "four characters cannot be empty" : null),
		FIVE_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "five characters cannot be empty" : null),
		SIX_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "six characters cannot be empty" : null),
		SEVEN_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "seven characters cannot be empty" : null),
		EIGHT_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "eight characters cannot be empty" : null),
		NINE_CHARACTERS(String.class, (data, value) -> ((String) value).isEmpty() ? "nine characters cannot be empty" : null);
		// @formatterOn

		private final Class<?> type;
		private final BiFunction<Map<Type, Object>, Object, String> validator;

		private Type(Class<?> type) {
			this(type, (data, value) -> null);
		}

		private Type(Class<?> type, BiFunction<Map<Type, Object>, Object, String> validator) {
			Objects.requireNonNull(type, "type cannot be null");
			this.type = type;
			this.validator = validator;
		}

		private void validate(Map<Type, Object> data, Object value) {
			Objects.requireNonNull(data, "data cannot be null");
			if (value == null) {
				throw new BadConfigValue("value cannot be null");
			}
			if (type.isInstance(value)) {
				throw new BadConfigValue("value must be of type: " + type.getName());
			}
			String exceptionMessage = validator.apply(data, value);
			if (exceptionMessage != null) {
				throw new BadConfigValue(exceptionMessage);
			}
		}
	}

	private final Map<Type, Object> data = new HashMap<>();

	public Config() {
		reset();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Type type) {
		Objects.requireNonNull(type, "type cannot be null");
		return (T) type.getClass().cast(data.get(type));
	}

	private void manualValidate(Type type, Object value) {
		if (type == Type.VOWELS || type == Type.CONSONANTS) {
			String s1 = (String) value;
			String s2 = (type == Type.VOWELS ? get(Type.CONSONANTS) : get(Type.VOWELS));
			for (int i = 0; i < s1.length(); i++) {
				if (s2.indexOf(s1.charAt(i)) != -1) {
					throw new BadConfigValue("vowels cannot contain characters from consonants (and vice versa): " + s1.charAt(i));
				}
			}
		}
	}

	public void reset() {
		set(Type.VOWELS, "aehiouxy");
		set(Type.CONSONANTS, "bcdfgjklmnpqrstvwz");
		set(Type.ZERO_CHARACTERS, "csz");
		set(Type.ONE_CHARACTERS, "t");
		set(Type.TWO_CHARACTERS, "n");
		set(Type.THREE_CHARACTERS, "m");
		set(Type.FOUR_CHARACTERS, "r");
		set(Type.FIVE_CHARACTERS, "l");
		set(Type.SIX_CHARACTERS, "gj");
		set(Type.SEVEN_CHARACTERS, "k");
		set(Type.EIGHT_CHARACTERS, "dfv");
		set(Type.NINE_CHARACTERS, "bp");
	}

	public void set(Type type, Object value) {
		Objects.requireNonNull(type, "type cannot be null");
		type.validate(data, value);
		manualValidate(type, value);
		data.put(type, value);
	}

	public void setTo(Config config) {
		Objects.requireNonNull(config, "config cannot be null");
		for (Type type : Type.values()) {
			config.set(type, get(type));
		}
	}
}