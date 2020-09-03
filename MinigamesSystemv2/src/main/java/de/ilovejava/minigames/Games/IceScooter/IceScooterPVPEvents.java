package de.ilovejava.minigames.Games.IceScooter;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Events.BoatBlockCollisionEvent;
import de.ilovejava.minigames.Events.BoatEntityCollisionEvent;
import de.ilovejava.minigames.Games.IceScooter.Items.IceScooterItems;
import de.ilovejava.minigames.Items.GameItem;
import de.ilovejava.minigames.Items.ItemBox;
import de.ilovejava.minigames.MapTools.ProbabilityMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IceScooterPVPEvents {

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
				IceScooterPVP pvp = (IceScooterPVP) Tracker.getGame(player);
				ProbabilityMap probabilities = pvp.getGameMap().getProbabilities();
				probabilities.setPlaces(pvp.getNumPlayers());
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
	 * Method to handle damage
	 *
	 * @param event(EntityDamageEvent): Damage event
	 */
	public void onDamage(EntityDamageEvent event) {
		//Stop damage
		event.setCancelled(true);
		Player player = (Player) event.getEntity();
		Vehicle boat = (Vehicle) player.getVehicle();
		if (boat != null) {
			player.setLevel(player.getLevel() - 1);
			if (player.getLevel() == 0) {
				IceScooterPVP pvp = (IceScooterPVP) Tracker.getGame(player);
				pvp.playerDie(player);
			} else  {
				//Make player invulnerable
				player.setInvulnerable(true);
				Vector stop = new Vector(0, 0, 0);
				//Create particles
				boat.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, boat.getLocation(), 10, 1.5, 1.5, 1.5);
				//Stop boat
				int breakTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> boat.setVelocity(stop), 1L, 1L);
				//Reset state and stop task
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
					Bukkit.getScheduler().cancelTask(breakTask);
					player.setInvulnerable(false);
				}, 21L);
			}
		}
	}
}
