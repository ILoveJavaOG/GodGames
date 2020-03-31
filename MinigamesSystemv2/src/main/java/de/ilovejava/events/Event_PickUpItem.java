package de.ilovejava.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;

@SuppressWarnings("deprecation")
public class Event_PickUpItem implements Listener {
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(Utils.getTypes().get(e.getPlayer()).equals(ChatType.LOBBY)) {
			if(!e.getPlayer().hasPermission("GG.Puckup") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				e.getItem().remove();
				e.setCancelled(true);
			}
		}
	}
}
