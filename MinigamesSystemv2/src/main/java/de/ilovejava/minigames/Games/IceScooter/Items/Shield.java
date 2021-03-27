package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Shield extends GameItem {

	/**
	 * Constructor for the item
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public Shield(Player holder) {
		super(holder, Material.SHIELD, IceScooterItems.ICESCOOTER_SHIELD.name());
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	protected void useItem() {
		//Make player block all damage
		holder.setInvulnerable(true);
		//Spawn particles to display shield
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				holder.getWorld().spawnParticle(Particle.TOTEM, holder.getLocation(), 200, 1.5, 1.5, 1.5);
			}
		}.runTaskTimerAsynchronously(Lobby.getPlugin(), 0, 10L);
		//Cancel particles and reset state
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
			task.cancel();
			holder.setInvulnerable(false);
		}, 60L);
	}
}
