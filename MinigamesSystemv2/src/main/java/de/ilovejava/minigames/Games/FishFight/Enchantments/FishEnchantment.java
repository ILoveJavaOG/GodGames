package de.ilovejava.minigames.Games.FishFight.Enchantments;

import de.ilovejava.lobby.Lobby;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Custom enchantment for a item to be a fish weapon
 */
public class FishEnchantment extends Enchantment {

    //Map of all known enchantment namespaced keys
    public static HashMap<String, NamespacedKey> enchantments = new HashMap<>();

    /**
     * Method add a enchantment to the list of known enchantments
     *
     * @param name(String): Name of the enchantment. Must be lowercase for namespace
     */
    private static void createKey(String name) {
        enchantments.put(name, new NamespacedKey(Lobby.getPlugin(), name));
    }

    //Add enchantments
    static{
        createKey("smell");
        createKey("salted");
        createKey("pointy");
        createKey("nemo");
    }

    //Name of the enchantment
    private final String title;

    /**
     * Constructor for FishEnchantment
     *
     * @param name(String): Name of the enchantment
     */
    public FishEnchantment(String name) {
        //Create enchantment by namespace key
        super(NamespacedKey.minecraft(name.toLowerCase()));
        this.title = name;
        //Register enchantment
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("ERROR IN REGISTRATION");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //Enchantment was already registered
        }
    }

    /**
     * Getter for title
     *
     * @return String: Name of the book
     */
    @Override
    public @NotNull String getName() {
        return title;
    }

    /**
     * Method to retrieve the max enchantment level for an item
     *
     * @return Max enchantment level
     */
    @Override
    public int getMaxLevel() {
        return 10;
    }

    /**
     * Method to retrieve the start enchantment level for an item
     *
     * @return Start enchantment level
     */
    @Override
    public int getStartLevel() {
        return 1;
    }

    /**
     * Method to get valid enchantment targets
     *
     * @return Target for enchantments
     */
    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    /**
     * Method to check if enchantment is a treasure
     *
     * @return Always false
     */
    @Override
    public boolean isTreasure() {
        return false;
    }

    /**
     * Method to check if enchantment is cursed
     *
     * @return Always false
     */
    @Override
    public boolean isCursed() {
        return false;
    }

    /**
     * Method to check if the enchantment conflicts with a different one
     *
     * @return Always false
     */
    @Override
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return false;
    }

    /**
     * Method to check if an item stack can be enchanted using this enchantment
     *
     * @param itemStack(ItemStack): ItemStack to enchant
     *
     * @return True if item is any fish
     */
    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material mat = itemStack.getType();
        return (mat == Material.TROPICAL_FISH ||
            mat == Material.PUFFERFISH ||
            mat == Material.COOKED_SALMON ||
            mat == Material.SALMON ||
            mat == Material.COD ||
            mat == Material.COOKED_COD);
    }
}
