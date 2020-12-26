package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;


public class Explosion extends GameItem {

	/**
	 * Constructor for the item
	 *
	 * @param holder(Player): Player which holds the item
	 * @param uses(int): Number of usages
	 */
	public Explosion(Player holder, int uses) {
		super(holder, Material.REDSTONE_BLOCK, IceScooterItems.ICESCOOTER_EXPLOSION.name(), uses);
	}

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public Explosion(Player holder) {
		this(holder, 1);
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	protected void useItem() {
		//Make player invulnerable to avoid damage to self
		holder.setInvulnerable(true);
		//Create explosion and particles

		holder.getWorld().createExplosion(holder.getLocation(), 5, false, false);
		holder.getWorld().spawnParticle(Particle.FALLING_LAVA, holder.getLocation(), 200, 3, 3, 3);
		//Reset state of player
		holder.setInvulnerable(false);
	}
}
