package de.ilovejava.minigames.Games.FireBall;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;

import java.util.Random;

public class Ball {

	private static final String[] codes = {"§f", "§6", "§4"};

	private final ArmorStand holder;
	private final ArmorStand cooldown;
	private final LargeFireball fireBall;
	private final int level = new Random().nextInt(3) + 1;
	private long cd;

	public Ball(Location spawn, int time) {
		this.cd = time;
		this.fireBall = (LargeFireball) create(spawn, LargeFireball.class, codes[level - 1] + "Fireball Lvl[" + level + "]");
		this.cooldown = (ArmorStand) create(spawn.clone().add(0, -0.5, 0), ArmorStand.class, "§4" + cd);
		this.holder = (ArmorStand) create(spawn.clone().add(0, -2, 0), ArmorStand.class, "");

	}

	private Entity create(Location spawn, Class<? extends Entity> type, String name) {
		World world = spawn.getWorld();
		assert world != null;
		Entity e = world.spawn(spawn, type);
		e.setCustomNameVisible(true);
		e.setCustomName(name);
		e.setGravity(false);
		e.setInvulnerable(true);
		return e;
	}

	public void die() {
		holder.eject();
		fireBall.getWorld().createExplosion(fireBall.getLocation(), level, false);
		holder.remove();
		fireBall.remove();
		cooldown.remove();
	}

	public boolean next() {
		cooldown.setCustomName("§4" + cd);
		return --cd == 0;
	}

	@Override
	public int hashCode() {
		return fireBall.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Ball) {
			return fireBall.equals(((Ball) obj).fireBall);
		}
		return false;
	}
}
