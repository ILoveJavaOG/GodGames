package de.ilovejava.minigames.Games.FireBall;

import de.ilovejava.minigames.Communication.RunnableWrapper;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FireBall extends Game {

	//======================================Game======================================//

	private List<Set<Player>> teams = new ArrayList<>();

	private BukkitTask fireBallSpawner = null;

	//Name of the game
	private static final String name = "FireBall";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<FireBall> factory = new GameFactory<FireBall>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2FireBall";
		}

		@NotNull
		@Override
		public FireBall createGame(GameMap map) {
			FireBall newGame = new FireBall();
			newGame.loadMap(new FireBallMap(map));
			newGame.setup();
			return newGame;
		}
	};

	/**
	 * Constructor to create a game
	 */
	public FireBall() {
		super(name);
		registerEvents(new FireBallEvents(this));
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {

	}

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player
	 */
	@Override
	protected void playerJoin(Player player) {
		player.getInventory().clear();
	}

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player
	 */
	@Override
	protected void playerLeave(Player player) {
		player.getInventory().clear();
		long empty = teams.stream().filter(Set::isEmpty).count();
		//All but one team is empty
		if (empty == teams.size() - 1) {
			stopGame();
		}
	}

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	protected void startGame() {
		FireBallMap map = (FireBallMap) gameMap;
		teams = FireBallMap.distributeInTeams(activePlayers, map.getIntOption(FireBallOptions.NUM_TEAMS));
		for (int i = 0; i < teams.size(); i++) {
			for (Player p : teams.get(i)) {
				map.respawn(p, i + 1);
			}
		}
		double respawnTime = map.getDoubleOption(FireBallOptions.RESPAWN_FIREBALL);
		long inTicks = (long) (respawnTime*20);
		fireBallSpawner = new RunnableWrapper(() -> {
			for (int i = 0; i < teams.size(); i++) {
				map.summonFireballs(Arrays.asList(teams.get(i).toArray(Player[]::new).clone()), i + 1);
			}
		}).runTaskTimerAsynchronously(0L, inTicks);
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	protected void stopGame() {
		fireBallSpawner.cancel();
	}

	protected int getTeam(Player puncher) {
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).contains(puncher)) {
				return i;
			}
		}
		return -1;
	}

	//======================================Game======================================//

}
