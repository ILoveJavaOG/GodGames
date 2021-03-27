package de.ilovejava.minigames.MapTools;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public interface Optionable {

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 *
	 * @return Object at key. May be null
	 */
	@Nullable
	Object getOption(String key);

	<E extends Enum<E> & Options> Object getOption(E key);

	default Integer getIntOption(String key) {
		return (Integer) getOption(key);
	}

	default <E extends Enum<E> & Options> Integer getIntOption(E key) {
		return getIntOption(key.name());
	}

	default Double getDoubleOption(String key) {
		return (Double) getOption(key);
	}

	default <E extends Enum<E> & Options> Double getDoubleOption(E key) {
		return (Double) getOption(key);
	}

	default String getStringOption(String key) {
		return (String) getOption(key);
	}

	default  <E extends Enum<E> & Options> String getStringOption(E key) {
		return (String) getOption(key);
	}

}
