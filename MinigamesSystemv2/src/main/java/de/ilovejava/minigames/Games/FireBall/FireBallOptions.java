package de.ilovejava.minigames.Games.FireBall;

import de.ilovejava.minigames.MapTools.Options;

public enum FireBallOptions implements Options  {

	NUM_FIREBALL("How many fireballs are allowed per team", 5),
	TIME_FIREBALL("Time (in seconds) when fireball should explode", 3.),
	RESPAWN_FIREBALL("Time in which fireballs should spawn", 5.0),
	NUM_TEAMS("Number of teams", 2);


	private final String description;
	private final Object defaultValue;

	FireBallOptions(String description, Object defaultValue) {
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
