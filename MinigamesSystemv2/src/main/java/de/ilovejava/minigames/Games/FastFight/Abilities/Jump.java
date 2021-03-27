package de.ilovejava.minigames.Games.FastFight.Abilities;

import de.ilovejava.minigames.Abilities.Ability;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;

public class Jump extends Ability {

	//Constant upwards velocity
	private final Vector jump;

	//Arrays containing the cos and sin values for a ring
	private static final double[] cosineRingValues = {1.0, 0.86602540378, 0.5, 0, -0.5, -0.86602540378};
	private static final double[] sineRingValues   = {0.0, 0.5, 0.86602540378, 1, 0.86602540378, 0.5};

	private static final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(252, 213, 0), 1);

	public Jump(Player holder, double cooldown, double scale) {
		super(holder, Material.BLAZE_POWDER, "Jump", Collections.singletonList(""), 1, cooldown);
		jump = new Vector(0.0, scale, 0.0);
	}

	@Override
	public void use() {
		Vector up = holder.getVelocity().clone();
		if (up.getY() <= 0) up.setY(0.0);
		up.add(jump);
		holder.setVelocity(up);
		//Spawn ring
		for (int i = 0; i < 6; i++) {
			holder.spawnParticle(Particle.REDSTONE, holder.getLocation().clone().add(cosineRingValues[i], 0, sineRingValues[i]), 5, dustOptions);
			holder.spawnParticle(Particle.REDSTONE, holder.getLocation().clone().add(-cosineRingValues[i], 0, -sineRingValues[i]), 5, dustOptions);
		}
	}
}
