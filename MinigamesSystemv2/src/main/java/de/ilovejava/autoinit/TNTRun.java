package de.ilovejava.autoinit;

import java.io.File;

import org.bukkit.Bukkit;

import de.ilovejava.tntrun.TNT;

public class TNTRun {
	public TNTRun() {
		File f = new File("plugins/Minigames/TNTRun");
		if(f.exists()) {
			int i = 0;
			for(File t : f.listFiles()) {
				String name = t.getName().replace(".tntrun", "");
				TNT tnt = new TNT(name);
				tnt.load();
				i ++;
			}
			Bukkit.getConsoleSender().sendMessage("§bTNTRun Arenen geladen: §e" + i);
		}
	}
}
