package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class ProjectileListener implements Listener {

	/**
	 * Event on projectile hit
	 *
	 * @param event(ProjectileHitEvent): ProjectileHitEvent event
	 */
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Player shooter = (Player) event.getEntity().getShooter();
		assert shooter != null;
		Tracker.redirectEvent(shooter, event);
	}

	/**
	 * Event on projectile launch
	 *
	 * @param event(ProjectileLaunchEvent): Projectile launch event
	 */
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent event) {
		Player shooter = (Player) event.getEntity().getShooter();
		assert shooter != null;
		Tracker.redirectEvent(shooter, event);
	}

	/**
	 * Event on projectile launch
	 *
	 * @param event(FireworkExplodeEvent): Firework explode event
	 */
	@EventHandler
	public void onLaunch(FireworkExplodeEvent event) {
		List<MetadataValue> data = event.getEntity().getMetadata("UUID");
		if (data.size() == 1) {
			UUID uuid = UUID.fromString(data.get(0).asString());
			Player thrower = Bukkit.getPlayer(uuid);
			Tracker.redirectEvent(thrower, event);
		}
	}
}
