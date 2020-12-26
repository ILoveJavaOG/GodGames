package de.ilovejava.events;

import com.mojang.authlib.GameProfile;
import de.ilovejava.commands.Command_Nick;
import de.ilovejava.support.skin.NickSystem;
import de.ilovejava.utils.Utils;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Random;

public class Event_TeamClick implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() == null) return;
		if(e.getCurrentItem() == null) return;
		if(!e.getCurrentItem().hasItemMeta()) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if(e.getView().getTitle().equalsIgnoreCase("§eTeam Arena")) {
			e.setCancelled(true);
			switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
				case "§eUndercover Modus":
					GameProfile gp = ((CraftPlayer)p).getHandle().getProfile();
					if(Utils.getProfielUUIDS().containsKey(gp)) {
						NickSystem.unnickPlayer(p);
						p.sendMessage(Utils.getPrefix() + "§bDu hast nun wieder §edeinen§b Namen");
					}else {
						Random rnd = new Random();
						String name = Utils.getNames().get(rnd.nextInt(Utils.getNames().size()));
						NickSystem.nickPlayer(p, name);
						p.sendMessage(Utils.getPrefix() + "§bDu hast nun den Namen: §e" + name);
					}
					updateInv(e.getInventory(), p);
					break;
				case "":
					
					break;
				
			}
		}
	}
	
	
	private void updateInv(Inventory i, Player p) {
		i.clear();
		i.setContents(Command_Nick.getInv(p).getContents());
		p.updateInventory();
	}
}
