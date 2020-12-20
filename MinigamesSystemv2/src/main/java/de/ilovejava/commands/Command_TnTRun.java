package de.ilovejava.commands;

import de.ilovejava.tntrun.TNT;
import de.ilovejava.tntrun.TNTRunJoinEvent;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_TnTRun extends AbstartcCommands{

	public Command_TnTRun(String commandName, Plugin pl) {
		super(commandName, pl);
	}

	@Override
	public boolean command(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.hasPermission("MGS.Admin")) {
				p.sendMessage(Utils.getPrefix() + "§cAchtung, du darfst das nicht!"); 
				return true;
			}
			if (args.length == 0) {
				p.sendMessage("§b/tntrun create [Name]");
				p.sendMessage("§b/tntrun setLobby [Name]");
				p.sendMessage("§b/tntrun setPlay [Name]");
				p.sendMessage("§b/tntrun setSpec [Name]");
				p.sendMessage("§b/tntrun setName [Name]");
				p.sendMessage("§b/tntrun setMin [Name] [Zahl]");
				p.sendMessage("§b/tntrun setMax [Name] [Zahl]");
				p.sendMessage("§b/tntrun save [Name]");
				p.sendMessage("§b/tntrun delete [Name]");
				p.sendMessage("§b/tntrun join [Name]");
			} else if (args.length == 1) {
				p.sendMessage("§b/tntrun create [Name]");
				p.sendMessage("§b/tntrun setLobby [Name]");
				p.sendMessage("§b/tntrun setPlay [Name]");
				p.sendMessage("§b/tntrun setSpec [Name]");
				p.sendMessage("§b/tntrun setName [Name] [MapName]");
				p.sendMessage("§b/tntrun setMin [Name] [Zahl]");
				p.sendMessage("§b/tntrun setMax [Name] [Zahl]");
				p.sendMessage("§b/tntrun save [Name]");
				p.sendMessage("§b/tntrun delete [Name]");
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("create")) {
					String name = args[1];
					TNT tnt = new TNT(name);
					if (!tnt.isExsist() && !Utils.getTntcreate().containsKey(name)) {
						p.sendMessage(Utils.getPrefix() + "§aDu hast eine neue Tnt Arena erstellt! Folge nun den weiteren Schritten! -> /tntrun setLobby [Name]");
						Utils.getTntcreate().put(name, tnt);
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, der Name §e" + name + "§c ist bereit erstellt!");
					}
				} else if (args[0].equalsIgnoreCase("setLobby")) {
					String name = args[1];
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setLobbyLoc(p.getLocation());
						Utils.getTntcreate().get(name).setLobbyWorld(p.getWorld());
						p.sendMessage(Utils.getPrefix() + "§aDer Lobby Spawn für die Arena §e" + name + "§a wurde gesetzt! -> /tntrun setPlay [Name]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("setplay")) {
					String name = args[1];
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setPlayLoc(p.getLocation());
						Utils.getTntcreate().get(name).setPlayWorld(p.getWorld());
						p.sendMessage(Utils.getPrefix() + "§aDu hast den Spiel Spawn für die Arena §e" + name + "§a gesetzt! -> /tntrun setSpec [Name]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("setspec")) {
					String name = args[1];
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setSpecLoc(p.getLocation());
						Utils.getTntcreate().get(name).setSpecWorld(p.getWorld());
						p.sendMessage(Utils.getPrefix() + "§aDu hast den Spec Spawn für die Arena §e" + name + "§a gesetzt! -> /tntrun setName [Name] [MapName]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("save")) {
					String name = args[1];
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).save();
						p.sendMessage(Utils.getPrefix() + "§aDu hast die Arena §e" + name + "§a gespeichert du kannst sie nun Spielen!");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("join")) {
					String name = args[1];
					if (Utils.getTNTReady().containsKey(name)) {
						Bukkit.getPluginManager().callEvent(new TNTRunJoinEvent(p, Utils.getTNTReady().get(name)));
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAktion nicht möglich!");
					}
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setname")) {
					String name = args[1];
					String mapName = args[2];
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setMapName(mapName);
						p.sendMessage(Utils.getPrefix() + "§aDer Name der Arena §e" + name + "§a lautet nun §e" + mapName + "§a -> /tntrun setMin [Name] [Zahl]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("setMin")) {
					String name = args[1];
					Integer min = Integer.valueOf(args[2]);
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setMinPlayer(min);
						p.sendMessage(Utils.getPrefix() + "§aDas Minimum der Arena §e" + name + "§a lautet nun §e" + min + "§a -> /tntrun setMax [Name] [Zahl]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				} else if (args[0].equalsIgnoreCase("setMax")) {
					String name = args[1];
					Integer max = Integer.valueOf(args[2]);
					if (Utils.getTntcreate().containsKey(name)) {
						Utils.getTntcreate().get(name).setMaxPlayer(max);
						p.sendMessage(Utils.getPrefix() + "§aDas Maximum der Arena §e" + name + "§a lautet nun §e" + max + "§a -> /tntrun save [Name]");
					} else {
						p.sendMessage(Utils.getPrefix() + "§cAchtung, die Tnt Arena wurde noch nicht erstellt! -> /tntrun create [Name]");
					}
				}
			} else {
				p.sendMessage("§b/tntrun create [Name]");
				p.sendMessage("§b/tntrun setLobby [Name]");
				p.sendMessage("§b/tntrun setPlay [Name]");
				p.sendMessage("§b/tntrun setSpec [Name]");
				p.sendMessage("§b/tntrun setName [Name]");
				p.sendMessage("§b/tntrun setMin [Name] [Zahl]");
				p.sendMessage("§b/tntrun setMax [Name] [Zahl]");
				p.sendMessage("§b/tntrun save [Name]");
				p.sendMessage("§b/tntrun delete [Name]");
			}
		}
		return true;
	}

}
