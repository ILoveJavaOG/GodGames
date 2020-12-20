package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum KillStreak {
	SONAR(new ItemStackBuilder(Material.ENDER_EYE)
			.getMetaDataBuilder()
			.setDisplayName("Sonar")
			.setLore("Deckt andere Spieler auf")
			.build().build()),
	SUPPLYDROP(new ItemStackBuilder(Material.FIREWORK_ROCKET)
			.getMetaDataBuilder()
			.setDisplayName("Supply Drop")
			.setLore("Rechtsklick zum aktivieren", "Wirft Supply Drop auf geklickte Stelle")
			.build().build()),
	BOMBER(new ItemStackBuilder(Material.ENDER_PEARL)
			.getMetaDataBuilder()
			.setDisplayName("Bomber")
			.setLore("Werfe um einen Bombardierung zu rufen",
					 "Zentrum der Bombadierung ist",
					 "Am Aufschlagsort")
			.build().build());

	@Getter
	private final ItemStack display;

	KillStreak(ItemStack build) {
		this.display = build;
	}
}
