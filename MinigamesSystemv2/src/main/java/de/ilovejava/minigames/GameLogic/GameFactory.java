package de.ilovejava.minigames.GameLogic;

import de.ilovejava.minigames.MapTools.GameMap;

/**
 * Interface to create a de.ilovejava.minigames.Games inside the selector
 * @see de.ilovejava.minigames.GameSelector.Selector
 *
 * @param <T> Game which will be created
 */
public interface GameFactory<T extends Game> {

	/**
	 * Utility method to get the type of the game
	 *
	 * @return Type of the game as String
	 */
	String getGame();

	/**
	 * Utility method to get the display inside the selector
	 *
	 * @return Display in the selector
	 */
	String getTitle();

	/**
	 * Factory to create a game with the given map
	 *
	 * @param map(GameMap) Map of the game
	 *
	 * @return Instance of a game. Should have the map loaded
	 */
	T createGame(GameMap map);
}
