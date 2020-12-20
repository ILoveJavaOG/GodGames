package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player teleporter = event.getPlayer();
		Tracker.redirectEvent(teleporter, event);
	}
}
