package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class SpawnListener implements Listener {

	@IsUsed
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		System.out.println("SPAWNED");
		Tracker.redirectEvent(event.getEntity(), event);
	}

}
