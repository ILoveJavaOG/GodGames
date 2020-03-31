package de.ilovejava.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.Enums.PVPContainer;
import de.ilovejava.utils.Utils;

public class Event_Food implements Listener {
	@EventHandler
	public void food(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(Utils.getTypes().get(p).equals(ChatType.LOBBY)) {
				e.setCancelled(true);
			}else if(Utils.getTypes().get(p).equals(ChatType.INGAME)) {
				PVPContainer pvpc = Utils.getPvpStats().get(p);
				switch(pvpc) {
					case NONFOODPVP:
						e.setCancelled(true);
					break;
					case NONPVP:
						e.setCancelled(true);
						break;
					case PVP:
						e.setCancelled(false);
						break;
					case PVPNONFOOD:
						e.setCancelled(true);
						break;
				}
			}
		}
	}
}
