package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
	@IsUsed
	@EventHandler
	public void onLeave(@NotNull PlayerQuitEvent leave) {
		Player left = leave.getPlayer();
		removeData(left);
		resetPlayer(left);
	}

	/**
	 * Event on player kick
	 *
	 * @param kick(PlayerKickEvent): Kick event
	 */
	@IsUsed
	@EventHandler
	public void onKick(@NotNull PlayerKickEvent kick) {
		Player kicked = kick.getPlayer();
		removeData(kicked);
		resetPlayer(kicked);
	}

	/**
	 * Event on player join
	 *
	 * @param joinEvent(PlayerKickEvent): Player join event
	 */
	@IsUsed
	@EventHandler
	public void onKick(@NotNull PlayerJoinEvent joinEvent) {
		Player joined = joinEvent.getPlayer();
		removeData(joined);
		resetPlayer(joined);
	}


	private void resetPlayer(Player player) {
		player.setInvulnerable(false);
		player.setGlowing(false);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0.0f);
		player.setLevel(0);
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
