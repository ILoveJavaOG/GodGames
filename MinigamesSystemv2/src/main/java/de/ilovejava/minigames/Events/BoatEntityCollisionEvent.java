package de.ilovejava.minigames.Events;

import lombok.Getter;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Class to represent a boat collision with an entity
 */
public class BoatEntityCollisionEvent extends BoatCollisionEvent {

	@Getter
	private final Entity hit;

	/**
	 * Constructor for the event
	 *
	 * @param boat(Boat): Boat which is colliding
	 * @param passenger(Player): Player who drives the boat
	 * @param hitTarget(Entity): Entity who got hit
	 */
	public BoatEntityCollisionEvent(Boat boat, Player passenger, Entity hitTarget) {
		super(boat, passenger);
		this.hit = hitTarget;
	}

}
