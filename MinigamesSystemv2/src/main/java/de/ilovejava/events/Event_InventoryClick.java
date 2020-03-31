package de.ilovejava.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ilovejava.lottochest.Lottochest;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;

public class Event_InventoryClick implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory().getName() == null) return;
		if(e.getCurrentItem().getType().equals(Material.AIR))return;
		if(!e.getCurrentItem().hasItemMeta()) return;
		
		Player p = (Player) e.getWhoClicked();
		String inv = e.getInventory().getName();
		String item = e.getCurrentItem().getItemMeta().getDisplayName();
		String uuid = uuidfetcher.getUUID(p).toString();
		switch(inv) {
			case "§e§o§lShop":
				e.setCancelled(true);
				switch(item) {
					case "§e§o§lSammlerköpfe Karte":
						if(Utils.getEco().getBalance(p) >= 60) {
							Utils.getEco().withdrawPlayer(p, 60);
							openSkullShop(e.getInventory(), p);
						}else {p.closeInventory(); p.sendMessage(Utils.getPrefix() + "§cDu hast nicht genügend Arden!");}
						break;
					case "§c???????":
						if(Utils.getSkullcards().containsKey(p)) {
							Utils.getSkullcards().get(p).showWin(e.getInventory(), e.getSlot(), uuid);
						}else {p.closeInventory();}
						break;
					case "§6Lotto Chest":
						if(!Utils.getWaitLottochest().contains(p)) {
							new Lottochest(p);
						}else {p.sendMessage(Utils.getPrefix() + "§cBitte warte noch etwas!");}
						break;
				}
				break;
			case "§6§o§lSammlerköpfe":
				e.setCancelled(true);
				break;
			case "§5LottoChest":
				e.setCancelled(true);
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e???????")) {
						if(Utils.getLottoCards().containsKey(p)) {
							Lottochest lt = Utils.getLottoCards().get(p);
							if(!lt.isChestEnd()) {
								ItemStack it = new ItemStack(Material.EMERALD);
								ItemMeta im = it.getItemMeta();
								im.setDisplayName("§e" + lt.getPrice());
								it.setItemMeta(im);
								e.getInventory().setItem(e.getSlot(), it);
								p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
								p.updateInventory();
							}else {}
						}else {p.closeInventory();}
					}
				break;
		}
	}
	
	private void openSkullShop(Inventory inv, Player p) {
		inv.clear();
		ItemStack i = new ItemStack(Material.GOLD_INGOT);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§c???????");
		i.setItemMeta(im);
		inv.setItem(12, i);
		inv.setItem(13, i);
		inv.setItem(14, i);
		p.updateInventory();
	}
}
