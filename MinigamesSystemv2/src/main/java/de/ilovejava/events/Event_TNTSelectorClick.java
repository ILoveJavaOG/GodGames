package de.ilovejava.events;

import de.ilovejava.tntrun.TNTRunJoinEvent;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class Event_TNTSelectorClick implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory() == null) return;
		if(e.getCurrentItem() == null) return;
		if(!e.getCurrentItem().hasItemMeta()) return;
		if(e.getSlotType() == null) return;
		if(e.getView().getTitle().contentEquals("§eVoid-Run")) {
			e.setCancelled(true);
			if(e.getCurrentItem().getType().equals(Material.BARRIER)) return;
			String aname = Utils.getNBTTag(e.getCurrentItem());
			System.out.println("Name: " + aname);
			Bukkit.getPluginManager().callEvent(new TNTRunJoinEvent(p, Utils.getTNTReady().get(aname)));
			p.closeInventory();
		}
	}
}
