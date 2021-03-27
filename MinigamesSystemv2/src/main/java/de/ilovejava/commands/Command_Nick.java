package de.ilovejava.commands;

import com.mojang.authlib.GameProfile;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Command_Nick extends AbstartcCommands{

	public Command_Nick(String commandName, Plugin pl) {
		super(commandName, pl);
		
	}

	@Override
	public boolean command(CommandSender paramCommandSender, String[] paramArrayOfString) {
		Player p = (Player) paramCommandSender;
		if (p.hasPermission("MSG.Team")) {
			p.openInventory(getInv(p));
		} else {
			p.sendMessage(Utils.getPrefix() + "§cAchtung, du darfst das nicht!");
		}
		return true;
	}

	public static Inventory getInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "§eTeam Arena");
		
		ArrayList<String> lore = new ArrayList<>();

		GameProfile gp = ((CraftPlayer)p).getHandle().getProfile();
		
		if (Utils.getProfielUUIDS().containsKey(gp)) {
			lore.add("§eAktiv: §aJa");
		} else {
			lore.add("§eAktiv: §cNein");
		}
		
		ItemStack i = new ItemStack(Material.DIAMOND);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§eUndercover Modus");
		im.setLore(lore);
		i.setItemMeta(im);
		inv.setItem(19, i);
		
		
		i = new ItemStack(Material.EMERALD);
		im = i.getItemMeta();
		im.setDisplayName("§eChat Ingnore");
		i.setItemMeta(im);
		inv.setItem(20, i);
		
		i = new ItemStack(Material.GOLD_INGOT);
		im = i.getItemMeta();
		im.setDisplayName("§eSpec Modus");
		i.setItemMeta(im);
		inv.setItem(21, i);
		
		i = new ItemStack(Material.ANVIL);
		im = i.getItemMeta();
		im.setDisplayName("§eTeam Chat");
		i.setItemMeta(im);
		inv.setItem(22, i);
		
		i = new ItemStack(Material.ENDER_CHEST);
		im = i.getItemMeta();
		im.setDisplayName("§eTeam Liste");
		i.setItemMeta(im);
		inv.setItem(23, i);
		
		i = new ItemStack(Material.DIAMOND_PICKAXE);
		im = i.getItemMeta();
		im.setDisplayName("§eSpieler Info abfragen!");
		i.setItemMeta(im);
		inv.setItem(24, i);
		
		i = new ItemStack(Material.BARRIER);
		im = i.getItemMeta();
		im.setDisplayName("§eSchlie§en");
		i.setItemMeta(im);
		inv.setItem(25, i);
		
		return inv;
	}
}
