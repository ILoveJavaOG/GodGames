package de.ilovejava.minigames.Games.SnowWar.HelperClasses;

import lombok.Getter;

/**
 * Container to keep track of the stats of a players
 */
public class PlayerStats {

	//Number of hits
	@Getter
	private final int hits;

	//Current counter to streak
	@Getter
	private final int streak;

	//Assigned team
	@Getter
	private final int team;

	/**
	 * Constructor to create the stats
	 *
	 * @param hits(int): Number of hits
	 * @param streak(int): Current streak counter
	 * @param team(int): Team of the player
	 */
	public PlayerStats(int hits, int streak, int team) {
		this.hits = hits;
		this.streak = streak;
		this.team = team;
	}

	/**
	 * Overloaded constructor call.
	 * Initialises hits and streak with 0
	 * @see PlayerStats(int, int int)
	 *
	 * @param team(int): Team of the player
	 */
	public PlayerStats(int team) {
		this(0, 0, team);
	}

}
