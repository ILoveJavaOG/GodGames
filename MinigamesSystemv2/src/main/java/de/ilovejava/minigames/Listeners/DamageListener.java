package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Listener to handle damage events
 */
public class DamageListener implements Listener {

	/**
	 * Explosion event
	 *
	 * @param event(EntityExplodeEvent): Explosion event
	 */
	@EventHandler
	public void onTNTExplosion(EntityExplodeEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event on player damage
	 *
	 * @param event(EntityDamageEvent): Damage event
	 */
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//Get the game and call the event
			if (Tracker.isInGame(player)) {
				Tracker.getGame(player).callEvent(event);
			}
		}
	}
}
