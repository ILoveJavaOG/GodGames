package de.ilovejava.minigames.MapTools;

import lombok.Getter;

/**
 * Enum to retrieve custom options from map
 */
public enum DefaultOptions {

	MINPLAYERS("minPlayer", 1),
	MAXPLAYERS("maxPlayer", 4),
	TITLE("title", "");

	@Getter
	private final String key;

	@Getter
	private final Object defaultValue;

	/**
	 * Constructor for the options
	 *
	 * @param key(String): Key word for the option
	 * @param defaultValue(Object): Value for the option
	 */
	DefaultOptions(String key, Object defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
}
