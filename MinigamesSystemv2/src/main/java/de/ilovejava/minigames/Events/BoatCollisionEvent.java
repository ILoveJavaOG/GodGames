package de.ilovejava.minigames.Events;

import lombok.Getter;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Class to represent any collision which can occur for boats
 */
public class BoatCollisionEvent extends Event {

	@Getter
	private final Boat boat;

	@Getter
	private final Player passenger;

	private static final HandlerList HANDLERS = new HandlerList();

	@Contract(" -> new")
	public static @NotNull HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	/**
	 * Constructor for the event
	 *
	 * @param boat(Boat): Boat which is colliding
	 * @param passenger(Player): Player who drives the boat
	 */
	public BoatCollisionEvent(Boat boat, Player passenger) {
		this.boat = boat;
		this.passenger = passenger;
	}

}
