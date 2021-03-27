package de.ilovejava.minigames.Games.IceScooter;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Events;
import de.ilovejava.minigames.MapTools.CheckPoint;
import de.ilovejava.minigames.MapTools.Position;
import de.ilovejava.minigames.MapTools.PositionMap;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Class to represent events during the race
 */
public class IceScooterRaceEvents implements IceScooterEvents, Events {

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
			//Make player invulnerable
			player.setInvulnerable(true);
			Vector stop = new Vector(0, 0, 0);
			//Create particles
			boat.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, boat.getLocation(), 10, 1.5, 1.5, 1.5);
			//Stop boat
			BukkitTask rotation = new BukkitRunnable() {
				@Override
				public void run() {
					boat.setVelocity(stop);
				}
			}.runTaskTimerAsynchronously(Lobby.getPlugin(), 1L, 1L);
			//Reset state and stop task
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				rotation.cancel();
				player.setInvulnerable(false);
			}, 21L);
		}
	}

	/**
	 * Method to handle moving of boats
	 *
	 * @param event(VehicleMoveEvent): Move event
	 * @param state(HashMap): Map of boats and prev and next checkpoints
	 * @param checkPoints(List): List of checkpoints
	 */
	public void onMove(VehicleMoveEvent event, PositionMap state, List<CheckPoint> checkPoints) {
		Boat boat = (Boat) event.getVehicle();
		CheckPoint next = state.getNextCheckpoint(boat);
		double distance = next.distanceSquared(boat.getLocation());
		Position current = state.getPosition(boat);
		current.setDistance(distance);
		//Determine if checkpoint is hit
		if (distance <= 0.81) {
			//Increase lap count if first checkpoint was reached
			if (next.getNumber() == 1) {
				Player driver = (Player) boat.getPassengers().get(0);
				IceScooterRace race = (IceScooterRace) Tracker.getGame(driver);
				current.increaseLab();
				race.newLap(driver);
			}
			CheckPoint newNextCheckpoint = checkPoints.get(next.getNumber() % checkPoints.size());
			state.update(boat, newNextCheckpoint);
		}
		state.update(boat, current);
	}
}
