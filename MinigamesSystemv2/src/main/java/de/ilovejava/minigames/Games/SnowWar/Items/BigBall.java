package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

/**
 * Class to represent a big snowball
 */
public class BigBall extends GameItem {

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player holding the item
	 */
	public BigBall(Player holder) {
		super(holder, Material.SNOW_BLOCK, "BIGBALL", 1, 4);
	}

	/**
	 * Method to be implemented to use the item
	 */
	@Override
	protected void useItem() {
		Location loc = holder.getLocation();
		if (loc.getWorld() != null) {
			//Create snowball
			FallingBlock snowball = loc.getWorld().spawnFallingBlock(loc.add(0, 1.5, 0), Bukkit.createBlockData(Material.SNOW_BLOCK));
			snowball.setHurtEntities(false);
			snowball.setDropItem(false);
			snowball.setVelocity(loc.getDirection().normalize().multiply(1.5));
			//Bind the snowball so event can be traced
			Tracker.bindEntity(snowball, holder);
		}
	}
}
