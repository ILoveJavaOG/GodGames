package de.ilovejava.minigames.GameLogic;


import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to make sure any game has events called by Game
 * @see Game
 */
public interface Events {

	/**
	 * Event called on item drop
	 *
	 * @param event(PlayerDropItemEvent): Event on item drop
	 */
	default void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if a player loosed food
	 *
	 * @param event(FoodLevelChangeEvent): Event on food depletion
	 */
	default void onFoodDepletion(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if a player regenerates health
	 *
	 * @param event(EntityRegainHealthEvent): Event on health regeneration
	 */
	default void onRegen(EntityRegainHealthEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if player eats
	 *
	 * @param event(PlayerItemConsumeEvent): Event on food consumption
	 */
	default void onConsumption(PlayerItemConsumeEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if a player hurts another player
	 *
	 * @param event(EntityDamageByEntityEvent): Damage event
	 */
	default void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Event if a player breaks a block
	 *
	 * @param event(BlockBreakEvent): BlockBreakEvent event
	 */
	default void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

	default void onBlockPlace(@NotNull BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	default void onPlayerTeleport(PlayerTeleportEvent event) {
		event.setCancelled(true);
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
		}
	}
}
