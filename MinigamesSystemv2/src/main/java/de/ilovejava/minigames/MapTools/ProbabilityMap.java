package de.ilovejava.minigames.MapTools;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to represent probabilities for items at a given place
 */
public class ProbabilityMap {

	//Map of items and probabilities
	private final HashMap<String, List<Double>> mapped = new HashMap<>();

	@Getter
	private final List<String> items;

	private final List<Pair<Double, Double>> probabilities;

	/**
	 * Constructor for the map
	 *
	 * @param items(List): List of items
	 * @param probabilities(List): List of pairs of upper and lower bounds
	 */
	ProbabilityMap(List<String> items, List<Pair<Double, Double>> probabilities) {
		this.items = items;
		this.probabilities = probabilities;
	}

	/**
	 * Method to recalculate the map
	 *
	 * @param places(int): Number of places to calculate
	 */
	public void setPlaces(int places) {
		for (int i = 0; i < items.size(); i++) {
			//Make linear scaling from min to max
			List<Double> probs = new ArrayList<>();
			double min = probabilities.get(i).getFirst();
			double max = probabilities.get(i).getSecond();
			double diff = max - min;
			for (int place = 0; place < places; place++) {
				probs.add(min + place*diff/places);
			}
			mapped.put(items.get(i), probs);
		}
	}

	/**
	 * Method to retrieve probability of item for a given place
	 *
	 * @param item(String): Item to look at
	 * @param place(int): Place to look at
	 *
	 * @return Probability for item at given place
	 */
	public double getProb(String item, int place) {
		return mapped.get(item).get(place - 1);
	}
}
