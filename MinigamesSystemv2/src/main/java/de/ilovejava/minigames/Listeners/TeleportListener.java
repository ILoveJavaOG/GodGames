package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

	@IsUsed
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@IsUsed
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event) {
		Tracker.redirectEvent(event.getEntity(), event);
	}
}
