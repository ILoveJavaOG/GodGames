package de.ilovejava.minigames.MapTools;

/**
 * Custom exception to be thrown while parsing the map
 * @see MapLoader
 */
public class ParserException extends Exception {

	/**
	 * Constructor for the exception
	 *
	 * @param error(String): Error message
	 */
	public ParserException(String error) {
		super(error);
	}
}
