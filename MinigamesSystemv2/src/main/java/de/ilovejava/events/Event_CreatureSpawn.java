package de.ilovejava.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class Event_CreatureSpawn implements Listener {
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if(e.getSpawnReason().equals(SpawnReason.NATURAL)) {
			e.setCancelled(true);
		}
	}
}
