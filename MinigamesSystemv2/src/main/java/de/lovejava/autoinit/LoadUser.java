package de.lovejava.autoinit;

import java.sql.ResultSet;

import org.bukkit.Bukkit;

import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;

public class LoadUser {

	public LoadUser() {
		int i = 0;
		ResultSet rs = null;
		try {
			rs = Utils.getMysql().query("SELECT * FROM User");
		} catch (NullPointerException e) {
			Bukkit.getConsoleSender().sendMessage("§3Could not load MYSQL");
		}
		try {
			while(rs.next()) {
				User s = new User(rs.getString("UUID"));
				s.load();
				i++;
			}
			rs.close();
		}catch(Exception e) {
			System.out.println("Exception occurred while loading user from database");
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage("§b" + i + " User wurde/n geladen!");
	}
}
