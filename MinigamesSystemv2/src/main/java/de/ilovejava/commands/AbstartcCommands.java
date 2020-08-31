package de.ilovejava.commands;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;

public abstract class AbstartcCommands extends BukkitCommand{

	public AbstartcCommands(String commandName, Plugin pl) {
		super(commandName);
		try {
			getCommandMap().register(pl.getName(), this);
		}catch(IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		return command(sender, args);
	}
	
	public abstract boolean command(CommandSender paramCommandSender, String[] paramArrayOfString);
	
	public CommandMap getCommandMap() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
		commandMap.setAccessible(true);
		return (CommandMap) commandMap.get(Bukkit.getServer());
	}
}
