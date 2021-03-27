package de.ilovejava.minigames.Games.FastFight;

import de.ilovejava.minigames.Abilities.Ability;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.Games.FastFight.Abilities.Dash;
import de.ilovejava.minigames.Games.FastFight.Abilities.Jump;
import de.ilovejava.minigames.Games.FastFight.Abilities.Push;
import de.ilovejava.minigames.Games.SnowWar.GameOptions;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Class to represent the FastFight game
 */
public class FastFight extends Game {

	//======================================Game======================================//

	//Name of the game
	private static final String name = "FastFight";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<FastFight> factory = new GameFactory<FastFight>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2FastFight";
		}

		@NotNull
		@Override
		public FastFight createGame(GameMap map) {
			FastFight newGame = new FastFight();
			newGame.loadMap(new FastFightMap(map));
			newGame.setup();
			return newGame;
		}
	};

	//======================================Game======================================//

	//===================================Game Stats===================================//

	//Stats for the game
	protected AtomicInteger[] numKills;

	//===================================Game Stats===================================//

	//===================================Game Phases==================================//

	/**
	 * Constructor to create a game
	 */
	public FastFight() {
		super(name);
		registerEvents(new FastFightEvents());
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		//Builds arrays to store possible spawn locations
		int numTeams = gameMap.getIntOption(GameOptions.NUM_TEAMS);
		numKills = new AtomicInteger[numTeams];
	}

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		FastFightMap map = (FastFightMap) gameMap;
		map.spawnPlayers(activePlayers);
		for (Player player : activePlayers) {
			Ability.current.put(player, new Ability[]{
					new Dash(player, map.getDoubleOption(FastFightOptions.DASH_COOLDOWN), map.getDoubleOption(FastFightOptions.DASH_SCALE)),
					new Jump(player, map.getDoubleOption(FastFightOptions.JUMP_COOLDOWN), map.getDoubleOption(FastFightOptions.JUMP_SCALE)),
					new Push(player, map.getDoubleOption(FastFightOptions.PUSH_COOLDOWN), map.getDoubleOption(FastFightOptions.PUSH_SCALE))
			});
			player.getInventory().setHeldItemSlot(3);
			player.setWalkSpeed(0.5f);
		}
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {

	}

	//===================================Game Phases==================================//

	//===================================Game Events==================================//
	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	@Override
	public void playerJoin(Player player) {
		player.getInventory().clear();
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
		player.getInventory().clear();
		//Stop the game if there arent any more players
		if (activePlayers.size() <= 1) {
			stopGame();
		}
	}

	//===================================Game Events==================================//

}
