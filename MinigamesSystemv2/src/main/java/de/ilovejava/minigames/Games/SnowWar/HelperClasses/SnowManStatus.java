package de.ilovejava.minigames.Games.SnowWar.HelperClasses;

import lombok.Getter;
import org.bukkit.entity.Snowman;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pair class to represent how many snow balls a snowman has dropped
 */
public class SnowManStatus {

	//Count of the dropped snowballs
	@Getter
	private final AtomicInteger count = new AtomicInteger(0);

	//The snowman which is used
	@Getter
	private final Snowman snowman;

	/**
	 * Constructor for the status
	 *
	 * @param snowman(Snowman): Snowman which is dropping the snowballs
	 */
	public SnowManStatus(Snowman snowman) {
		this.snowman = snowman;
	}
}
