package de.ilovejava.minigames.Communication;

import de.ilovejava.minigames.GameLogic.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to keep track of players and games
 */
public class Tracker {

	//Game associated to player
	private static final ConcurrentHashMap<Player, Integer> gameIds = new ConcurrentHashMap<>();

	private static final ConcurrentHashMap<UUID, Player> entityConnections = new ConcurrentHashMap<>();

	/**
	 * Function to check if a player is currently playing a mini game
	 *
	 * @param player(Player) Player to check
	 *
	 * @return True if player is in a game, False if not
	 */
	public static boolean isInGame(Player player) {
		return gameIds.containsKey(player);
	}

	/**
	 * Function to store a game id inside the player
	 *
	 * @param player(Player) Player to store data in
	 * @param gameId(int) ID to store
	 */
	public static void registerPlayer(@NotNull Player player, int gameId) {
		gameIds.put(player, gameId);
	}

	/**
	 * Function to remove data from Player
	 *
	 * @param player(Player) Player to remove data from
	 */
	public static void unregisterPlayer(@NotNull Player player) {
		gameIds.remove(player);
	}

	/**
	 * Function to get the game id from the player
	 *
	 * @param player(Player) Player to retrieve id from
	 *
	 * @return Game id
	 */
	public static int getGameId(@NotNull Player player) {
		return gameIds.get(player);
	}

	public static void redirectEvent(Player player, Event event) {
		if (isInGame(player)) Tracker.getGame(player).callEvent(event);
	}

	public static void redirectEvent(UUID entity, Event event) {
		if (entityConnections.containsKey(entity)) redirectEvent(entityConnections.get(entity), event);
	}

	public static void bindEntity(Entity entity, Player owner) {
		entityConnections.put(entity.getUniqueId(), owner);
	}

	/**
	 * Function to get the game for the given player
	 *
	 * @param player(Player): Player to retrieve game from
	 *
	 * @return Game player is playing
	 */
	public static Game getGame(Player player) {
		return Game.allGames.get(getGameId(player));
	}

	public static Player consume(Entity entity) {
		return entityConnections.remove(entity.getUniqueId());
	}
}
