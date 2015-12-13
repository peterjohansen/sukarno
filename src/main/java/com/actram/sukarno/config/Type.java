package com.actram.sukarno.config;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

/**
 *
 *
 * @author Peter André Johansen
 */
public enum Type {
	// @formatterOff
	VOWELS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "vowels cannot be empty" : null),
	CONSONANTS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "consonants cannot be empty" : null),
	ZERO_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "zero characters cannot be empty" : null),
	ONE_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "one characters cannot be empty" : null),
	TWO_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "two characters cannot be empty" : null),
	THREE_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "three characters cannot be empty" : null),
	FOUR_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "four characters cannot be empty" : null),
	FIVE_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "five characters cannot be empty" : null),
	SIX_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "six characters cannot be empty" : null),
	SEVEN_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "seven characters cannot be empty" : null),
	EIGHT_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "eight characters cannot be empty" : null),
	NINE_CHARACTERS(Set.class, (data, value) -> ((Set<?>) value).isEmpty() ? "nine characters cannot be empty" : null);
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

	void validate(Map<Type, Object> data, Object value) {
		Objects.requireNonNull(data, "data cannot be null");
		if (value == null) {
			throw new BadConfigValueException("value cannot be null");
		}
		if (value.getClass().isInstance(type)) {
			throw new BadConfigValueException("value must be of type: " + type.getName());
		}
		String exceptionMessage = validator.apply(data, value);
		if (exceptionMessage != null) {
			throw new BadConfigValueException(exceptionMessage);
		}
	}
}