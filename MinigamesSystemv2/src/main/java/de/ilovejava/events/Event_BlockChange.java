package de.ilovejava.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class Event_BlockChange implements Listener {
	@EventHandler
	public void onChange(EntityChangeBlockEvent e) {
		if (e.getEntity().getType().equals(EntityType.FALLING_BLOCK)) {
			e.getEntity().remove();
			e.getBlock().setType(Material.AIR);
			e.setCancelled(true);
		}
	}
}
