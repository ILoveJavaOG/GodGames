package de.lovejava.autoinit;

import java.sql.ResultSet;

import org.bukkit.Bukkit;

import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;

public class LoadUser {
	public LoadUser() {
		int i = 0;
		ResultSet rs = Utils.getMysql().query("SELECT * FROM User");
		try {
			while(rs.next()) {
				User s = new User(rs.getString("UUID"));
				s.load();
				i++;
			}
			rs.close();
		}catch(Exception e) {e.printStackTrace();}
		Bukkit.getConsoleSender().sendMessage("§b" + i + " User wurde/n geladen!");
	}
}
