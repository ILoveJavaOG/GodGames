package de.ilovejava.minigames.Games.IceScooter;
import com.mojang.datafixers.util.Pair;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.GameLogic.GameOptions;
import de.ilovejava.minigames.MapTools.CheckPoint;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to represent the race mini game
 */
public class IceScooterTimeTrial extends Game {

	//Name of the game
	public static final String name = "IceScooterTimeTrial";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<IceScooterTimeTrial> factory = new GameFactory<IceScooterTimeTrial>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2IceScooterTimeTrial";
		}

		@NotNull
		@Override
		public IceScooterTimeTrial createGame(GameMap map) {
			IceScooterTimeTrial newGame = new IceScooterTimeTrial();
			newGame.loadMap(map);
			newGame.setup();
			return newGame;
		}
	};

	//Start location for all players
	private CustomLocation startPosition;

	//Ordered list of checkpoints
	private final List<CheckPoint> checkPoints = new ArrayList<>();

	//List of currently active boats
	private final List<Boat> activeBoats = new ArrayList<>();

	//Map of currently driving boats
	private final HashMap<Boat, Pair<CheckPoint, CheckPoint>> state = new HashMap<>();

	//Map of laps for the player
	private final HashMap<Player, Integer> rounds = new HashMap<>();

	private final HashMap<Player, List<Long>> times = new HashMap<>();

	/**
	 * Constructor for the game
	 */
	public IceScooterTimeTrial() {
		super(name);
		registerEvents(new IceScooterTimeTrialEvents());
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		//Map for checkpoints
		HashMap<Integer, CustomLocation> cps  = new HashMap<>();
		for (CustomLocation location : gameMap.getLocations()) {
			//Start point
			if (location.getName().equals("START")) {
				startPosition = location;
				//Checkpoints
			} else if (location.getName().contains("CHECKPOINT")) {
				//Retrieve number of checkpoint
				Integer number = location.getData("NUMBER", Integer.class);
				//Check if another edge of the checkpoint has been found
				if (cps.containsKey(number)) {
					CustomLocation first = cps.get(number);
					checkPoints.add(new CheckPoint(first, location));
					//No checkpoint edge existed before
				} else {
					cps.put(number, location);
				}
			}
		}
		//Sort checkpoints by number
		checkPoints.sort((CheckPoint first, CheckPoint second) -> first.getNumber() < second.getNumber() ? -1 : 1);
	}

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	@Override
	public void playerJoin(@NotNull Player player) {
		player.sendMessage("Joined: " + name + " on map " + getGameMap().getMapName());
		player.teleport(startPosition.getLocation());
	}

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player(Player) Leaving player
	 */
	@Override
	public void playerLeave(@NotNull Player player) {
		player.sendMessage("Left: " + name + " on map " + getGameMap().getMapName());
		Boat boat = (Boat) player.getVehicle();
		if (boat != null) {
			boat.removePassenger(player);
			activeBoats.remove(boat);
		}
	}

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		List<Boat> allBoats = new ArrayList<>();
		World world = gameMap.getWorld();
		//Spawn boats at given locations
		gameMap.getLocations().stream()
				.filter(location -> location.getData("BOAT") != null)
				.limit(activePlayers.size())
				.forEach(boatLocation -> allBoats.add(world.spawn(boatLocation.getLocation(), Boat.class)));
		//Make boats take no damage
		for (Boat boat : allBoats) {
			boat.setInvulnerable(true);
		}
		//Place players into random boats
		Random generator = new Random();
		for (Player playing : activePlayers) {
			//activePlayers.forEach((Player play) -> play.hidePlayer(Lobby.getPlugin(), playing));
			int random = generator.nextInt(allBoats.size());
			Boat boat = allBoats.remove(random);
			boat.addPassenger(playing);
			activeBoats.add(boat);
			state.put(boat, new Pair<>(checkPoints.get(0), checkPoints.get(0)));
			rounds.put(playing, 0);
		}
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		gameOver();
	}

	/**
	 * Method to increase the lab count for a given player
	 *
	 * @param player(Player): Player to increase lab count for
	 */
	public void newLap(Player player) {
		int currentLap = rounds.get(player);
		if (currentLap > 0) {
			stopTime(player);
		}
		int maxLap = gameMap.getOption(GameOptions.MAXLAP.name(), Integer.class);
		if (currentLap == maxLap) {
			Boat boat = activeBoats.get(player.getLevel() - 1);
			activeBoats.remove(boat);
			boat.removePassenger(player);
			boat.remove();
			activePlayers.remove(player);
			watchingPlayers.add(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> player.teleport(startPosition.getLocation()), 1L);
			if (activePlayers.isEmpty()) {
				stopGame();
			}
		} else {
			rounds.put(player, currentLap + 1);
			startTime(player);
		}
	}

	public void startTime(Player player) {
		Calendar calendar = Calendar.getInstance();
		long start = calendar.getTimeInMillis();
		if (times.containsKey(player)) {
			times.get(player).add(start);
		} else {
			List<Long> lapTimes = new ArrayList<>(gameMap.getOption(GameOptions.MAXLAP.name(), Integer.class));
			times.put(player, lapTimes);
		}
	}

	public void stopTime(Player player) {
		Calendar calendar = Calendar.getInstance();
		int lap = rounds.get(player);
		long stop = calendar.getTimeInMillis();
		long start = times.get(player).get(lap - 1);
		times.get(player).set(lap - 1, stop - start);
	}
}
