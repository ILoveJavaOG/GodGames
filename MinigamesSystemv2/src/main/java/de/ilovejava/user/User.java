package de.ilovejava.user;

import de.ilovejava.skull.Skulls;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;

import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class User implements de.ilovejava.interfaces.User{

	private String UUid;
	private String Name;
	private Integer Kills;
	private Integer Deahts;
	private Double KD;
	private Double Arden;
	private Long PlayTime;
	private Timer t;
	private ResultSet rs;
	private boolean isOnline = false;
	private Skulls skulls;
	private Double removeEnableMoney = 0.0;
	public User(String uu) {this.setUUid(uu);}
	
	@Override
	public String UUid() {
		return this.UUid;
	}

	@Override
	public String Name() {
		return this.Name;
	}

	@Override
	public Integer Kills() {
		return this.Kills;
	}

	@Override
	public Integer Deahts() {
		return this.Deahts;
	}

	@Override
	public Long PlayTime() {
		return this.PlayTime;
	}

	@Override
	public void create() {
		Utils.getMysql().update("INSERT INTO User(UUID,Kills,Deahts,PlayTime,KD,money) VALUES('"+this.UUid+"','0','0','0','0.0','100.0')");
		this.load();
	}
	
	@Override
	public void load() {
		this.rs = Utils.getMysql().query("SELECT * FROM User WHERE UUID='"+this.UUid()+"'");
		try {
			if(rs.next()) {
				this.setName(uuidfetcher.getName(UUID.fromString(this.UUid)));
				this.setKills(rs.getInt("Kills"));
				this.setDeaths(rs.getInt("Deahts"));
				this.setPlaytime(rs.getLong("PlayTime"));
				this.setKD(rs.getDouble("KD"));
				this.setArden(rs.getDouble("money"));
				Utils.getUser().put(this.UUid, this);
				rs.close();
			}
		}catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public void update() {
		this.t = new Timer();
		this.t.schedule(new TimerTask() {
			@Override
			public void run() {
				if(isOnline) {
					setPlaytime(PlayTime()+1);
				}else {this.cancel();}
			}
		}, 0, 1000);
	}

	@Override
	public void save() {
		Utils.getMysql().update("UPDATE User SET Kills='"+this.Kills()+"',Deahts='"+this.Deahts()+"',PlayTime='"+this.PlayTime()+"',KD='"+this.KD()+"',money='"+this.Arden()+"' WHERE UUID='"+this.UUid()+"'");
	}

	@Override
	public void delete() {
		Utils.getMysql().update("DELETE FROM USER WHERE UUID='"+this.UUid()+"'");
	}

	@Override
	public void setUUid(String uu) {
		this.UUid = uu;
	}

	@Override
	public void setName(String name) {
		this.Name = name;
	}

	@Override
	public void setKills(Integer Kills) {
		this.Kills = Kills;
	}

	@Override
	public void setDeaths(Integer Deahts) {
		this.Deahts = Deahts;
	}

	@Override
	public void setPlaytime(Long playtime) {
		this.PlayTime = playtime;
	}

	@Override
	public void addKills() {
		this.setKills(this.Kills()+1);
	}

	@Override
	public void addDeaths() {
		this.setDeaths(this.Deahts()+1);
	}

	@Override
	public Double KD() {
		return this.KD;
	}

	@Override
	public void stroeKD() {
		double kd = this.Kills/this.Deahts;
		this.setKD(kd);
	}

	@Override
	public void setKD(Double d) {
		this.KD = d;
	}

	public void setOnline(boolean b) {
		this.isOnline = b;
	}

	@Override
	public double Arden() {
		return this.Arden;
	}

	@Override
	public void setArden(Double d) {
		this.Arden = d;
	}

	@Override
	public double addArden(Double d) {
		this.setArden(this.Arden()+d);
		return this.Arden();
	}

	@Override
	public boolean removeArden(Double d) {
		this.setArden(this.Arden()-d);
		return true;
	}

	public double toRemoveArden() {
		return this.removeEnableMoney;
	}
	
	public void setToRemoveArden(Double d) {
		this.removeEnableMoney = d;
	}
	
	@Override
	public Skulls skulls() {
		return this.skulls;
	}

	@Override
	public void setSkulls(Skulls skulls) {
		this.skulls = skulls;
	}
	
}
