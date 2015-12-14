package com.actram.sukarno.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Config {
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object) {
		Objects.requireNonNull(object, "object cannot be null");
		return (T) object;
	}

	public static Set<Character> toCharacterSet(String str) {
		Objects.requireNonNull(str, "string cannot be null");
		Set<Character> chars = new ConcurrentSkipListSet<>();
		for (int i = 0; i < str.length(); i++) {
			chars.add(str.charAt(i));
		}
		return Collections.unmodifiableSet(chars);
	}

	private final Map<Type, Object> data = new HashMap<>();

	private final List<ConfigListener> listeners = new ArrayList<>();

	public Config() {
		reset();
	}

	public Config(Config config) {
		setTo(config);
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

	public void forEachDigitCharacterMap(BiConsumer<Type, Object> consumer) {
		Objects.requireNonNull(consumer, "consumer cannot be null");
		Type[] digitCharsTypes = getDigitCharactersTypes();
		for (Type digitCharsType : digitCharsTypes) {
			consumer.accept(digitCharsType, get(digitCharsType));
		}
	}

	public <T> T get(Type type) {
		Objects.requireNonNull(type, "type cannot be null");
		synchronized (data) {
			return cast(data.get(type));
		}
	}

	public Type[] getDigitCharactersTypes() {
		Type[] digitCharsTypes = new Type[10];
		for (int i = 0; i < digitCharsTypes.length; i++) {
			if (Type.values()[i].ordinal() < Type.ZERO_CHARACTERS.ordinal()) {
				continue;
			}
			if (Type.values()[i].ordinal() > Type.NINE_CHARACTERS.ordinal()) {
				continue;
			}
			digitCharsTypes[i] = Type.values()[i];
		}
		return digitCharsTypes;
	}

	private void manualValidate(Type type, Object value) {
		forEachDigitCharacterMap((otherType, otherValue) -> {
			if (type != otherType) {
				if (!Collections.disjoint(cast(value), cast(otherValue))) {
					throw new BadConfigValueException("number-character values cannot be share characters");
				}
			}
		});
	}

	public void removeListener(ConfigListener listener) {
		Objects.requireNonNull(listener, "change listener cannot be null");
		this.listeners.remove(listener);
	}

	public void reset() {
		uncheckedSet(Type.ZERO_CHARACTERS, toCharacterSet("csz"));
		uncheckedSet(Type.ONE_CHARACTERS, toCharacterSet("t"));
		uncheckedSet(Type.TWO_CHARACTERS, toCharacterSet("n"));
		uncheckedSet(Type.THREE_CHARACTERS, toCharacterSet("m"));
		uncheckedSet(Type.FOUR_CHARACTERS, toCharacterSet("r"));
		uncheckedSet(Type.FIVE_CHARACTERS, toCharacterSet("l"));
		uncheckedSet(Type.SIX_CHARACTERS, toCharacterSet("gj"));
		uncheckedSet(Type.SEVEN_CHARACTERS, toCharacterSet("k"));
		uncheckedSet(Type.EIGHT_CHARACTERS, toCharacterSet("dfv"));
		uncheckedSet(Type.NINE_CHARACTERS, toCharacterSet("bp"));
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
			uncheckedSet(type, config.get(type));
		}
	}

	private void uncheckedSet(Type type, Object value) {
		data.put(type, value);
	}
}