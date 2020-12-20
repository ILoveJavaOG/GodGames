package de.ilovejava.autoinit;

import de.ilovejava.tntrun.TNT;
import org.bukkit.Bukkit;

import java.io.File;

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
