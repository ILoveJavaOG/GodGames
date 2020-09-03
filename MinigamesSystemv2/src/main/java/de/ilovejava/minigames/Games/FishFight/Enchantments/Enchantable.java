package de.ilovejava.minigames.Games.FishFight.Enchantments;

import org.bukkit.enchantments.Enchantment;

/**
 * Interface to show something is enchantable
 */
public interface Enchantable {

	/**
	 * Method to add the enchantment
	 *
	 * @param enchantment(Enchantment): Enchantment which is to be added
	 * @param level(int): Level of the enchantment
	 */
	void addEnchantment(Enchantment enchantment, int level);

}
