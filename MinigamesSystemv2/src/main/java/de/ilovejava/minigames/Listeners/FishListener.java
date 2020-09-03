package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Listener to handle fish events
 */
public class FishListener implements Listener {

	/**
	 * Event on player fishing
	 *
	 * @param event(EntityDamageEvent): Fish event
	 */
	@EventHandler
	public void onPlayerFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		if (Tracker.isInGame(player)) {
			Tracker.getGame(player).callEvent(event);
		}
	}
}
