package de.ilovejava.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

import de.ilovejava.utils.Utils;

public class Command_Nick extends AbstartcCommands{

	public Command_Nick(String commandName, Plugin pl) {
		super(commandName, pl);
		
	}

	@Override
	public boolean command(CommandSender paramCommandSender, String[] paramArrayOfString) {
		Player p = (Player) paramCommandSender;
		if(p.hasPermission("MSG.Team")) {
			p.openInventory(getInv(p));
		}else {p.sendMessage(Utils.getPrefix() + "ßcAchtung, du darfst das nicht!");}
		return true;
	}

	public static Inventory getInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "ßeTeam Arena");
		
		ArrayList<String> lore = new ArrayList<String>();
		
		GameProfile gp = ((CraftPlayer)p).getHandle().getProfile();
		
		if(Utils.getProfielUUIDS().containsKey(gp)) {lore.add("ßeAktiv: ßaJa");}else {lore.add("ßeAktiv: ßcNein");}
		
		ItemStack i = new ItemStack(Material.DIAMOND);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("ßeUndercover Modus");
		im.setLore(lore);
		i.setItemMeta(im);
		inv.setItem(19, i);
		
		
		i = new ItemStack(Material.EMERALD);
		im = i.getItemMeta();
		im.setDisplayName("ßeChat Ingnore");
		i.setItemMeta(im);
		inv.setItem(20, i);
		
		i = new ItemStack(Material.GOLD_INGOT);
		im = i.getItemMeta();
		im.setDisplayName("ßeSpec Modus");
		i.setItemMeta(im);
		inv.setItem(21, i);
		
		i = new ItemStack(Material.ANVIL);
		im = i.getItemMeta();
		im.setDisplayName("ßeTeam Chat");
		i.setItemMeta(im);
		inv.setItem(22, i);
		
		i = new ItemStack(Material.ENDER_CHEST);
		im = i.getItemMeta();
		im.setDisplayName("ßeTeam Liste");
		i.setItemMeta(im);
		inv.setItem(23, i);
		
		i = new ItemStack(Material.DIAMOND_PICKAXE);
		im = i.getItemMeta();
		im.setDisplayName("ßeSpieler Info abfragen!");
		i.setItemMeta(im);
		inv.setItem(24, i);
		
		i = new ItemStack(Material.BARRIER);
		im = i.getItemMeta();
		im.setDisplayName("ßeSchlieﬂen");
		i.setItemMeta(im);
		inv.setItem(25, i);
		
		return inv;
	}
}
