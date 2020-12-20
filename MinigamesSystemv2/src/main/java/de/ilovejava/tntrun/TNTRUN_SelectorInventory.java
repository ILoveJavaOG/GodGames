package de.ilovejava.tntrun;

import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TNTRUN_SelectorInventory {

	Player p;
	Inventory inv;
	public TNTRUN_SelectorInventory(Player p) {
		this.p = p;
		this.inv = Bukkit.createInventory(null, 54, "§eVoid-Run");
		this.buildInventory();
	}
	
	private void buildInventory() {
		if(Utils.getTNTReady().size() == 0) {
			ItemStack i = new ItemStack(Material.BARRIER);
			ItemMeta im = i.getItemMeta();
			im.setDisplayName("§cKein Spiel vorhanden!");
			i.setItemMeta(im);
			this.inv.setItem(22, i);
		} else {
			for(String key : Utils.getTNTReady().keySet()) {
				TNT tnt = Utils.getTNTReady().get(key);
				ItemStack i = new ItemStack(Material.SAND);
				ItemMeta im = i.getItemMeta();
				im.setDisplayName("§b"+tnt.mapName());
				ArrayList<String> Lore = new ArrayList<String>();
				Lore.add("§6Spieler: " + tnt.player().size());
				Lore.add("§6Min. Spieler: " + tnt.Min_Player());
				Lore.add("§6Max. Spieler: " + tnt.Max_Player());
				im.setLore(Lore);
				i.setItemMeta(im);
				inv.addItem(Utils.setCustomNBTTag(i, "arena", key));
			}
		}
		this.openInv();
	}
	
	private void openInv() {
		this.p.openInventory(this.inv);
	}
}
