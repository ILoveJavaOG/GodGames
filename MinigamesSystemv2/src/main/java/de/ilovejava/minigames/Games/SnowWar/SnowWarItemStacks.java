package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Class which contains all items
 */
public class SnowWarItemStacks {

	//Snowball item
	protected static final ItemStack fastBall = new ItemStackBuilder(Material.SNOWBALL)
			.getMetaDataBuilder()
			.setDisplayName("Fast Ball")
			.build().build();

	//====================Placeholders====================//
	protected static final ItemStack sonarItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Sonar")
			.setLore("Toete 1 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack supplyDropItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2SupplyDrop")
			.setLore("Toete 2 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack bomberItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Bomber")
			.setLore("Toete 3 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack presentPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Geschenk")
			.setLore("Sammle Geschenke!")
			.build().build();
	//====================Placeholders====================//

	//======================Shields======================//
	public static final Banner[] designs = new Banner[5];

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
	//======================Shields======================//

	//Gloves
	protected static final ItemStack gloves = new ItemStackBuilder(Material.LEATHER_BOOTS)
			.getMetaDataBuilder()
			.setDisplayName("§2Warme Handschuhe")
			.build().build();

	//======================Armor=======================//
	private static final Material[] armorTypes = {Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET};

	public static ItemStack[] getArmor() {
		Random random = new Random();
		ItemStack[] armor = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			ItemStack piece = new ItemStack(armorTypes[i]);
			LeatherArmorMeta meta = (LeatherArmorMeta) piece.getItemMeta();
			assert meta != null;
			meta.setColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
			piece.setItemMeta(meta);
			armor[i] = piece;
		}
		return armor;
	}
	//======================Armor=======================//

	//=====================Shovels======================//
	public static final ItemStack[] shovels = new ItemStack[5];

	static {
		Material[] shovelTypes = new Material[]{Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL};
		for (int i = 0; i < 5; i++) {
			shovels[i] = new ItemStackBuilder(shovelTypes[i])
					.getMetaDataBuilder()
					.setDisplayName("§2 Schaufel")
					.setLore("LOL")
					.build()
					.build();
		}
	}
	//=====================Shovels======================//
}
