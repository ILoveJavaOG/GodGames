package de.ilovejava.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import de.ilovejava.utils.Utils;

public class Event_InventoryClose implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getView().getTitle().equalsIgnoreCase("§e§o§lShop")) {
			if(Utils.getSkullcards().containsKey(e.getPlayer())) {
				if(!Utils.getSkullcards().get(e.getPlayer()).canPlay()) {
					Utils.getSkullcards().remove(e.getPlayer());
				}
			}
		}else if(e.getView().getTitle().equalsIgnoreCase("§5LottoChest")) {
			if(Utils.getLottoCards().containsKey(e.getPlayer())) {
				if(!Utils.getLottoCards().get(e.getPlayer()).isGive()) {
					Utils.getEco().depositPlayer(Bukkit.getOfflinePlayer(e.getPlayer().getName()), Utils.getLottoCards().get(e.getPlayer()).getSadPrice());
					e.getPlayer().sendMessage(Utils.getPrefix() + "§bDu hast §e" + Utils.getLottoCards().get(e.getPlayer()).getSadPrice() + "§b Arden bekommen!");
					Utils.getLottoCards().remove(e.getPlayer());
					Utils.getWaitLottochest().remove(e.getPlayer());
				}
			}
		}
	}
}
