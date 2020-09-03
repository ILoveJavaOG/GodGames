package de.ilovejava.minigames.Games.FishFight.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Enum to capture all possible items
 */
public enum FishFightItems {

	FISHFIGHT_SMELLINGFLOUNDER,
	FISHFIGHT_SWORDFISH,
	FISHFIGHT_SHELLFISH,
	FISHFIGHT_SPOILEDFISH,
	FISHFIGHT_DEADFISH,
	FISHFIGHT_WHALEFISH;

	/**
	 * Method to get item from enum value
	 *
	 * @param player(Player): Player who holds the item
	 *
	 * @return Weapon associated with enum value
	 */
	public FishWeapon createItem(Player player) {
		switch (this) {
			case FISHFIGHT_SMELLINGFLOUNDER:
				return new FishWeapon(player, Material.PUFFERFISH, "SmellingFlounder");
			case FISHFIGHT_SHELLFISH:
				return new FishWeapon(player, Material.COD, "ShellFish");
			case FISHFIGHT_SWORDFISH:
				return new FishWeapon(player, Material.COOKED_COD, "SwordFish");
			case FISHFIGHT_SPOILEDFISH:
				return new FishWeapon(player, Material.TROPICAL_FISH, "SpoiledFish");
			case FISHFIGHT_DEADFISH:
				return new FishWeapon(player, Material.COOKED_SALMON, "DeadFish");
			case FISHFIGHT_WHALEFISH:
				return new FishWeapon(player, Material.SALMON, "WhaleFish");
			default:
				return null;
		}
	}
}
