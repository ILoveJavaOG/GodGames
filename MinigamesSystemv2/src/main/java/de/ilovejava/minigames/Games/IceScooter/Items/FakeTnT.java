package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class FakeTnT extends GameItem {

	/**
	 * Constructor for the item
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public FakeTnT(Player holder) {
		super(holder, Material.TNT, IceScooterItems.ICESCOOTER_FAKETNT.name());
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	public void useItem() {
		Location loc = holder.getLocation();
		if (loc.getWorld() != null) {
			//Spawn a tnt which glows and explodes after 2 seconds
			TNTPrimed primed = (TNTPrimed) loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
			primed.setFuseTicks(40);
			primed.setGlowing(true);
			primed.setYield(5);
		}
	}
}
