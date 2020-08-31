package de.ilovejava.minigames.GameLogic;

import org.bukkit.entity.Player;

/**
 * Interface to make sure any game has events called by Game
 * @see Game
 */
public interface Events {

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	void playerJoin(Player player);

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player(Player) Leaving player
	 */
	void playerLeave(Player player);

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	void startGame();

	/**
	 * Method to be called when the game is over
	 */
	void stopGame();
}
