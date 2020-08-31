package de.ilovejava.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.Enums.PVPContainer;
import de.ilovejava.utils.Utils;

public class Event_Damge implements Listener {
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(Utils.getTypes().get(p).equals(ChatType.LOBBY)) {
				e.setCancelled(true);
			}else if(Utils.getTypes().get(p).equals(ChatType.INGAME)) {
				PVPContainer pvpc = Utils.getPvpStats().get(p);
				switch(pvpc) {
				case NONFOODPVP:
					e.setCancelled(false);
					break;
				case NONPVP:
					e.setCancelled(true);
					break;
				case PVP:
					e.setCancelled(false);
					break;
				case PVPNONFOOD:
					System.out.println(e.getCause().toString());
					e.setCancelled(e.getCause().equals(DamageCause.FALL));
					break;
				}
			}
		}
	}
}
