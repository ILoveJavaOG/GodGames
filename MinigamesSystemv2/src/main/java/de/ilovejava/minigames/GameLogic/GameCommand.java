package de.ilovejava.minigames.GameLogic;

import de.ilovejava.commands.AbstartcCommands;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameSelector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Class to register a command for any game
 */
public class GameCommand extends AbstartcCommands {

	public GameCommand(String game) {
		super(game, Lobby.getPlugin(Lobby.class));
	}

	@Override
	public boolean command(CommandSender sender, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			Bukkit.getConsoleSender().sendMessage("No console commands available");
		} else {
			Player player = (Player) sender;
			if (args.length == 0) {
				//TODO PRINT HELP
				return true;
			}
			boolean isInGame = false;
			int gameId = -1;
			//Determine if a player is playing and get the id if
			if (Tracker.isInGame(player)) {
				gameId = Tracker.getGameId(player);
				isInGame = true;
			}
			if (args.length == 1) {
				//Leave
				if (args[0].equalsIgnoreCase("leave")) {
					//Leave game
					if (isInGame) {
						Game.allGames.get(gameId).leavingPlayer(player);
						Selector.selectors.get(getName()).leaving(gameId);
					//Cannot leave no game
					} else {
						player.sendMessage("Must be in a game");
					}
					return true;
				//Join
				} else if (args[0].equalsIgnoreCase("join")) {
					//Check if the player can join a game
					if (!isInGame) {
						player.openInventory(Selector.selectors.get(getName()).getInventory());
					//Player cannot join if already in a game
					} else {
						player.sendMessage("Cannot join a new game while playing");
					}
				}
			}
		}
		return false;
	}
}
