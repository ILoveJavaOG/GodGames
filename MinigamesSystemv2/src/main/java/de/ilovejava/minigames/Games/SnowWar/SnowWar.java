package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.MapTools.GameMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Class to represent the FastFight game
 */
public class SnowWar extends Game {

	//======================================Game======================================//

	//Name of the game
	private static final String name = "FastFight";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<SnowWar> factory = new GameFactory<SnowWar>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2SnowWar";
		}

		@NotNull
		@Override
		public SnowWar createGame(GameMap map) {
			SnowWar newGame = new SnowWar();
			newGame.loadMap(new SnowWarMap(map));
			newGame.setup();
			return newGame;
		}
	};

	//======================================Game======================================//

	@Getter
	private final StreakManager streaks = new StreakManager(gameMap);

	//===================================Game Stats===================================//

	//Stats for the game
	protected AtomicInteger[] numKills;

	//===================================Game Stats===================================//

	//===================================Game Phases==================================//

	/**
	 * Constructor to create a game
	 */
	public SnowWar() {
		super(name);
		registerEvents(new SnowWarEvents(this));
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		//Builds arrays to store possible spawn locations
		int numTeams = gameMap.getIntOption(GameOptions.NUM_TEAMS);
		numKills = new AtomicInteger[numTeams];
		SnowWarMap map = (SnowWarMap) gameMap;
		map.setup();
		map.toggleWall(true);
	}

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		SnowWarMap map = (SnowWarMap) gameMap;
		map.spawnPlayers(activePlayers, streaks.playerData);
		map.initSnowman();
		//Initialize fighting phase
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
			activePlayers.forEach(active -> active.sendMessage("Spiel Beginn!"));
			//Remove wall
			map.toggleWall(false);
			map.initPresents();
		}, 20L*gameMap.getIntOption(GameOptions.TIME_BUILDING));
		int maxTime = gameMap.getIntOption(GameOptions.MAX_GAMETIME);
		//Max time for the game
		if (maxTime > 0) Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), this::stopGame, 20L*maxTime);
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		for (int team = 0; team < numKills.length; team++) {
			if (numKills[team].get() == gameMap.getIntOption(GameOptions.MAX_KILLS)) {
				for (Player player : activePlayers) {
					player.sendMessage("Spiel Ende!");
					player.sendMessage("Team: " + (team + 1) + " won");
				}
			}
		}
		((SnowWarMap) gameMap).stop();
		activePlayers.forEach(active -> active.sendMessage("Spiel Ende!"));
		//Remove placed blocks
		SnowWarEvents events = (SnowWarEvents) eventHandler;
		events.clear();

		//Call default game over method
		gameOver();
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

	//==================================Communication================================//

	/**
	 * Method which is to be called when a player gets hit by a snowball
	 *
	 * @param hitPlayer(Player): Player who got hit
	 */
	public void playerHit(Player hitPlayer, Player damager) {
		//Check if a player will die by this
		if (hitPlayer.getLevel() == 1) {
			int kills = numKills[streaks.playerData.get(damager).getTeam() - 1].incrementAndGet();
			if (kills == gameMap.getIntOption(GameOptions.MAX_KILLS)) {
				stopGame();
			} else {
				playerDeath(hitPlayer);
			}
		//Decrease level ("Hit count")
		} else {
			hitPlayer.setLevel(hitPlayer.getLevel() - 1);
		}
	}

	/**
	 * Method which is to be called when a player is being killed according to the game
	 *
	 * @param deadPlayer(Player): Player who died
	 */
	private void playerDeath(Player deadPlayer) {
		//Reset streak
		streaks.resetStreak(deadPlayer);
		SnowWarMap map = (SnowWarMap) gameMap;
		map.dropLoot(deadPlayer.getLocation());
		map.respawn(deadPlayer, streaks.playerData, activePlayers);
	}

	/**
	 * Method to be called when a player gets killed by a shovel
	 *
	 * @param deadPlayer(Player): The player who got shoveled
	 */
	protected void instantKill(Player deadPlayer, Player killer) {
		int kills = numKills[streaks.playerData.get(killer).getTeam() - 1].incrementAndGet();
		if (kills == gameMap.getIntOption(GameOptions.MAX_KILLS)) {
			stopGame();
		} else {
			Location deathSpot = deadPlayer.getLocation();
			//Player death and double loot
			playerDeath(deadPlayer);
			((SnowWarMap) gameMap).dropLoot(deathSpot);
		}
	}

	//==================================Communication================================//

}
