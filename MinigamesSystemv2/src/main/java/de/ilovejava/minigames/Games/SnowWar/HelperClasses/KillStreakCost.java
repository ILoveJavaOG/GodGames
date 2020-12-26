package de.ilovejava.minigames.Games.SnowWar.HelperClasses;

import de.ilovejava.minigames.Games.SnowWar.KillStreak;
import lombok.Getter;

/**
 * Pair class to map a kill streak to the number of kills
 */
public class KillStreakCost {

	//Kill streak which is used
	@Getter
	private final KillStreak streak;

	//Cost of the kill streak
	@Getter
	private final int cost;

	/**
	 * Constructor to create the pair
	 *
	 * @param streak(KillStreak): The kill streak
	 * @param cost(Integer): Cost for the kill streak
	 */
	public KillStreakCost(KillStreak streak, int cost) {
		this.streak = streak;
		this.cost = cost;
	}

}
