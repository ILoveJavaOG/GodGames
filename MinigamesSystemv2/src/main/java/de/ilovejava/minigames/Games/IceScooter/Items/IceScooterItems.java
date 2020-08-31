package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.entity.Player;

/**
 * Enum to encapsulate items
 */
public enum IceScooterItems {
	ICESCOOTER_RANDOMROCKET,
	ICESCOOTER_RANDOMROCKET_3,
	ICESCOOTER_FAKETNT,
	ICESCOOTER_ANVIL,
	ICESCOOTER_SHIELD,
	ICESCOOTER_EXPLOSION,
	ICESCOOTER_BOOST,
	ICESCOOTER_BOOST_3;

	/**
	 * Method to get item from enum value
	 *
	 * @param player(Player): Player who holds the item
	 *
	 * @return Item associated with enum value
	 */
	public GameItem createItem(Player player) {
		switch (this) {
			case ICESCOOTER_RANDOMROCKET:
				return new SimpleRandomRocket(player);
			case ICESCOOTER_RANDOMROCKET_3:
				return new SimpleRandomRocket(player, 3);
			case ICESCOOTER_FAKETNT:
				return new FakeTnT(player);
			case ICESCOOTER_ANVIL:
				return new Anvil(player);
			case ICESCOOTER_SHIELD:
				return new Shield(player);
			case ICESCOOTER_EXPLOSION:
				return new Explosion(player);
			case ICESCOOTER_BOOST:
				return new Boost(player);
			case ICESCOOTER_BOOST_3:
				return new Boost(player, 3);
		}
		return null;
	}
}
