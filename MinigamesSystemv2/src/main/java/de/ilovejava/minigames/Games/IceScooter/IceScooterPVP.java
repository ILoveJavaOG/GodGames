package de.ilovejava.minigames.Games.IceScooter;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Events.BoatBlockCollisionEvent;
import de.ilovejava.minigames.Events.BoatEntityCollisionEvent;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.GameSelector.Selector;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IceScooterPVP extends Game {

	//Name of the game
	public static final String name = "IceScooterPVP";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<IceScooterPVP> factory = new GameFactory<IceScooterPVP>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2IceScooterPVP";
		}

		@NotNull
		@Override
		public IceScooterPVP createGame(GameMap map) {
			IceScooterPVP newGame = new IceScooterPVP();
			newGame.loadMap(map);
			newGame.setup();
			return newGame;
		}
	};

	//Events will be handled by this class
	private final IceScooterPVPEvents events = new IceScooterPVPEvents();

	//Start location for all players
	private CustomLocation startPosition;

	//List of currently active boats
	private final List<Boat> activeBoats = new ArrayList<>();

	/**
	 * Constructor for the game
	 */
	public IceScooterPVP() {
		super(name);
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		//Map for checkpoints
		for (CustomLocation location : gameMap.getLocations()) {
			//Start point
			if (location.getName().equals("START")) {
				startPosition = location;
			}
		}
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
			int random = generator.nextInt(allBoats.size());
			Boat boat = allBoats.remove(random);
			boat.addPassenger(playing);
			activeBoats.add(boat);
			playing.setLevel(3);
		}
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		if (activePlayers.size() == 1) {
			Boat boat = activeBoats.remove(0);
			Player player = (Player) boat.getPassengers().get(0);
			boat.removePassenger(player);
			activePlayers.remove(player);
			watchingPlayers.add(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> player.teleport(startPosition.getLocation()), 1L);
		}
		Selector.selectors.get(name).nextGame(getId(), gameMap);
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
		}
	}

	public void playerDie(Player player) {
		Boat boat = (Boat) player.getVehicle();
		activeBoats.remove(boat);
		if (boat != null) {
			boat.removePassenger(player);
		}
		activePlayers.remove(player);
		watchingPlayers.add(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> player.teleport(startPosition.getLocation()), 1L);
		if (activePlayers.size() == 1) {
			stopGame();
		}
	}
}
