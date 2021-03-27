package de.ilovejava.minigames.Games.FastFight.Abilities;

import de.ilovejava.minigames.Abilities.Ability;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Games.FastFight.FastFight;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;

public class Push extends Ability {

	private final double scale;

	public Push(Player holder, double cooldown, double scale) {
		super(holder, Material.ENDER_EYE, "Push", Collections.singletonList(""), 2, cooldown);
		this.scale = scale;
	}

	private static final double phi = Math.PI * (3. - Math.sqrt(5.));

	@Override
	public void use() {
		Location base = holder.getLocation().clone().subtract(0, 0.5, 0);
		Vector vec = base.toVector();
		FastFight fight = (FastFight) Tracker.getGame(holder);
		for (Player active : fight.getActivePlayers()) {
			if (active != holder) {
				double distance = active.getEyeLocation().distance(base);
				if (distance <= 5) {
					Vector dir = active.getEyeLocation().toVector().clone().subtract(vec).normalize();
					active.setVelocity(active.getVelocity().clone().add(dir.multiply(scale*(Math.exp(-distance)-Math.exp(distance-5) + 1))));
				}
			}
		}
		//Create Particle sphere
		Location center = holder.getEyeLocation().clone().subtract(0, 0.9, 0);
		int numParticles = 100;
		for (int i = 0; i < numParticles; i++) {
			double y = 1 - (i / (double) (numParticles - 1)) * 2;
			double radius = Math.sqrt(1 - y*y);
			double theta = phi * i;
			double x = Math.cos(theta) * radius;
			double z = Math.sin(theta) * radius;
			holder.getWorld().spawnParticle(Particle.CRIT_MAGIC, center, 0, x, y, z, 3.0);
		}
	}
}
