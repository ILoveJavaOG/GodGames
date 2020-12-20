package de.ilovejava.minigames.Games.SnowWar.Items;

import de.ilovejava.minigames.Items.GameItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Shield extends GameItem {

	private static final Banner[] designs = new Banner[5];

	static {
		//Tree
		ItemStack shield = new ItemStack(Material.SHIELD);
		ItemMeta meta = shield.getItemMeta();
		BlockStateMeta bmeta = (BlockStateMeta) meta;
		assert bmeta != null;
		Banner tree = tree(bmeta);
		tree.update();
		designs[0] = tree(bmeta);
		designs[1] = present(bmeta);
		designs[2] = present2(bmeta);
		designs[3] = shoe(bmeta);
		designs[4] = snowflake(bmeta);
	}
	/**
	 * Overloaded constructor call
	 *
	 * @param holder
	 */
	public Shield(Player holder) {
		super(holder, Material.STICK, "SHIELD", 1, 4);
	}

	/**
	 * Method to be implemented to use the item
	 */
	@Override
	protected void useItem() {
		ItemStack shield = new ItemStack(Material.SHIELD);
		ItemMeta meta = shield.getItemMeta();
		BlockStateMeta bmeta = (BlockStateMeta) meta;
		assert bmeta != null;
		Random random = new Random();
		bmeta.setBlockState(designs[random.nextInt(5)]);
		shield.setItemMeta(bmeta);
		holder.getInventory().addItem(shield);
		holder.updateInventory();
	}

	private static Banner tree(BlockStateMeta bmeta) {
		Banner banner = (Banner) bmeta.getBlockState();
		banner.setBaseColor(DyeColor.GREEN);
		List<Pattern> patterns = new ArrayList<>(5);
		patterns.add(new Pattern(DyeColor.RED, PatternType.BRICKS));
		patterns.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_SMALL));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE));
		patterns.add(new Pattern(DyeColor.WHITE, Objects.requireNonNull(PatternType.getByIdentifier("rud"))));
		patterns.add(new Pattern(DyeColor.WHITE, Objects.requireNonNull(PatternType.getByIdentifier("ld"))));
		banner.setPatterns(patterns);
		banner.update();
		return banner;
	}

	private static Banner present(BlockStateMeta bmeta) {
		Banner banner = (Banner) bmeta.getBlockState();
		banner.setBaseColor(DyeColor.RED);
		List<Pattern> patterns = new ArrayList<>(4);
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.FLOWER));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRAIGHT_CROSS));
		patterns.add(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
		banner.setPatterns(patterns);
		banner.update();
		return banner;
	}

	private static Banner present2(BlockStateMeta bmeta) {
		Banner banner = (Banner) bmeta.getBlockState();
		banner.setBaseColor(DyeColor.LIME);
		List<Pattern> patterns = new ArrayList<>(4);
		patterns.add(new Pattern(DyeColor.PURPLE, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE));
		patterns.add(new Pattern(DyeColor.PURPLE, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE));
		banner.setPatterns(patterns);
		banner.update();
		return banner;
	}

	private static Banner shoe(BlockStateMeta bmeta) {
		Banner banner = (Banner) bmeta.getBlockState();
		banner.setBaseColor(DyeColor.BLACK);
		List<Pattern> patterns = new ArrayList<>(4);
		patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(DyeColor.RED, PatternType.SQUARE_BOTTOM_LEFT));
		patterns.add(new Pattern(DyeColor.WHITE, Objects.requireNonNull(PatternType.getByIdentifier("ts"))));
		patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
		banner.setPatterns(patterns);
		banner.update();
		return banner;
	}

	private static Banner snowflake(BlockStateMeta bmeta) {
		Banner banner = (Banner) bmeta.getBlockState();
		banner.setBaseColor(DyeColor.WHITE);
		List<Pattern> patterns = new ArrayList<>(3);
		patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.GRADIENT));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.FLOWER));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_TOP));
		banner.setPatterns(patterns);
		banner.update();
		return banner;
	}
}
