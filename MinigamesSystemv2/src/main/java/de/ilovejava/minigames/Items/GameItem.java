package de.ilovejava.minigames.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Class to represent an item inside a game
 */
public abstract class GameItem implements Listener {

	//Player who uses the item
	protected Player holder;

	//Display of the item in the inventory
	protected ItemStack display;

	/**
	 * Constructor for the the game
	 *
	 * @param holder(Player): Holder of the item
	 * @param display(ItemStack): Display for the item
	 * @param name(String): Name of the item
	 * @param uses(int): Uses for the item
	 */
	public GameItem(Player holder, Material display, String name, int uses) {
		this.holder = holder;
		ItemStack item = new ItemStack(display);
		ItemMeta meta = item.getItemMeta();
		//Set name and uses
		if (meta != null) {
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}
		item.setAmount(uses);
		this.display = item;
	}

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Holder of the item
	 * @param display(ItemStack): Display for the item
	 * @param name(String): Name of the item
	 */
	public GameItem(Player holder, Material display, String name) {
		this(holder, display, name, 1);
	}

	/**
	 * Method to retrieve the display of the item
	 *
	 * @return Display for the item
	 */
	ItemStack getDisplay() {
		return display;
	}

	/**
	 * Method to call when the item is being activated
	 */
	public void activate() {
		//Use the item
		useItem();
		//If there are no items left remove the box and the item
		if (display.getAmount() == 1) {
			ItemBox.removeBox(holder);
			holder.getInventory().setItem(0, null);
		} else {
			//Decrease the count of the item
			ItemStack active = holder.getInventory().getItem(0);
			if (active != null) {
				active.setAmount(active.getAmount() - 1);
			}
		}
	}

	/**
	 * Method to be implemented to use the item
	 */
	protected abstract void useItem();
}
