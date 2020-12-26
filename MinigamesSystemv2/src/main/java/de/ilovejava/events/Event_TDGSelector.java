package de.ilovejava.events;

import de.ilovejava.tdg.TDG;
import de.ilovejava.tdg.Targeter;
import de.ilovejava.tntrun.TNTRUN_SelectorInventory;
import de.ilovejava.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Event_TDGSelector implements Listener {
	@EventHandler
	public void onInterAcct(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			for(Entity en : Utils.getInstance().entities) {
				if(Targeter.getTargetEntity(e.getPlayer()) == en) {
					String name = en.getName();
					TDG tdg = Utils.getInstance().pmenu.get(e.getPlayer());
					switch(name) {
						case "§b1VS1":

							break;
						case "§eBedWars":
							
							break;
						case "§6Void-Run":
							new TNTRUN_SelectorInventory(e.getPlayer());
							break;
						case "§aIce Scooter":
							
							break;
						case "§5Fischeschlacht":
							
							break;
						case "§9Schleimervalley":
							
							break;
						case "§3Schatzkiste":
							
							break;
						case "§cSchlie§en":
							tdg.closeMenu(e.getPlayer());
							break;
						default:
							
							break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
		Entity en = e.getRightClicked();
		if (Utils.getInstance().entities.contains(en)) {
			e.setCancelled(true);
		}
	}

}
