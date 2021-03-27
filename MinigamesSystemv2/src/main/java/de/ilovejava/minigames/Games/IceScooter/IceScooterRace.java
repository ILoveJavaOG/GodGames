package de.ilovejava.minigames.Games.IceScooter;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.RunnableWrapper;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.GameLogic.GameOptions;
import de.ilovejava.minigames.MapTools.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to represent the race mini game
 */
public class IceScooterRace extends Game {

	//Name of the game
	public static final String name = "IceScooterRace";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<IceScooterRace> factory = new GameFactory<IceScooterRace>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2IceScooterRace";
		}

		@NotNull
		@Override
		public IceScooterRace createGame(GameMap map) {
			IceScooterRace newGame = new IceScooterRace();
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
	private final PositionMap state = new PositionMap();

	//Map of laps for the player
	private final HashMap<Player, Integer> rounds = new HashMap<>();

	private BukkitTask positions;

	/**
	 * Constructor for the game
	 */
	public IceScooterRace() {
		super(name);
		registerEvents(new IceScooterRaceEvents());
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
				Integer number = location.getIntOption("NUMBER");
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
			adjustPlaces();
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
				.filter(location -> location.getOption("BOAT") != null)
				.limit(activePlayers.size())
				.forEach(boatLocation -> allBoats.add(world.spawn(boatLocation.getLocation(), Boat.class)));
		//Make boats take no damage
		for (Boat boat : allBoats) {
			boat.setInvulnerable(true);
		}
		//Place players into random boats
		Random generator = new Random();
		for (Player playing : activePlayers) {
			int random = generator.nextInt(allBoats.size());
			Boat boat = allBoats.remove(random);
			boat.addPassenger(playing);
			activeBoats.add(boat);
			Position position = new Position(boat, checkPoints.get(0));
			state.add(position);
			rounds.put(playing, 0);
		}
		state.sort();
		//Task to update player position in the race
		positions = new RunnableWrapper(state::sort).runTaskTimerAsynchronously(0L, 1L);
	}

	private void adjustPlaces() {
		Map<UUID, Integer> positions = state.getLookup();
		activeBoats.forEach((Boat driving) -> {
			int place = positions.get(driving.getUniqueId());
			Player driver = (Player) driving.getPassengers().get(0);
			driver.setLevel(place + 1);
		});
	}
	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		positions.cancel();
		if (activePlayers.size() == 1) {
			Boat boat = activeBoats.remove(0);
			Player player = (Player) boat.getPassengers().get(0);
			boat.removePassenger(player);
			activePlayers.remove(player);
			watchingPlayers.add(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> player.teleport(startPosition.getLocation()), 1L);
		}
		gameOver();
	}

	/**
	 * Method to increase the lab count for a given player
	 *
	 * @param player(Player): Player to increase lab count for
	 */
	public void newLap(Player player) {
		int currentLap = rounds.get(player);
		int maxLap = gameMap.getIntOption(GameOptions.MAXLAP);
		if (currentLap == maxLap) {
			Boat boat = activeBoats.get(player.getLevel() - 1);
			activeBoats.remove(boat);
			state.remove(boat);
			boat.removePassenger(player);
			boat.remove();
			activePlayers.remove(player);
			watchingPlayers.add(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> player.teleport(startPosition.getLocation()), 1L);
			if (activePlayers.size() == 1) {
				stopGame();
			}
		} else {
			rounds.put(player, currentLap + 1);
		}
	}
}
