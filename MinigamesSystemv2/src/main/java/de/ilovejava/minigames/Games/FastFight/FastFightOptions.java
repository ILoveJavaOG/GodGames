package de.ilovejava.minigames.Games.FastFight;

import de.ilovejava.minigames.MapTools.Options;

public enum FastFightOptions implements Options {


	DASH_COOLDOWN("Dash forward", 3.5),
	JUMP_COOLDOWN("Jump up", 5.0),
	PUSH_COOLDOWN("Push away", 5.5),
	DASH_SCALE("How strong dash is", 1.3),
	JUMP_SCALE("How strong to jump up (should be positive!)", 0.8),
	PUSH_SCALE("How strong push is", 1.5);


	private final String description;
	private final Double cooldown;

	FastFightOptions(String description, double cooldown) {
		this.description = description;
		this.cooldown = cooldown;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Object getDefaultValue() {
		return cooldown;
	}
}
