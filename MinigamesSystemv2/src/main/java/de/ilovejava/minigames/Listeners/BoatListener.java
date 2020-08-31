package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Events.BoatBlockCollisionEvent;
import de.ilovejava.minigames.Events.BoatEntityCollisionEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener to handle boat events
 */
public class BoatListener implements Listener {

	/**
	 * Method to get colliding blocks
	 *
	 * @param boat(Boat): Boat to check for
	 *
	 * @return List of blocks boat is colliding with
	 */
	private @NotNull List<Block> collidingBlocks(@NotNull Boat boat) {
		List<Block> collidingBlocks = new ArrayList<>(8);
		//Check blocks around boat
		for (int x : new int[]{1, 0, -1}) {
			for (int y : new int[]{1, 0, -1}) {
				for (int z: new int[]{1, 0, -1}) {
					Block colliding = boat.getLocation().add(x, y, z).getBlock();
					//Skip ice and air
					if (colliding.getType() != Material.AIR &&
						colliding.getType() != Material.ICE &&
					    colliding.getType() != Material.BLUE_ICE &&
					    colliding.getType() != Material.FROSTED_ICE &&
					    colliding.getType() != Material.PACKED_ICE) {
						collidingBlocks.add(colliding);
					}
				}
			}
		}
		return collidingBlocks;
	}

	/**
	 * Method to get colliding entities
	 *
	 * @param boat(Boat): Boat to check for
	 *
	 * @return List of entities boat is colliding with
	 */
	private @NotNull List<Entity> collidingEntities(@NotNull Boat boat) {
		List<Entity> entities = boat.getNearbyEntities(0.25, 0.25, 0.25);
		//Filter passengers and boat itself
		entities.remove(boat);
		entities.removeAll(boat.getPassengers());
		return entities;
	}

	/**
	 * Event on boat move
	 *
	 * @param event(VehicleMoveEvent): Move event
	 */
	@EventHandler
	public void onBoatMove(@NotNull VehicleMoveEvent event) {
		Vehicle moving = event.getVehicle();
		//Check if the boat is moving and has a passenger
		if (moving instanceof Boat && moving.getPassengers().size() == 1) {
			Boat boat = (Boat) moving;
			Entity passenger = moving.getPassengers().get(0);
			//Check if the passenger is a player and
			if (passenger instanceof Player) {
				Player player = (Player) passenger;
				//Check the game and call events
				if (Tracker.isInGame(player)) {
					collidingEntities(boat).forEach(entity -> {
						BoatEntityCollisionEvent entityCollisionEvent = new BoatEntityCollisionEvent(boat, player, entity);
						Tracker.getGame(player).callEvent(entityCollisionEvent);
					});
					collidingBlocks(boat).forEach(block -> {
						BoatBlockCollisionEvent blockCollisionEvent = new BoatBlockCollisionEvent(boat, player, block);
						Tracker.getGame(player).callEvent(blockCollisionEvent);
					});
				}
			}
		}
	}

	/**
	 * Event on boat exit
	 *
	 * @param event(VehicleExitEvent): Exit event
	 */
	@EventHandler
	public void onExit(VehicleExitEvent event) {
		Vehicle vehicle = event.getVehicle();
		if (vehicle instanceof Boat && vehicle.getPassengers().size() == 1) {
			Entity passenger = vehicle.getPassengers().get(0);
			if (passenger instanceof Player) {
				Player player = (Player) passenger;
				//Get the game and call the event
				if (Tracker.isInGame(player)) {
					Tracker.getGame(player).callEvent(event);
				}
			}
		}
	}
}
