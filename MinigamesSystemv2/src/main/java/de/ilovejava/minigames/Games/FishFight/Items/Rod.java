package de.ilovejava.minigames.Games.FishFight.Items;

import de.ilovejava.minigames.Communication.NumeralTranslator;
import de.ilovejava.minigames.Games.FishFight.Enchantments.Enchantable;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the fishing rod which is used in the game
 */
public class Rod implements Enchantable {

	//Rod itself
	@Getter
	private final ItemStack rod = new ItemStack(Material.FISHING_ROD);


	/**
	 * Constructor for the rod
	 */
	public Rod() {
		//Hide enchantments and unbreakable
		ItemMeta meta = rod.getItemMeta();
		if (meta != null) {
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		rod.setItemMeta(meta);
	}
	
	/**
	 * Method to add the enchantment
	 *
	 * @param enchantment(Enchantment): Enchantment which is to be added
	 * @param level(int): Level of the enchantment
	 */
	@Override
	public void addEnchantment(Enchantment enchantment, int level) {
		ItemMeta meta = rod.getItemMeta();
		//Add enchantments to lore
		if (meta != null) {
			List<String> lore = meta.getLore();
			if (lore == null) lore = new ArrayList<>();
			lore.add(0, ChatColor.translateAlternateColorCodes('&', "&r&7" + StringUtils.capitalize(enchantment.getKey().getKey()) + " " + NumeralTranslator.toNumeral(level)));
			meta.setLore(lore);
		}
		rod.setItemMeta(meta);
	}
}
