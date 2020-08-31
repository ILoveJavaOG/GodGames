package de.ilovejava.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.ilovejava.shop.Shop;
import de.ilovejava.utils.Utils;

public class Command_Spawn extends AbstartcCommands{

	public Command_Spawn(String commandName, Plugin pl) {
		super(commandName, pl);
	}

	@Override
	public boolean command(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if (Utils.getSpawn() != null) {
					p.teleport(Utils.getSpawn());
				} else {
					p.sendMessage(Utils.getPrefix() + "§cAchtung, der Spawn ist zurzeit nicht aktiv!");
				}
			} else if(args.length == 1) {
				FileConfiguration cfg;
				Location loc;
				switch(args[0].toLowerCase()) {
					case "spawn":
						if(p.hasPermission("GG.Spawn")) {
							cfg = Utils.getInstance().getConfig();
							loc = p.getLocation();
							cfg.set("Config.Spawn.X", loc.getX());
							cfg.set("Config.Spawn.Y", loc.getY());
							cfg.set("Config.Spawn.Z", loc.getZ());
							cfg.set("Config.Spawn.Yaw", loc.getYaw());
							cfg.set("Config.Spawn.Pitch", loc.getPitch());
							cfg.set("Config.Spawn.World", loc.getWorld().getName());
							Utils.getInstance().saveConfig();
							Utils.setSpawn(p.getLocation());
							p.sendMessage(Utils.getPrefix() + "§bDer Spawn wurde gesetzt!");
						}
						return true;
					case "1vs1":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.VS.X", loc.getX());
						cfg.set("Config.VS.Y", loc.getY());
						cfg.set("Config.VS.Z", loc.getZ());
						cfg.set("Config.VS.Yaw", loc.getYaw());
						cfg.set("Config.VS.Pitch", loc.getPitch());
						cfg.set("Config.VS.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setVs(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer 1VS1 Spawn wurde gesetzt!");
						return true;
					case "bedwars":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.BW.X", loc.getX());
						cfg.set("Config.BW.Y", loc.getY());
						cfg.set("Config.BW.Z", loc.getZ());
						cfg.set("Config.BW.Yaw", loc.getYaw());
						cfg.set("Config.BW.Pitch", loc.getPitch());
						cfg.set("Config.BW.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setBedWars(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer BedWars Spawn wurde gesetzt!");
						return true;
					case "void":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Void.X", loc.getX());
						cfg.set("Config.Void.Y", loc.getY());
						cfg.set("Config.Void.Z", loc.getZ());
						cfg.set("Config.Void.Yaw", loc.getYaw());
						cfg.set("Config.Void.Pitch", loc.getPitch());
						cfg.set("Config.Void.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setVoid(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer Void Spawn wurde gesetzt!");
						return true;
					case "ice":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Ice.X", loc.getX());
						cfg.set("Config.Ice.Y", loc.getY());
						cfg.set("Config.Ice.Z", loc.getZ());
						cfg.set("Config.Ice.Yaw", loc.getYaw());
						cfg.set("Config.Ice.Pitch", loc.getPitch());
						cfg.set("Config.Ice.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setIce(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer Ice Spawn wurde gesetzt!");
						return true;
					case "fisch":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Fisch.X", loc.getX());
						cfg.set("Config.Fisch.Y", loc.getY());
						cfg.set("Config.Fisch.Z", loc.getZ());
						cfg.set("Config.Fisch.Yaw", loc.getYaw());
						cfg.set("Config.Fisch.Pitch", loc.getPitch());
						cfg.set("Config.Fisch.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setFisch(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer Fisch Spawn wurde gesetzt!");
						return true;
					case "schleim":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Schleim.X", loc.getX());
						cfg.set("Config.Schleim.Y", loc.getY());
						cfg.set("Config.Schleim.Z", loc.getZ());
						cfg.set("Config.Schleim.Yaw", loc.getYaw());
						cfg.set("Config.Schleim.Pitch", loc.getPitch());
						cfg.set("Config.Schleim.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setSchleim(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer Schleim Spawn wurde gesetzt!");
						return true;
					case "schatz":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Schatz.X", loc.getX());
						cfg.set("Config.Schatz.Y", loc.getY());
						cfg.set("Config.Schatz.Z", loc.getZ());
						cfg.set("Config.Schatz.Yaw", loc.getYaw());
						cfg.set("Config.Schatz.Pitch", loc.getPitch());
						cfg.set("Config.Schatz.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setSchatz(loc);
						p.sendMessage(Utils.getPrefix() + "§bDer Schatz Spawn wurde gesetzt!");
						return true;
					case "schatzshop":
						if(!p.hasPermission("GG.Spawn")) {return true;}
						cfg = Utils.getInstance().getConfig();
						loc = p.getLocation();
						cfg.set("Config.Shop.X", loc.getX());
						cfg.set("Config.Shop.Y", loc.getY());
						cfg.set("Config.Shop.Z", loc.getZ());
						cfg.set("Config.Shop.Yaw", loc.getYaw());
						cfg.set("Config.Shop.Pitch", loc.getPitch());
						cfg.set("Config.Shop.World", loc.getWorld().getName());
						Utils.getInstance().saveConfig();
						Utils.setShop(loc);
						new Shop();
						p.sendMessage(Utils.getPrefix() + "§bDer Shop Spawn wurde gesetzt!");
						break;
				}
			}
		}
		return true;
	}
	
}
