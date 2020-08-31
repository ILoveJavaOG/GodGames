package de.ilovejava.minigames.Items;

import de.ilovejava.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Class to represent a box with items which get chosen randomly
 */
public class ItemBox {

	//Task during the selection
	private int selection;

	//State of the item
	private boolean active = false;

	//Selected item
	private int selected = 0;

	//Iteration while selecting items
	private int iteration = 0;

	//List of possible items
	private final List<? extends GameItem> possible;

	//Generator for the random numbers
	private final DistributedRandomNumberGenerator gen = new DistributedRandomNumberGenerator();

	//Players holding boxes
	private static final HashMap<Player, ItemBox> boxes = new HashMap<>();

	/**
	 * Function to determine if a player has a box associated
	 *
	 * @param player(Player): Player to look for
	 *
	 * @return True if player has a box. False if not
	 */
	public static boolean hasBox(Player player) {
		return boxes.containsKey(player);
	}

	/**
	 * Function to associate a box to a player
	 *
	 * @param box(ItemBox): ItemBox to associate
	 * @param player(Player): Player to associate box to
	 */
	public static void addBox(ItemBox box, Player player) {
		boxes.put(player, box);
	}

	/**
	 * Function to remove a box for the current player
	 *
	 * @param player(Player): Player to remove box from
	 */
	public static void removeBox(Player player) {
		boxes.remove(player);
	}

	/**
	 * Method to get the box for the associated player
	 *
	 * @param player(Player): Player to retrieve box for
	 *
	 * @return ItemBox for the player
	 */
	public static ItemBox getBox(Player player) {
		return boxes.get(player);
	}

	/**
	 * Constructor for the box
	 *
	 * @param holder(Player): Holder of the box
	 * @param possible(List): List of possible items
	 * @param percentage(List): Probability for the items
	 */
	public ItemBox(Player holder, @NotNull List<? extends GameItem> possible, List<Double> percentage) {
		//Setup generator
		for (int i = 0; i < possible.size(); i++) {
			gen.addNumber(i, percentage.get(i));
		}
		this.possible = possible;
		animate(holder);
	}

	/**
	 * Method to animate the selection process
	 *
	 * @param holder(Player): Player holding the item
	 */
	private void animate(Player holder) {
		//Determine which items should be selected
		selected = gen.getDistributedRandomNumber();
		//Selection process
		selection = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			//Select items from list
			GameItem item = possible.get(iteration % possible.size());
			holder.getInventory().setItem(0, item.getDisplay());
			iteration++;
			//After 20 iterations set active item
			if (iteration > 20) {
				holder.getInventory().setItem(0, possible.get(selected).getDisplay());
				active = true;
				Bukkit.getScheduler().cancelTask(selection);
			}
		}, 0, 5L);
	}

	/**
	 * Method to check if the box is active
	 *
	 * @return True if the box is active. False if not
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Method to retrieve the active item
	 *
	 * @return Currently active game item
	 */
	public GameItem getActiveItem() {
		return possible.get(selected);
	}
}
