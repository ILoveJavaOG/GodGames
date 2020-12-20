package de.ilovejava.events;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Event_Build implements Listener {
	@EventHandler
	public void onBuild(BlockPlaceEvent e) {
		if (Utils.getTypes().get(e.getPlayer()).equals(ChatType.LOBBY)) {
			if(!e.getPlayer().hasPermission("GG.Build") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
			}
		} else {
			if(!e.getPlayer().hasPermission("GG.Build") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBuild(BlockBreakEvent e) {
		if (Utils.getTypes().get(e.getPlayer()).equals(ChatType.LOBBY)) {
			if(!e.getPlayer().hasPermission("GG.Build") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
			}
		} else {
			if (!e.getPlayer().hasPermission("GG.Build") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
			}
		}
	}
}
