package de.ilovejava.minigames.Communication;

import java.util.TreeMap;

/**
 * Util class to translate numbers from 1 to 10 to numerals
 */
public class NumeralTranslator {

	//Map of int to numeral
	private static final TreeMap<Integer, String> numerals = new TreeMap<>();

	//Store base values
	static {
		numerals.put(10, "X");
		numerals.put(9, "IX");
		numerals.put(5, "V");
		numerals.put(4, "IV");
		numerals.put(1, "I");
	}

	/**
	 * Function to transform int into numeral
	 *
	 * @param num(int): Number to transform
	 *
	 * @return Roman numeral for number
	 */
	public static String toNumeral(int num) {
		int floor = numerals.floorKey(num);
		return num == floor ? numerals.get(num) : numerals.get(floor) + toNumeral(num - floor);
	}
}
