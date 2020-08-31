package de.ilovejava.minigames.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to create random numbers
 */
public class DistributedRandomNumberGenerator {

	//Distribution of integers to possibilities
	private final Map<Integer, Double> distribution = new HashMap<>();

	//Sum of the distribution
	private double distSum;

	/**
	 * Method to add another number to the generator
	 *
	 * @param value(int): Value to add
	 * @param distribution(double): Probability for the number
	 */
	public void addNumber(int value, double distribution) {
		//Check if number exists
		if (this.distribution.get(value) != null) {
			distSum -= this.distribution.get(value);
		}
		this.distribution.put(value, distribution);
		distSum += distribution;
	}

	/**
	 * Get number from generator
	 *
	 * @return Random number using given distribution
	 */
	public int getDistributedRandomNumber() {
		double rand = Math.random();
		double ratio = 1.0f / distSum;
		double tempDist = 0;
		for (Integer i : distribution.keySet()) {
			tempDist += distribution.get(i);
			if (rand / ratio <= tempDist) {
				return i;
			}
		}
		return 0;
	}

}