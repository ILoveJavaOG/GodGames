package de.ilovejava.minigames.Games.FishFight.Items;

import de.ilovejava.minigames.Communication.NumeralTranslator;
import de.ilovejava.minigames.Games.FishFight.Enchantments.Enchantable;
import de.ilovejava.minigames.Games.FishFight.Enchantments.FishEnchantment;
import de.ilovejava.minigames.Items.GameItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a fish which is used as a weapon
 */
public class FishWeapon extends GameItem implements Enchantable {

	/**
	 * Constructor for the weapon
	 *
	 * @param holder(Player): Player holding the weapon
	 * @param display(Material): Display item of the weapon
	 * @param name(String): Display name of the weapon
	 */
	public FishWeapon(Player holder, Material display, String name) {
		super(holder, display, name, 1);
		//5 possible hits
		Damageable meta = (Damageable) this.display.getItemMeta();
		if (meta != null) {
			meta.setDamage(5);
		}
		this.display.setItemMeta((ItemMeta) meta);
	}

	@Override
	protected void useItem() {

	}

	/**
	 * Method to retrieve the item for the weapon
	 *
	 * @return Fish which is being used
	 */
	public ItemStack getFish() {
		return display;
	}

	/**
	 * Method to add the enchantment
	 *
	 * @param enchantment(Enchantment): Enchantment which is to be added
	 * @param level(int): Level of the enchantment
	 */
	@Override
	public void addEnchantment(Enchantment enchantment, int level) {
		//Check if the enchantment is a custom one
		if (enchantment instanceof FishEnchantment) {
			//Add enchantment to the lore for display
			ItemMeta meta = this.display.getItemMeta();
			if (meta != null) {
				List<String> lore = meta.getLore();
				if (lore == null) lore = new ArrayList<>();
				lore.add(0, ChatColor.translateAlternateColorCodes('&', "&r&7" + StringUtils.capitalize(enchantment.getKey().getKey()) + " " + NumeralTranslator.toNumeral(level)));
				meta.setLore(lore);
			}
			this.display.setItemMeta(meta);
		}
		//Add enchantment to item itself
		this.display.addUnsafeEnchantment(enchantment, level);
	}
}
