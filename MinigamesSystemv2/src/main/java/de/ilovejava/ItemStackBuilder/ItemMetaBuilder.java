package de.ilovejava.ItemStackBuilder;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemMetaBuilder {

	private final ItemMeta meta;
	private final ItemStack item;

	public ItemMetaBuilder(@NotNull ItemStack item) {
		this.meta = item.getItemMeta();
		this.item = item;
	}


	public ItemMetaBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
		this.meta.addAttributeModifier(attribute, modifier);
		return this;
	}

	public ItemMetaBuilder addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
		this.meta.addEnchant(ench, level, ignoreLevelRestriction);
		return this;
	}

	public ItemMetaBuilder addItemFlags(ItemFlag... itemFlags) {
		this.meta.addItemFlags(itemFlags);
		return this;
	}

	public ItemMetaBuilder removeAttributeModifier(EquipmentSlot slot) {
		this.meta.removeAttributeModifier(slot);
		return this;
	}

	public ItemMetaBuilder removeEnchant(Enchantment ench) {
		this.meta.removeEnchant(ench);
		return this;
	}

	public ItemMetaBuilder removeItemFlags(ItemFlag... itemFlags) {
		this.meta.removeItemFlags(itemFlags);
		return this;
	}

	public ItemMetaBuilder setAttributeModifiers(Multimap<Attribute,AttributeModifier> attributeModifiers) {
		this.meta.setAttributeModifiers(attributeModifiers);
		return this;
	}

	public ItemMetaBuilder setCustomModelData(Integer data) {
		this.meta.setCustomModelData(data);
		return this;
	}

	public ItemMetaBuilder setDisplayName(String name) {
		this.meta.setDisplayName(name);
		return this;
	}

	public ItemMetaBuilder setLocalizedName(String name) {
		this.meta.setLocalizedName(name);
		return this;
	}

	public ItemMetaBuilder setLore(List<String> lore) {
		this.meta.setLore(lore);
		return this;
	}

	public ItemMetaBuilder setLore(String... lore) {
		this.meta.setLore(new ArrayList<>(Arrays.asList(lore)));
		return this;
	}

	public ItemMetaBuilder setUnbreakable(boolean unbreakable) {
		this.meta.setUnbreakable(unbreakable);
		return this;
	}

	public ItemStackBuilder build() {
		this.item.setItemMeta(meta);
		return new ItemStackBuilder(this.item);
	}


}
