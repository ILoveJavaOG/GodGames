package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Class to represent a speed boost
 */
public class Boost extends GameItem {

	/**
	 * Constructor for the item
	 *
	 * @param holder(Player): Player which holds the item
	 * @param uses(int): Number of usages
	 */
	public Boost(Player holder, int uses) {
		super(holder, Material.BLAZE_POWDER, IceScooterItems.ICESCOOTER_BOOST.name(), uses);
	}

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public Boost(Player holder) {
		this(holder, 1);
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	protected void useItem() {
		Boat boat = (Boat) holder.getVehicle();
		if (boat != null) {
			Vector dir = holder.getVehicle().getLocation().getDirection();
			//Check if direction can be changed
			if (!dir.equals(new Vector(0, 0, 0))) {
				//Add a boost
				Vector old = dir.clone();
				dir = dir.normalize().multiply(2);
				boat.setVelocity(old.add(dir));
			}
		}
	}
}
