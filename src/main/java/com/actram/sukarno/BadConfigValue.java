package com.actram.sukarno;

/**
 *
 *
 * @author Peter André Johansen
 */
public class BadConfigValue extends RuntimeException {
	private static final long serialVersionUID = 2850325200661535693L;

	public BadConfigValue(String message) {
		super(message);
	}
}