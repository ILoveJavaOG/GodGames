package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener to handle interact events
 */
public class InteractListener implements Listener {

	/**
	 * Event on player interactions
	 *
	 * @param event(PlayerInteractEvent): Interact event
	 */
	@EventHandler
	public void onClick(@NotNull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Tracker.redirectEvent(player, event);
	}

	/**
	 * Event on item drop
	 *
	 * @param event(PlayerDropItemEvent): Drop event
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		Tracker.redirectEvent(player, event);
	}

	/**
	 * Event on food consumption
	 *
	 * @param event(PlayerItemConsumeEvent): Consume event
	 */
	@EventHandler
	public void onConsumption(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		Tracker.redirectEvent(player, event);
	}

	/**
	 * Event on interaction
	 *
	 * @param event(PlayerInteractAtEntityEvent): Player interact with entity event
	 */
	@EventHandler
	public void onEntityInteract(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Tracker.redirectEvent(player, event);
	}
}
