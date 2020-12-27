package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockListener implements Listener {

	@IsUsed
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@IsUsed
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@IsUsed
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		Tracker.redirectEvent(event.getEntity(), event);
	}
}
