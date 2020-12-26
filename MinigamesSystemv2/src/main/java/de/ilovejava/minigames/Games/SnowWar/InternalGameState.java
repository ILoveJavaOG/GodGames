package de.ilovejava.minigames.Games.SnowWar;

/**
 * Enum used to represent the internal game state
 */
public enum InternalGameState {
	//Active during loading
	INITIAL,
	//Active after loading and before fighting
	BUILDING,
	//Active after building
	FIGHTING
}
