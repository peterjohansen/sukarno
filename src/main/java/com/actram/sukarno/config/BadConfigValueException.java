package com.actram.sukarno.config;

/**
 *
 *
 * @author Peter André Johansen
 */
public class BadConfigValueException extends RuntimeException {
	private static final long serialVersionUID = 2850325200661535693L;

	public BadConfigValueException(String message) {
		super(message);
	}
}