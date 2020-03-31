package de.ilovejava.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;

public class Event_Drop implements Listener {
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(Utils.getTypes().get(e.getPlayer()).equals(ChatType.LOBBY)) {
			if(!e.getPlayer().hasPermission("GG.Drop") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
			}
		}
	}
}
