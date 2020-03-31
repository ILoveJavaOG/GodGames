package de.ilovejava.interfaces;

import de.ilovejava.skull.Skulls;

public interface User {
	public String UUid();
	public String Name();
	public Integer Kills();
	public Integer Deahts();
	public Long PlayTime();
	public Double KD();
	public double Arden();
	public Skulls skulls();
	
	public void load();
	public void update();
	public void save();
	public void delete();
	public void stroeKD();
	public void create();
	
	public void setUUid(String uu);
	public void setName(String name);
	public void setKills(Integer Kills);
	public void setDeaths(Integer Deahts);
	public void setPlaytime(Long playtime);
	public void setKD(Double d);
	public void setArden(Double d);
	public void setSkulls(Skulls skulls);
	
	public double addArden(Double d);
	public boolean removeArden(Double d);
	public void addKills();
	public void addDeaths();
}
