package de.lovejava.autoinit;

import de.ilovejava.skull.Skulls;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;

import java.sql.ResultSet;

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
		Bukkit.getConsoleSender().sendMessage("§b"+i+" K§pf/e wurde/n geladen");
	}
}
