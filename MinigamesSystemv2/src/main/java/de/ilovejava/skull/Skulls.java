package de.ilovejava.skull;

import java.sql.ResultSet;
import java.util.HashMap;

import de.ilovejava.utils.Utils;

public class Skulls {
	private HashMap<String, Boolean> skulls;
	private String uuid;
	private ResultSet rs;
	private String[] name = {"ILoveJava","Hyzenthay_Rah","KingMontegro","Noronei","_Minemacs_","martoffel_2","ZiffernZocker","Icetea3125","Serratt","Stacy148","Ch3ckerJan","DieMingos"};
	
	public Skulls(String uuid) {
		this.uuid = uuid;
		this.skulls = new HashMap<>();
		check();
	}
	
	private void check() {
		if(isExists()) {
			this.load();
		}else {
			this.create();
		}
	}
	public boolean isExists() {
		this.rs = Utils.getMysql().query("SELECT * FROM Skull WHERE UUID='"+this.uuid+"'");
		try {
			if(rs.next()) {
				return true;
			}
			rs.close();
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public void create() {
		Utils.getMysql().update("INSERT INTO Skull(UUID,ILoveJava,Hyzenthay_Rah,KingMontegro,Noronei,_Minemacs_,martoffel_2,ZiffernZocker,Icetea3125,Serratt,Stacy148,Ch3ckerJan,DieMingos) "
						+ "VALUES('"+this.uuid+"','0','0','0','0','0','0','0','0','0','0','0','0')");
		this.load();
	}
	
	public void load() {
		this.rs = Utils.getMysql().query("SELECT * FROM Skull WHERE UUID='"+this.uuid+"'");
		try {
			if(rs.next()) {
				for(String key : name) {
					skulls.put(key, rs.getBoolean(key));
				}
			}
			rs.close();
		}catch(Exception e) {e.printStackTrace();}
		Utils.getUser().get(this.uuid).setSkulls(this);
	}
	
	public void updateSkull(String Key,boolean b) {
		Utils.getMysql().update("UPDATE Skull SET "+Key+"='"+Boolean.compare(b, false)+"' WHERE UUID='"+this.uuid+"'");
	}
	
	public boolean isSkull(String Name) {
		if(skulls.containsKey(Name)) {
			if(skulls.get(Name)) {
				return true;
			}else {return false;}
		}else {return false;}
	}
	
	public HashMap<String, Boolean> getSkulls(){
		return this.skulls;
	}
}
