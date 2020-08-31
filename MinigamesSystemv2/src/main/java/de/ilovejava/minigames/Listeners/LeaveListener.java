package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for Leave related events
 */
public class LeaveListener implements Listener {

	/**
	 * Event on player leave
	 *
	 * @param leave(PlayerQuitEvent): Leave event
	 */
	@EventHandler
	public void onLeave(@NotNull PlayerQuitEvent leave) {
		removeData(leave.getPlayer());
		leave.getPlayer().setInvulnerable(false);
	}

	/**
	 * Event on player kick
	 *
	 * @param kick(PlayerKickEvent): Kick event
	 */
	@EventHandler
	public void onKick(@NotNull PlayerKickEvent kick) {
		removeData(kick.getPlayer());
		kick.getPlayer().setInvulnerable(false);
	}

	/**
	 * Method to remove the data for the player
	 *
	 * @param player(Player): Player to remove data from
	 */
	private void removeData(Player player) {
		if (Tracker.isInGame(player)) {
			Tracker.unregisterPlayer(player);
		}
	}
}
