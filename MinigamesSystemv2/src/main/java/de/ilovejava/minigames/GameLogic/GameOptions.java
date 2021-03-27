package de.ilovejava.minigames.GameLogic;

import de.ilovejava.minigames.MapTools.Options;

/**
 * Class to represent options which are available
 */
public enum GameOptions implements Options {
	MINPLAYERS(0, "Minimum number of Players"),
	MAXPLAYERS(0, "Maximum number of Players"),
	MAXLAP(0, "Number of laps"),
	TITLE(0, "Title for the game");

	protected final Object defaultValue;
	protected final String description;

	GameOptions(Object defaultValue, String description) {
		this.defaultValue = defaultValue;
		this.description = description;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}
}
