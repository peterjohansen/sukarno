package com.actram.sukarno.config;

/**
 *
 *
 * @author Peter André Johansen
 */
public interface ConfigListener {
	public void configUpdated(Type type, Object oldValue, Object newValue);
}