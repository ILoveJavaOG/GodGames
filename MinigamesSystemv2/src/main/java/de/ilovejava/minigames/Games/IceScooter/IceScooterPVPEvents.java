package de.ilovejava.minigames.Games.IceScooter;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Events;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class IceScooterPVPEvents implements IceScooterEvents, Events {


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
