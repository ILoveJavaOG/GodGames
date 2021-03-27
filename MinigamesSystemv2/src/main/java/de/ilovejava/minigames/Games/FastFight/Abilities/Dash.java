package de.ilovejava.minigames.Games.FastFight.Abilities;

import de.ilovejava.minigames.Abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Dash extends Ability {

	private static final int numParticles = 100;
	private static final Vector step = new Vector(0, 1.8/numParticles, 0);

	private final double scale;

	public Dash(Player holder, double cooldown, double scale) {
		super(holder, Material.NETHER_STAR, "Dash", Collections.singletonList(""), 0, cooldown);
		this.scale = scale;
	}


	@Override
	public void use() {
		@NotNull Vector looking = holder.getEyeLocation().getDirection().clone().normalize();
		Location base = holder.getLocation();
		holder.setVelocity(looking.clone().normalize().multiply(scale));
		for (int i = 0; i < numParticles; i++) {
			holder.spawnParticle(Particle.TOTEM, base.clone().add(step), 1);
		}
	}
}
