package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

/**
 * Listener to handle damage events
 */
public class DamageListener implements Listener {


	public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
		Entity damaged = event.getEntity();
		if (damaged instanceof  Player) {
			Tracker.redirectEvent((Player) damaged, event);
		}
	}

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

	/**
	 * Event on player damage
	 *
	 * @param event(EntityDamageEvent): Damage event
	 */
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player receiver = (Player) event.getEntity();
			//Get the game and call the event
			if (Tracker.isInGame(damager) && Tracker.isInGame(receiver)) {
				if (Tracker.getGameId(damager) == Tracker.getGameId(receiver)) {
					Tracker.getGame(damager).callEvent(event);
				}
			}
		}
	}

	/**
	 * Event on player regen
	 *
	 * @param event(EntityDamageEvent): Damage event
	 */
	@EventHandler
	public void onPlayerRegen(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//Get the game and call the event
			if (Tracker.isInGame(player)) {
				Tracker.getGame(player).callEvent(event);
			}
		}
	}

	/**
	 * Event on player regen
	 *
	 * @param event(EntityDamageEvent): Damage event
	 */
	@EventHandler
	public void onFoodDepletion(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//Get the game and call the event
			Tracker.redirectEvent(player, event);
		}
	}
}
