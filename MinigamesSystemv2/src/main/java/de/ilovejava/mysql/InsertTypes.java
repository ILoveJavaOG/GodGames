package de.ilovejava.mysql;

import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;

public class InsertTypes {
	public InsertTypes() {
		if(!Utils.getMySQlHost().equals("Host")) {
			Utils.setMysql(new API_MySQL(Utils.getMySQlHost(), Utils.getMySQLDatabase(), Utils.getMySQLUser(), Utils.getMySQLPassword(), Utils.getMySQlPort()));
			Utils.getMysql().update("CREATE TABLE IF NOT EXISTS User(id MEDIUMINT NOT NULL AUTO_INCREMENT,PRIMARY KEY (id), UUID varchar(123), Kills int, Deahts int, PlayTime long, KD double, money double)");
			Utils.getMysql().update("CREATE TABLE IF NOT EXISTS Skull(id MEDIUMINT NOT NULL AUTO_INCREMENT,PRIMARY KEY (id),UUID varchar(123), ILoveJava boolean, Hyzenthay_Rah boolean, KingMontegro boolean, Noronei boolean, _Minemacs_ boolean,"
								  + "martoffel_2 boolean,VicoKasa boolean,Icetea3125 boolean,Serratt boolean,Stacy148 boolean,Ch3ckerJan boolean,DieMingos boolean)");
		}else {Bukkit.getConsoleSender().sendMessage("§cDie MySQL Daten m§ssen ge§ndert werden!"); Bukkit.getPluginManager().disablePlugin(Utils.getInstance());}
	}
}
