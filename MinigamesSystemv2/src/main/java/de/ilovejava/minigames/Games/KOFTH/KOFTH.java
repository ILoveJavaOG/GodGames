package de.ilovejava.minigames.Games.KOFTH;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to represent the fish fight game
 */
public class KOFTH extends Game {

	//Name of the game
	private static final String name = "KOFTH";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<KOFTH> factory = new GameFactory<KOFTH>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2KOFTH";
		}

		@NotNull
		@Override
		public KOFTH createGame(GameMap map) {
			KOFTH newGame = new KOFTH();
			newGame.loadMap(map);
			newGame.setup();
			return newGame;
		}
	};

	private int heightChecker;

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		spawns = gameMap.getLocations().stream()
				.filter((CustomLocation loc) -> loc.getData("SPAWN") != null)
				.collect(Collectors.toList());
	}

	//List of possible spawns
	private List<CustomLocation> spawns = new ArrayList<>();

	/**
	 * Constructor to create a game
	 */
	public KOFTH() {
		super(name);
		registerEvents(new KOFTHEvents());
	}


	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		//Place players into random boats
		Random generator = new Random();
		for (Player playing : activePlayers) {
			Bukkit.getOnlinePlayers().forEach((Player hiden) -> playing.showPlayer(Lobby.getPlugin(), hiden));
			//Reset player stats
			playing.setHealth(20.f);
			playing.setFoodLevel(20);
			playing.setLevel(playing.getLocation().getBlockY());
			playing.setExp(0.0f);
			//Get random spawn point and remove it
			int random = generator.nextInt(activePlayers.size());
			CustomLocation spawnPoint = spawns.remove(random);
			playing.teleport(spawnPoint.getLocation());
		}
		heightChecker = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			Optional<Player> lowest = activePlayers.stream()
					.min(Comparator.comparingDouble(player -> player.getLocation().getY()));
			lowest.ifPresent(this::playerKill);
		}, 5*20L, 0L);
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		Bukkit.getScheduler().cancelTask(heightChecker);
		gameOver();
	}

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	@Override
	public void playerJoin(Player player) {
		//Nothing needs to be done right now
	}

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player(Player) Leaving player
	 */
	@Override
	public void playerLeave(Player player) {
		//Stop the game if there arent any more players
		if (activePlayers.size() <= 1) {
			stopGame();
		}
	}

	/**
	 * Method which is to be called when a player gets killed
	 *
	 * @param receiver(Player): Player who got killed
	 */
	public void playerKill(Player receiver) {
		activePlayers.remove(receiver);
		watchingPlayers.add(receiver);
		receiver.teleport(spawns.get(0).getLocation());
		receiver.sendMessage("You died :(");
		if (activePlayers.size() == 1) {
			stopGame();
		}
	}
}
