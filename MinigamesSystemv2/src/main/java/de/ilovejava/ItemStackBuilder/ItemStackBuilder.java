package de.ilovejava.ItemStackBuilder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemStackBuilder {

	private final ItemStack item;

	public ItemStackBuilder(ItemStack stack) {
		this.item = new ItemStack(stack);
	}

	public ItemStackBuilder(Material type) {
		this.item = new ItemStack(type);
	}

	public ItemStackBuilder(Material type, int amount) {
		this.item = new ItemStack(type, amount);
	}

	public ItemStack build() {
		return item;
	}

	public ItemStackBuilder addEnchantment(Enchantment ench, int level) {
		this.item.addEnchantment(ench, level);
		return this;
	}

	public ItemStackBuilder addEnchantments(Map<Enchantment,Integer> enchantments) {
		this.item.addUnsafeEnchantments(enchantments);
		return this;
	}

	public ItemStackBuilder	addUnsafeEnchantment(Enchantment ench, int level) {
		this.item.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemStackBuilder	addUnsafeEnchantments(Map<Enchantment,Integer> enchantments) {
		this.item.addUnsafeEnchantments(enchantments);
		return this;
	}

	public ItemStackBuilder	setAmount(int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemStackBuilder	setType(Material type) {
		this.item.setType(type);
		return this;
	}

	public ItemMetaBuilder getMetaDataBuilder() {
		return new ItemMetaBuilder(this.item);
	}
}
