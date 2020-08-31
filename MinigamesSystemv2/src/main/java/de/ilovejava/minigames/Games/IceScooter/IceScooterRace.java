package de.ilovejava.minigames.Games.IceScooter;
import com.mojang.datafixers.util.Pair;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Events.BoatBlockCollisionEvent;
import de.ilovejava.minigames.Events.BoatEntityCollisionEvent;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.MapTools.CheckPoint;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

	//Events will be handled by this class
	private final IceScooterRaceEvents events = new IceScooterRaceEvents();

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

	private int positions;

	/**
	 * Constructor for the game
	 */
	public IceScooterRace() {
		super(name);
	}

	/**
	 * Method to prepare the race
	 */
	public void setup() {
		//Map for checkpoints
		HashMap<Integer, CustomLocation> cps  = new HashMap<>();
		for (CustomLocation location : gameMap.getLocations()) {
			//Start point
			if (location.getName().equalsIgnoreCase("start")) {
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
			state.put(boat, new Pair<>(checkPoints.get(0), checkPoints.get(0)));
			rounds.put(playing, 1);
		}
		//Task to update player position in the race
		positions = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			activeBoats.sort((Boat first, Boat second) -> {
				//Order by checkpoint
				CheckPoint f = state.get(first).getFirst();
				CheckPoint s = state.get(first).getFirst();
				if (f.getNumber() > s.getNumber()) {
					return 1;
				} else  {
					//Order by distance to next checkpoint
					double distanceF = f.distance(first.getLocation());
					double distanceS = s.distance(second.getLocation());
					if (distanceF > distanceS) {
						return 1;
					}
				}
				return -1;
			});
			//Change level to position in race
			for (int i = 0; i < activeBoats.size(); i++) {
				List<Entity> riding = activeBoats.get(i).getPassengers();
				if (!riding.isEmpty()) {
					((Player) activeBoats.get(i).getPassengers().get(0)).setLevel(i + 1);
				}
			}
		}, 0L, 5L);
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		Bukkit.getScheduler().cancelTask(positions);
	}

	/**
	 * Event listener
	 *
	 * @param event(Event) Event which is called and regards the game
	 */
	@Override
	public void callEvent(Event event) {
		if (event instanceof VehicleExitEvent) {
			events.onExitVehicle((VehicleExitEvent) event);
		} else if (event instanceof BoatBlockCollisionEvent) {
			events.onBoatBlockCollision((BoatBlockCollisionEvent) event);
		} else if (event instanceof BoatEntityCollisionEvent) {
			events.onBoatEntityCollision((BoatEntityCollisionEvent) event);
		} else if (event instanceof PlayerInteractEvent) {
			events.onRightClick((PlayerInteractEvent) event);
		} else if (event instanceof EntityDamageEvent) {
			events.onDamage((EntityDamageEvent) event);
		} else if (event instanceof VehicleMoveEvent) {
			events.onMove((VehicleMoveEvent) event, state, checkPoints);
		}
	}

	/**
	 * Method to increase the lab count for a given player
	 *
	 * @param player(Player): Player to increase lab count for
	 */
	public void newLap(Player player) {
		rounds.put(player, rounds.get(player) + 1);
	}

}
