package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
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
		super(holder, Material.DARK_OAK_LEAVES, "SHIELD", 1, 3);
		this.display = new ItemStackBuilder(Material.DARK_OAK_LEAVES)
				.getMetaDataBuilder()
				.setDisplayName("&r&2Adventskranz")
				.setLore("&r&fEin alter Adventskranz",
						"&r&f",
						"&r&fNutze die Äste um dir",
						"&r&fein Schild zu bauen")
				.build()
				.build();
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
		holder.getInventory().addItem(shield);
	}


}
