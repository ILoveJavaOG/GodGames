package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
		//Cancel clicks if player is in game and call event
		if (Tracker.isInGame(event.getPlayer())) {
			event.setCancelled(true);
			Tracker.getGame(event.getPlayer()).callEvent(event);
		}
	}

}
