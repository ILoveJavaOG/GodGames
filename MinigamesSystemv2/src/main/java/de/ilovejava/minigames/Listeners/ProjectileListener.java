package de.ilovejava.minigames.Listeners;

import de.ilovejava.minigames.Communication.Tracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileListener implements Listener {

	/**
	 * Event on projectile hit
	 *
	 * @param event(ProjectileHitEvent): ProjectileHitEvent event
	 */
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Player shooter = (Player) event.getEntity().getShooter();
		Tracker.redirectEvent(shooter, event);
	}

	/**
	 * Event on projectile launch
	 *
	 * @param event(ProjectileLaunchEvent): Projectile launch event
	 */
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent event) {
		ProjectileSource source = event.getEntity().getShooter();
		if (source instanceof Player) Tracker.redirectEvent((Player) source, event);
	}

	/**
	 * Event on projectile launch
	 *
	 * @param event(FireworkExplodeEvent): Firework explode event
	 */
	@EventHandler
	public void onLaunch(FireworkExplodeEvent event) {
		Tracker.redirectEvent(event.getEntity().getUniqueId(), event);
	}
}
