package de.ilovejava.events;

import de.ilovejava.skullCard.SkullCard;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Event_InterActWithEntity implements Listener {
	@EventHandler
	public void onInter(PlayerInteractEntityEvent e) {
		if(e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			e.setCancelled(true);
			openShop(e.getPlayer());
			SkullCard sc = new SkullCard(e.getPlayer());
			sc.loadWins();
		}else {
			e.setCancelled(true);
		}
	}
	
	private void openShop(Player p) {
		
		List<String> Lore = new ArrayList<String>();
		
		Inventory inv = Bukkit.createInventory(null, 27, "§e§o§lShop");
		ItemStack i = Utils.getTimeSkull();
		SkullMeta sm = (SkullMeta) i.getItemMeta();
		sm.setDisplayName("§e§o§lSammlerköpfe Karte");
		Lore.add("§bGewinn: 1 Arden bis 5000 Arden");
		Lore.add("§bPreis: 60 Arden");
		sm.setLore(Lore);
		i.setItemMeta(sm);
		inv.setItem(11, i);
		
		i = new ItemStack(Material.ENDER_CHEST);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§6Lotto Chest");
		i.setItemMeta(im);
		inv.setItem(13, i);
		
		Lore.clear();
		Lore.add("§Versuch dein Glück und verdoppel deine Arden!");
		Lore.add("§bPreis: 75 Arden");
		Lore.add("§bGewinn: 1-7000 Arden");
		i = new ItemStack(Material.EMERALD);
		im = i.getItemMeta();
		im.setDisplayName("§a§o§lDeine Arden verdoppeln");
		i.setItemMeta(im);
		inv.setItem(15, i);
		
		p.openInventory(inv);
	}
}
