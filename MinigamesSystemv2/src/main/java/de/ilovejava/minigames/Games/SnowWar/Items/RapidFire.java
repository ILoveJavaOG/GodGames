package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Games.SnowWar.SnowWar;
import de.ilovejava.minigames.Games.SnowWar.SnowWarEvents;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Class to represent a the rapid fire game item
 */
public class RapidFire extends GameItem {

	/**
	 * Constructor for the the game
	 *
	 * @param holder(Player): Player for which rapid fire will be enabled
	 */
	public RapidFire(Player holder) {
		super(holder, Material.COOKED_CHICKEN, "RAPIDFIRE", 1, 3);
		this.display = new ItemStackBuilder(Material.COOKED_CHICKEN)
				.getMetaDataBuilder()
				.setDisplayName("&r&4Warme Weinachtsgans")
				.setLore("&r&fEine warme Gans",
						"&r&f(Not sponsored by Endstille)",
						"&r&f",
						"&r&fIss die ganz um dich aufzuwärmen.",
						"&r&fWenn dir warm ist verlierst du",
						"&r&fkeine Schneebälle beim werfen")
				.build()
				.build();
	}

	/**
	 * Method to be implemented to use the item
	 */
	@Override
	protected void useItem() {
		//Get the game and redirect state
		SnowWar war = (SnowWar) Tracker.getGame(holder);
		((SnowWarEvents) war.getEventHandler()).enableRapidFire(holder);
		holder.playSound(holder.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1f, 0.1f);
	}
}
