package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.entity.Player;

/**
 * Enum to encapsulate items
 */
public enum SnowWarItems {
	BIGBALL,
	RAPIDFIRE,
	SHIELD;

	/**
	 * Method to get item from enum value
	 *
	 * @param player(Player): Player who holds the item
	 *
	 * @return Item associated with enum value
	 */
	public GameItem createItem(Player player) {
		switch (this) {
			case BIGBALL:
				return new BigBall(player);
			case RAPIDFIRE:
				return new RapidFire(player);
			case SHIELD:
				return new Shield(player);
		}
		return null;
	}
}
