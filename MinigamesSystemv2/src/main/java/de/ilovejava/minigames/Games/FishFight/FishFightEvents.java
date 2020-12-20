package de.ilovejava.minigames.Games.FishFight;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Events;
import de.ilovejava.minigames.Games.FishFight.Enchantments.FishEnchantment;
import de.ilovejava.minigames.Games.FishFight.Items.FishFightItems;
import de.ilovejava.minigames.Games.FishFight.Items.FishWeapon;
import de.ilovejava.minigames.Items.DistributedRandomNumberGenerator;
import de.ilovejava.minigames.MapTools.ProbabilityMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Class to handle events during the fish fight
 */
public class FishFightEvents implements Events {

	//Map of currently catching players
	private final HashMap<Player, FishingStatus> catching = new HashMap<>();

	//Task to start auto bite
	private int waiting;

	/**
	 * Utility method to stop auto bite task
	 *
	 * @param player(Player): Player to stop task for
	 */
	private void stopFishing(Player player) {
		FishingStatus status = catching.get(player);
		if (status != null) {
			status.stop();
		}
	}

	/**
	 * Event called during fishing
	 *
	 * @param event(PlayerFishEvent): Event during fishing
	 */
	public void onFish(PlayerFishEvent event) {
		//Retrieve data
		Player player = event.getPlayer();
		FishHook hook = event.getHook();
		PlayerFishEvent.State currentState = event.getState();
		//Hook throw
		if (currentState == PlayerFishEvent.State.FISHING) {
			//Start the auto bite after 2 seconds
			Random random = new Random();
			boolean positive = random.nextBoolean();
			int extraDelay = random.nextInt(50);
			int delay = 40 + (positive ? extraDelay : -extraDelay);
			waiting = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				int autoFish = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
					event.setCancelled(true);
					//Last state will now be bite
					catching.get(player).setLastState(PlayerFishEvent.State.BITE);
					Bukkit.getServer().getPluginManager().callEvent(new PlayerFishEvent(player, null, hook, PlayerFishEvent.State.BITE));
					//Make hook dip under the water
					hook.setVelocity(new Vector(0, -0.4, 0));
				}, 0L, 40L);
				catching.put(player, new FishingStatus(autoFish, currentState));
			}, delay);
		//Natural catch
		} else if (currentState == PlayerFishEvent.State.CAUGHT_FISH || currentState == PlayerFishEvent.State.CAUGHT_ENTITY) {
			//Spawn fish and remove natural catch
			getFish(player, hook.getLocation());
			if (event.getCaught() != null) event.getCaught().remove();
			stopFishing(player);
		//Hook pull
		} else if (currentState == PlayerFishEvent.State.REEL_IN){
			FishingStatus status = catching.get(player);
			//Player is in Auto catch
			if (status != null) {
				//If player did not have a bite before then there will be no catch
				if (status.getLastState() != PlayerFishEvent.State.BITE) {
					stopFishing(player);
				//If player got the bite spawn the fish
				} else if (status.isCaught()) {
					getFish(player, hook.getLocation());
					if (event.getCaught() != null) event.getCaught().remove();
					catching.remove(player).stop();
				//Player did not catch the fish
				} else {
					stopFishing(player);
				}
			}
			//Stop auto bite because rod is reeled in
			Bukkit.getScheduler().cancelTask(waiting);
		//Miss catch or on ground
		} else if (currentState != PlayerFishEvent.State.BITE) {
			stopFishing(player);
			Bukkit.getScheduler().cancelTask(waiting);
		}
	}

	/**
	 * Method to spawn a fish
	 *
	 * @param player(Player): Player who caught the fish
	 * @param hookLocation(Location): Location of the hook
	 */
	private void getFish(Player player, Location hookLocation) {
		//Get the game and probabilities
		FishFight fight = (FishFight) Tracker.getGame(player);
		ProbabilityMap probabilities = fight.getGameMap().getProbabilities();
		probabilities.setPlaces(1);
		//Build probabilities for weapons
		List<String> foundItems = probabilities.getItems();
		List<FishWeapon> items = new ArrayList<>();
		DistributedRandomNumberGenerator rng = new DistributedRandomNumberGenerator();
		for (int i = 0; i < foundItems.size(); i++) {
			String item = foundItems.get(i);
			FishFightItems enumValue = FishFightItems.valueOf(item);
			items.add(enumValue.createItem(player));
			rng.addNumber(i, probabilities.getProb(item, 1));
		}
		FishWeapon chosen = items.get(rng.getDistributedRandomNumber());
		//Get random number of enchantments and random level for enchantments
		Random random = new Random();
		int numEnchantments = random.nextInt(FishEnchantment.enchantments.size() - 1) + 1;
		List<String> fishEnchantments = new ArrayList<>(FishEnchantment.enchantments.keySet());
		Collections.shuffle(fishEnchantments);
		fishEnchantments = fishEnchantments.subList(0, numEnchantments);
		for (int i = 0; i < numEnchantments; i++) {
			FishEnchantment enchantment = new FishEnchantment(fishEnchantments.get(i));
			chosen.addEnchantment(enchantment, random.nextInt(9) + 1);
		}
		//Spawn fish and make it fly towards player
		Item fish = player.getWorld().dropItem(hookLocation, chosen.getFish());
		fish.setPickupDelay(0);
		fish.setVelocity(player.getLocation().subtract(hookLocation).toVector().normalize());
	}

	/**
	 * Event called on item drop
	 *
	 * @param event(PlayerDropItemEvent): Event on item drop
	 */
	public void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if a player hurts another player
	 *
	 * @param event(EntityDamageByEntityEvent): Damage event
	 */
	public void onDamage(EntityDamageByEntityEvent event) {
		//Custom damage should be dealt
		event.setCancelled(true);
		Player damager = (Player) event.getDamager();
		Player receiver = (Player) event.getEntity();
		//Get active item
		ItemStack item = damager.getInventory().getItem(damager.getInventory().getHeldItemSlot());
		if (item != null) {
			//Look if item contains fish enchantment
			item.getEnchantments().keySet().stream()
					.filter((Enchantment enchantment) -> FishEnchantment.enchantments.containsKey(enchantment.getKey().getKey()))
					.findFirst()
					.ifPresent((Enchantment enchantment) -> {
						//Reduce durability until item breaks
						Damageable damageable = (Damageable) item.getItemMeta();
						if (damageable != null) {
							int damage = damageable.getDamage();
							if (damage == 0) {
								damager.getInventory().setItem(damager.getInventory().getHeldItemSlot(), null);
							} else {
								damageable.setDamage(damage - 1);
								item.setItemMeta((ItemMeta) damageable);
							}
						}
						//Remove health by half a heart and remove player if dead
						double currentHealth = receiver.getHealth();
						if (currentHealth <= 0.5) {
							receiver.setHealth(20.0);
							FishFight fight = (FishFight) Tracker.getGame(damager);
							fight.playerKill(receiver);
						} else {
							receiver.setHealth(currentHealth - 0.5);
						}
					});
		}
	}


}
