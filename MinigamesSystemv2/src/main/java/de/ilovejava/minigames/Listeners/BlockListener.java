package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Tracker.redirectEvent(event.getPlayer(), event);
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		List<MetadataValue> data = event.getEntity().getMetadata("UUID");
		if (data.size() == 1) {
			UUID uuid = UUID.fromString(data.get(0).asString());
			Player thrower = Bukkit.getPlayer(uuid);
			Tracker.redirectEvent(thrower, event);
		}
	}
}
