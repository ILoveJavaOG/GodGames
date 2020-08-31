package de.ilovejava.minigames.Communication;

import de.ilovejava.minigames.GameLogic.Game;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Class to keep track of players and games
 */
public class Tracker {

	//Game associated to player
	private static final HashMap<Player, Integer> gameIds = new HashMap<>();

	/**
	 * Function to check if a player is currently playing a mini game
	 *
	 * @param player(Player) Player to check
	 *
	 * @return True if player is in a game, False if not
	 */
	public static boolean isInGame(@NotNull Player player) {
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
}
