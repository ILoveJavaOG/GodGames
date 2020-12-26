package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Games.SnowWar.SnowWar;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Class to represent a the rapid fire game item
 */
public class RapidFire extends GameItem {

	/**
	 * Constructor for the the game
	 *
	 * @param holder(Player): Player for which rapid fire will be enabled
	 */
	public RapidFire(Player holder) {
		super(holder, Material.MAGMA_CREAM, "RAPIDFIRE", 1, 4);
	}

	/**
	 * Method to be implemented to use the item
	 */
	@Override
	protected void useItem() {
		//Get the game and redirect state
		SnowWar war = (SnowWar) Tracker.getGame(holder);
		war.enableRapidFire(holder);
	}
}
