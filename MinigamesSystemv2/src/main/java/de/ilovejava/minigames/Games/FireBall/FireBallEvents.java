package de.ilovejava.minigames.Games.FireBall;

import de.ilovejava.minigames.GameLogic.Events;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class FireBallEvents implements Events {

	private final FireBall fireBall;

	public FireBallEvents(FireBall fireBall) {
		this.fireBall = fireBall;
	}

	public void onFireBallPunch(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		if (damaged instanceof LargeFireball && damager instanceof Player) {
			Player puncher = (Player) damager;
			Vector dir = puncher.getEyeLocation().getDirection().clone().normalize();
			damaged.setVelocity(dir);
			int team = fireBall.getTeam(puncher);
			if (team != -1) ((FireBallMap) fireBall.getGameMap()).getInStasis().get(team - 1).remove(damaged);

		}
	}
}
