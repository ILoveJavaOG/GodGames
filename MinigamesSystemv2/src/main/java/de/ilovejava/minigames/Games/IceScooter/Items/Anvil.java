package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

/**
 * Class to represent an item which drops an anvil
 */
public class Anvil extends GameItem {

	/**
	 * Constructor to create the item
	 *
	 * @param holder(Player): Player which holds the item
	 * @param uses(int): Number of usages
	 */
	public Anvil(Player holder, int uses) {
		super(holder, Material.ANVIL, IceScooterItems.ICESCOOTER_ANVIL.name(), uses);
	}

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public Anvil(Player holder) {
		this(holder, 1);
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	protected void useItem() {
		Location loc = holder.getLocation();
		if (loc.getWorld() != null) {
			//Make anvil hover for a short time
			FallingBlock anvil = loc.getWorld().spawnFallingBlock(loc.add(0, 3, 0), Bukkit.createBlockData(Material.ANVIL));
			anvil.setGravity(false);
			//After 1 seconds drop anvil down
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> anvil.setGravity(true), 20L);
			//Remove anvil after 3 seconds
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				if (anvil.isOnGround()) {
					anvil.getLocation().getBlock().setType(Material.AIR);
				} else  {
					anvil.remove();
				}
			}, 80L);
		}
	}
}
