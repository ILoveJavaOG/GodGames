package de.ilovejava.minigames.Games.IceScooter;

import com.mojang.datafixers.util.Pair;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Events.BoatBlockCollisionEvent;
import de.ilovejava.minigames.Events.BoatEntityCollisionEvent;
import de.ilovejava.minigames.Games.IceScooter.Items.IceScooterItems;
import de.ilovejava.minigames.Items.GameItem;
import de.ilovejava.minigames.Items.ItemBox;
import de.ilovejava.minigames.MapTools.CheckPoint;
import de.ilovejava.minigames.MapTools.ProbabilityMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Class to represent events during the race
 */
public class IceScooterTimeTrialEvents {

	/**
	 * Method to prevent players from exiting during the race
	 *
	 * @param event(VehicleExitEvent): Event to cancel
	 */
	public void onExitVehicle(@NotNull VehicleExitEvent event) {
		if (event.getVehicle() instanceof Boat && event.getExited() instanceof Player) {
			event.setCancelled(true);
		}
	}

	/**
	 * Method to handle collisions with entities
	 *
	 * @param event(BoatEntityCollisionEvent): Collision event
	 */
	public void onBoatEntityCollision(@NotNull BoatEntityCollisionEvent event){
		//ItemBox hit
		if (event.getHit() instanceof EnderCrystal) {
			//Remove crystal
			event.getHit().remove();
			Player player = event.getPassenger();
			//Check if the player has no item box
			if (!ItemBox.hasBox(player)) {
				//Build probabilities for items
				IceScooterTimeTrial race = (IceScooterTimeTrial) Tracker.getGame(player);
				ProbabilityMap probabilities = race.getGameMap().getProbabilities();
				probabilities.setPlaces(race.getNumPlayers());
				List<GameItem> items = new ArrayList<>();
				List<Double> possibilities = new ArrayList<>();
				for (String item : probabilities.getItems()) {
					IceScooterItems enumValue = IceScooterItems.valueOf(item);
					items.add(enumValue.createItem(player));
					possibilities.add(probabilities.getProb(item, player.getLevel()));
				}
				ItemBox.addBox(new ItemBox(event.getPassenger(), items, possibilities), event.getPassenger());
			}
		}
	}

	//Set of player who should not receive a jump
	private final HashSet<Player> inAir = new HashSet<>();

	/**
	 * Method to handle collision with blocks
	 *
	 * @param event(BoatBlockCollisionEvent): Collision event
	 */
	public void onBoatBlockCollision(@NotNull BoatBlockCollisionEvent event) {
		//Drive up snow layers
		if (event.getHit().getType() == Material.SNOW) {
			Vector direction = event.getBoat().getLocation().getDirection();
			direction.setY(direction.getY() + 0.1);
			//Change velocity before task to minimize stops in mid air
			event.getBoat().setVelocity(direction);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> event.getBoat().setVelocity(direction), 1L);
		}
		else if (!inAir.contains(event.getPassenger()) && (event.getHit().getType() == Material.SLIME_BLOCK || event.getHit().getType() == Material.HONEY_BLOCK)) {
			double factor = event.getHit().getType() == Material.HONEY_BLOCK ? 1.5 : 2.0;
			inAir.add(event.getPassenger());
			Player player = event.getPassenger();
			Vector direction = event.getBoat().getLocation().getDirection();
			if (!direction.equals(new Vector(0.0, 0.0, 0.0))) {
				Vector jump = direction.setY(0.0).normalize().setY(0.6).normalize().multiply(factor);
				//Change velocity before task to minimize stops in mid air
				event.getBoat().setVelocity(jump);
				//For some reason boats need to delay this...
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> event.getBoat().setVelocity(jump), 1L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> inAir.remove(player), 20L);
			}
		}
	}

	/**
	 * Method to handle interactions
	 *
	 * @param event(PlayerInteractEvent): Interact event
	 */
	public void onRightClick(@NotNull PlayerInteractEvent event) {
		event.setCancelled(true);
		//Activate the item if players right clicks
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (ItemBox.hasBox(event.getPlayer())) {
				ItemBox box = ItemBox.getBox(event.getPlayer());
				if (box.isActive()) {
					box.getActiveItem().activate();
				}
			}
		}
	}

	/**
	 * Method to handle moving of boats
	 *
	 * @param event(VehicleMoveEvent): Move event
	 * @param state(HashMap): Map of boats and prev and next checkpoints
	 * @param checkPoints(List): List of checkpoints
	 */
	public void onMove(VehicleMoveEvent event, HashMap<Boat, Pair<CheckPoint, CheckPoint>> state, List<CheckPoint> checkPoints) {
		Boat boat = (Boat) event.getVehicle();
		CheckPoint next = state.get(boat).getSecond();
		//Determine if checkpoint is hit
		if (next.distanceSquared(boat.getLocation()) <= 0.81) {
			//Increase lap count if first checkpoint was reached
			if (next.getNumber() == 1) {
				Player driver = (Player) boat.getPassengers().get(0);
				IceScooterTimeTrial race = (IceScooterTimeTrial) Tracker.getGame(driver);
				race.newLap(driver);
			}
			//Get next checkpoints
			state.put(boat, new Pair<>(next, checkPoints.get(next.getNumber() % checkPoints.size())));
		}
	}
}
