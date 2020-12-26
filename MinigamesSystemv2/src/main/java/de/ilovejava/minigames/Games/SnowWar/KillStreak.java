package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Enum to encapsulate the kill streaks
 */
public enum KillStreak {

	//Sonar item
	SONAR(new ItemStackBuilder(Material.ENDER_EYE)
			.getMetaDataBuilder()
			.setDisplayName("Sonar")
			.setLore("Deckt andere Spieler auf")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build().build()),
	//Supply drop item
	SUPPLYDROP(new ItemStackBuilder(Material.FIREWORK_ROCKET)
			.getMetaDataBuilder()
			.setDisplayName("Supply Drop")
			.setLore("Rechtsklick zum aktivieren", "Wirft Supply Drop auf geklickte Stelle")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build().build()),
	//Bomber item
	BOMBER(new ItemStackBuilder(Material.ENDER_PEARL)
			.getMetaDataBuilder()
			.setDisplayName("Bomber")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.setLore("Werfe um einen Bombardierung zu rufen",
					 "Zentrum der Bombadierung ist",
					 "Am Aufschlagsort")
			.build().build());

	//Display inside the inventory
	@Getter
	private final ItemStack display;

	/**
	 * Constructor for the enum
	 *
	 * @param build(ItemStack): Display of the item
	 */
	KillStreak(ItemStack build) {
		this.display = build;
	}
}
