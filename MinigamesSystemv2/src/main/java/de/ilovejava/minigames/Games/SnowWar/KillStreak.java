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
	SONAR(new ItemStackBuilder(Material.WRITTEN_BOOK)
			.getMetaDataBuilder()
			.setDisplayName("&r&fSanta's Liste")
			.setLore("&r&fEine Liste mit Namen",
					 "&r&f",
					 "&r&fZeigt dir wer alles",
					 "&r&funnartig war")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build().build()),
	//Supply drop item
	SUPPLYDROP(new ItemStackBuilder(Material.PAPER)
			.getMetaDataBuilder()
			.setDisplayName("&r&7Wunschzettel")
			.setLore("&r&fDein Wunschzettel",
					 "&r&f",
					 "&r&fSchicke deinen Wunschzettel",
					 "&r&fzum Weinachtsmann")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build().build()),
	//Bomber item
	BOMBER(new ItemStackBuilder(Material.COCOA_BEANS)
			.getMetaDataBuilder()
			.setDisplayName("&r&8Rentier Futter")
			.setLore("&r&fWerfe das Futter auf den Boden.",
					 "&r&fDas Futter lockt die Rentiere an.")
			.addEnchant(Enchantment.DURABILITY, 1, true)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
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
