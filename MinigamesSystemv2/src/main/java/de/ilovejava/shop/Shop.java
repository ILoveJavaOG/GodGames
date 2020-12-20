package de.ilovejava.shop;

import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class Shop {
	public Shop() {
		if(Utils.getShop() != null) {
			Villager v = (Villager) Utils.getShop().getWorld().spawnEntity(Utils.getShop(), EntityType.VILLAGER);
			v.setCustomName("§6§o§lSchatz Shop");
			v.setCustomNameVisible(true);
			v.setAI(false);
			v.setHealth(20.0);
			v.setInvulnerable(true);
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Shop Spawn gesetzt werden!");}
	}
}
