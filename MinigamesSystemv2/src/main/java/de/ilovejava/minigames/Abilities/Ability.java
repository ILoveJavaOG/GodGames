package de.ilovejava.minigames.Abilities;

import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import de.ilovejava.lobby.Lobby;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Ability {

	public static ConcurrentHashMap<Player, Ability[]> current = new ConcurrentHashMap<>();

	private final int slot;
	private final long cooldown;

	protected final Player holder;

	private long cooldownStart = new Date().getTime();


	private final ItemStack display;
	private final ItemStack ready;

	public Ability(Player holder, Material type, String name, List<String> lore, int slot, double cooldown) {
		this.holder = holder;
		this.display = new ItemStackBuilder(type)
				.setAmount(((int) cooldown) + 1)
				.getMetaDataBuilder()
				.setDisplayName(name)
				.setLore(lore)
				.build().build();
		this.slot = slot;
		holder.getInventory().setItem(slot, display);
		this.cooldown = (long) (cooldown*1000);
		this.ready = new ItemStackBuilder(this.display)
				.setAmount(1)
				.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
				.getMetaDataBuilder()
				.setUnbreakable(true)
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
				.build().build();
		animate();
	}

	public void activate() {
		Date date = new Date();
		long start = date.getTime();
		if (start - cooldownStart >= this.cooldown) {
			this.cooldownStart = start;
			holder.getInventory().setItem(slot, new ItemStackBuilder(this.display).setAmount(((int) (this.cooldown/1000)) + 1).build());
			this.use();
			this.animate();
		}
	}

	private void animate() {
		long delay = ((this.cooldown % 1000) + 49)/50;
		ItemStack placeHolder = this.display.clone();
		Player holder = this.holder;
		new BukkitRunnable() {
					@Override
					public void run() {
						int current = placeHolder.getAmount() - 1;
						ItemStack changed;
						if (current == 0) {
							changed = ready;
							cancel();
						} else {
							changed = placeHolder;
							changed.setAmount(current);
						}
						holder.getInventory().setItem(slot, changed);
					}
		}.runTaskTimer(Lobby.getPlugin(), delay, 20L);
	}

	protected abstract void use();
}
