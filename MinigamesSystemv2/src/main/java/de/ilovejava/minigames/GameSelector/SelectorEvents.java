package de.ilovejava.minigames.GameSelector;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Listener to listen for clicks inside the Selector
 */
public class SelectorEvents implements Listener {

	/**
	 * Method to handle right clicks in the selector
	 *
	 * @param event(InventoryClickEvent): Click in the inventory
	 */
	@EventHandler
	public void onClick(@NotNull InventoryClickEvent event) {
		//Check if the holder is a selector
		InventoryHolder holder = event.getView().getTopInventory().getHolder();
		if (holder instanceof Selector) {
			//Stop the click and inform the selector about the click
			event.setCancelled(true);
			if (0 <= event.getSlot() && event.getSlot() <= 8) {
				((Selector) holder).join(event.getSlot(), (Player) event.getWhoClicked());
			}
		}
	}


}
