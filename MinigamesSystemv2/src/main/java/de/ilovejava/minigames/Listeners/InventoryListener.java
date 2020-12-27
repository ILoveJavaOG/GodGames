package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener {

	@IsUsed
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Tracker.redirectEvent((Player) event.getWhoClicked(), event);
		}
	}

	@IsUsed
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@IsUsed
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Tracker.redirectEvent((Player) event.getWhoClicked(), event);
		}
	}
}
