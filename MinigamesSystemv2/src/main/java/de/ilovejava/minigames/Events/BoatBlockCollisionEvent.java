package de.ilovejava.minigames.Events;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;

/**
 * Class to represent a collision event with a block
 */
public class BoatBlockCollisionEvent extends BoatCollisionEvent{

	@Getter
	private final Block hit;

	/**
	 * Constructor for the event
	 *
	 * @param boat(Boat): Boat which is colliding
	 * @param passenger(Player): Player who drives the boat
	 * @param hitTarget(Block): Block which got hit
	 */
	public BoatBlockCollisionEvent(Boat boat, Player passenger, Block hitTarget) {
		super(boat, passenger);
		this.hit = hitTarget;
	}

}
