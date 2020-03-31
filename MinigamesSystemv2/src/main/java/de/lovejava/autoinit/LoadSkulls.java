package de.lovejava.autoinit;

import java.sql.ResultSet;

import org.bukkit.Bukkit;

import de.ilovejava.skull.Skulls;
import de.ilovejava.utils.Utils;

public class LoadSkulls {
	public LoadSkulls() {
		int i = 0;
		ResultSet rs = Utils.getMysql().query("SELECT * FROM Skull");
		try {
			while(rs.next()) {
				Skulls s = new Skulls(rs.getString("UUID"));
				s.load();
				i++;
			}
			rs.close();
		}catch(Exception e) {e.printStackTrace();}
		Bukkit.getConsoleSender().sendMessage("§b"+i+" Köpf/e wurde/n geladen");
	}
}
