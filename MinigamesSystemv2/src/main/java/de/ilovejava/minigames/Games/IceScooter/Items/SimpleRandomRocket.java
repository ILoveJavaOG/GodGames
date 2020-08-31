package de.ilovejava.minigames.Games.IceScooter.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;

public class SimpleRandomRocket extends GameItem {

	/**
	 * Constructor to create the item
	 *
	 * @param holder(Player): Player which holds the item
	 * @param uses(int): Number of usages
	 */
	public SimpleRandomRocket(Player holder, int uses) {
		super(holder, Material.FIREWORK_ROCKET, "ICESCOOTER_ROCKET", uses);
	}

	/**
	 * Overloaded constructor call
	 *
	 * @param holder(Player): Player which holds the item
	 */
	public SimpleRandomRocket(Player holder) {
		this(holder, 1);
	}

	/**
	 * Method to use/activate the item
	 */
	@Override
	public void useItem() {
		//Create randomizer
		Random randomGen = new Random();
		//Get 2d direction holder is facing and normalize it
		Vector direction = holder.getLocation().getDirection().setY(0).normalize();
		//Make rocket spawn above the ground
		Firework rocket = holder.getWorld().spawn(holder.getLocation().add(direction.multiply(2)).add(0, 1, 0), Firework.class);
		//Build firework effects
		FireworkMeta meta = rocket.getFireworkMeta();
		FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.withColor(Color.fromBGR(randomGen.nextInt(256), randomGen.nextInt(256), randomGen.nextInt(256)));
		builder.withTrail();
		builder.withFade(Color.fromBGR(randomGen.nextInt(256), randomGen.nextInt(256), randomGen.nextInt(256)));
		meta.addEffect(builder.build());
		rocket.setFireworkMeta(meta);
		rocket.setVelocity(direction.multiply(0.5));
		rocket.setShotAtAngle(true);
	}
}
