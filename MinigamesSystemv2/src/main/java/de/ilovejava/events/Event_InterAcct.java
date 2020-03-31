package de.ilovejava.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.tdg.TDG;
import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;

public class Event_InterAcct implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInter(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand().equals(null)) {return;}
		if(!e.getPlayer().getItemInHand().hasItemMeta()) {return;};
		if(Utils.getTypes().get(e.getPlayer()).equals(ChatType.LOBBY)) {
			if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) ||e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
				if(!e.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
					if(!e.getPlayer().getItemInHand().hasItemMeta()) {if(!e.getPlayer().hasPermission("GG.InterAcct")) {e.setCancelled(true);}}
					String name = e.getPlayer().getItemInHand().getItemMeta().getDisplayName();
					if(name.equals("§e§o§lSpiel wählen§8(§cRechtklick§8)")) {
						if(Utils.getInstance().pmenu.get(e.getPlayer()) == null || !Utils.getInstance().pmenu.containsKey(e.getPlayer())) {
							openGUI(e.getPlayer());
						}
					}else if(name.equals("§6§o§lSammlerköpfe")) {
						openLegSkulls(e.getPlayer());;
					}
				}else {
					if(!e.getPlayer().hasPermission("GG.InterAcct")) {
						e.setCancelled(true);
					}
				}
			}else {
				if(!e.getPlayer().hasPermission("GG.InterAcct")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	private void openLegSkulls(Player p) {
		String uuid = uuidfetcher.getUUID(p).toString();
		Inventory inv = Bukkit.createInventory(null, 27, "§6§o§lSammlerköpfe");
		User user = Utils.getUser().get(uuid);
		HashMap<String, Boolean> skulls= user.skulls().getSkulls();

		ItemStack i = new ItemStack(Material.BARRIER);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§cVerschlossen!");
		List<String> lore = new ArrayList<String>();
		int timer = 1;
		
		p.openInventory(inv);
		
		for(String key : skulls.keySet()) {
			if(skulls.get(key)) {
				inv.setItem(getSlot(key), Utils.getSkullbase().getSkull(key));
			}else {
				lore.clear();
				lore.add(""+timer);
				im.setLore(lore);
				i.setItemMeta(im);
				inv.setItem(getSlot(key), i);
			}
			timer++;
			p.updateInventory();
		}
	}
	
	private Integer getSlot(String name) {
		int i = 0;
		switch(name) {
		case "ILoveJava":
			i = 0;
			break;
		case "Hyzenthay_Rah":
			i = 1;
			break;
		case "KingMontegro":
			i = 2;
			break;
		case "Noronei":
			i = 3;
			break;
		case "_Minemacs_":
			i = 4;
			break;
		case "martoffel_2":
			i = 5;
			break;
		case "ZiffernZocker":
			i = 6;
			break;
		case "Icetea3125":
			i = 7;
			break;
		case "Serratt":
			i = 8;
			break;
		case "Stacy148":
			i = 9;
			break;
		case "Ch3ckerJan":
			i = 10;
			break;
		case "DieMingos":
			i = 11;
			break;
		}
		return i;
	}
	
	private void openGUI(Player p) {
		TDG gui = new TDG();
		boolean cm = false;
		Location loc = null;
		if (cm == false) {
			loc = Utils.getBFLoc(p.getLocation(), 3.5);
			Utils.getInstance().lastLoc.put(p, loc);
		}
		if (cm == true) {
			loc = Utils.getInstance().lastLoc.get(p);
		}
		
		gui.addIcon(p, Utils.setPosition(loc, 1, 1, 4, 2, 2, 4), "§b1VS1", new ItemStack(Material.DIAMOND_SWORD), true, true, 2);
		gui.addIcon(p, Utils.setPosition(loc, 2, 1, 4, 2, 2, 4), "§eBedWars", new ItemStack(Material.GOLD_SWORD), true, true, 2);
		gui.addIcon(p, Utils.setPosition(loc, 3, 1, 4, 2, 2, 4), "§6Void-Run", new ItemStack(Material.SAND), 2);
		gui.addIcon(p, Utils.setPosition(loc, 4, 1, 4, 2, 2, 4), "§aIce Scooter", new ItemStack(Material.ICE), 2);
		gui.addIcon(p, Utils.setPosition(loc, 5, 1, 4, 2, 2, 4), "§5Fischeschlacht", new ItemStack(Material.RAW_FISH), true, 2);
		gui.addIcon(p, Utils.setPosition(loc, 1, 2, 4, 2, 2, 4), "§9Schleimervalley", new ItemStack(Material.TNT), 3);
		gui.addIcon(p, Utils.setPosition(loc, 2, 2, 4, 2, 2, 4), "§3Schatzkiste", new ItemStack(Material.ENDER_CHEST), 3);
		gui.addIcon(p, Utils.setPosition(loc, 5, 2, 4, 2, 2, 4), "§cSchließen", "http://textures.minecraft.net/texture/5a6787ba32564e7c2f3a0ce64498ecbb23b89845e5a66b5cec7736f729ed37", 3);
		Utils.getInstance().pmenu.put(p, gui);
	}
}
