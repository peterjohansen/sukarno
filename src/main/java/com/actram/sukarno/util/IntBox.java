package com.actram.sukarno.util;

/**
 *
 *
 * @author Peter André Johansen
 */
public class IntBox {
	private int value;

	public IntBox(int value) {
		this.value = value;
	}

	public int decrement() {
		return --value;
	}

	public int getValue() {
		return value;
	}

	public int increment() {
		return ++value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}