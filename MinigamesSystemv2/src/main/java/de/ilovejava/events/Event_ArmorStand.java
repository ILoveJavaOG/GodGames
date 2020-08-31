package de.ilovejava.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Event_ArmorStand implements Listener {
	@EventHandler
	public void entityDes(EntityDamageByEntityEvent e) {
		Entity target = e.getEntity();
		Entity Damager = e.getDamager();
		if (Damager instanceof Player) {
			if (target.getType().equals(EntityType.ARMOR_STAND)) {
				Player p = (Player) Damager;
				if (!p.hasPermission("*")) {
					e.setCancelled(true);
				}
			}
		}
	}
}
