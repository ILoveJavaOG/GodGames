package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.designs;

public class Shield extends GameItem {

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player who should recieve the shield
	 */
	public Shield(Player holder) {
		super(holder, Material.STICK, "SHIELD", 1, 4);
	}

	/**
	 * Method to be implemented to use the item
	 */
	@Override
	protected void useItem() {
		ItemStack shield = new ItemStack(Material.SHIELD);
		ItemMeta meta = shield.getItemMeta();
		BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
		assert blockStateMeta != null;
		Random random = new Random();
		blockStateMeta.setBlockState(designs[random.nextInt(5)]);
		shield.setItemMeta(blockStateMeta);
		Damageable damageable = (Damageable) meta;
		damageable.setDamage(3);
		shield.setItemMeta((ItemMeta) damageable);
		holder.getInventory().addItem(shield);
	}


}
