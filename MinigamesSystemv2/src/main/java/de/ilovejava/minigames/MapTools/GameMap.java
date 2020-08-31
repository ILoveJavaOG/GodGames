package de.ilovejava.minigames.MapTools;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 * Class to represent a map for a given game
 */
public class GameMap {

	//World the map is set in
	@Getter
	private final World world;

	//Name of the map
	@Getter
	private final String mapName;

	//Locations contained in the map
	@Getter
	private final List<CustomLocation> locations;

	//Optional options for the map
	@Getter
	private final HashMap<DefaultOptions, Object> extraOptions = new HashMap<>();

	@Getter
	private final HashMap<String, Pair<Double, Double>> items = new HashMap<>();

	@Getter
	private final ProbabilityMap probabilities;

	/**
	 * Constructor for map
	 *
	 * @param mapName(String) Name of the map
	 * @param world(World) World of the map
	 * @param locations(List) List of locations in the map
	 */
	public GameMap(String mapName, World world, List<CustomLocation> locations, ProbabilityMap items) {
		this.mapName = mapName;
		this.locations = locations;
		this.world = world;
		this.probabilities = items;
	}

	/**
	 * Method to add option to the map
	 *
	 * @param key(String) Key to retrieve option by
	 * @param option(Object) Option to store
	 */
	protected void addOption(DefaultOptions key, Object option) {
		extraOptions.put(key, option);
	}

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 * @param castTo(T) CLass to cast to
	 *
	 * @return Object at key casted to T. May be null
	 */
	@Nullable
	public <T> T getOption(DefaultOptions key, @NotNull Class<T> castTo) {
		return castTo.cast(extraOptions.get(key));
	}

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 *
	 * @return Object at key. May be null
	 */
	@Nullable
	public Object getOption(DefaultOptions key) {
		return extraOptions.get(key);
	}
}
