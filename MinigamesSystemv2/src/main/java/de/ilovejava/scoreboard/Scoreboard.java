package de.ilovejava.scoreboard;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.Timer;
import java.util.TimerTask;

public class Scoreboard {
	public Scoreboard(Player p) {
		String uuid = uuidfetcher.getUUID(p).toString();
		org.bukkit.scoreboard.Scoreboard b = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = b.registerNewObjective("ggg","ccc");
		
		Team money = b.registerNewTeam("Money");
		money.setPrefix("§e");
		money.setSuffix("§e"+Utils.getMoneyAmmount(Utils.User.get(uuid).Arden()));
		money.addEntry(ChatColor.BLACK.toString());
		
		Team KD = b.registerNewTeam("KD");
		KD.setPrefix("§e");
		KD.setSuffix("§e"+Utils.getUser().get(uuid).KD());
		KD.addEntry(ChatColor.BOLD.toString());
		
		obj.setDisplayName("§6GodGames");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.getScore("§bServer").setScore(16);
		obj.getScore("§eGodGames").setScore(15);
		obj.getScore(" ").setScore(14);
		obj.getScore("§bArden").setScore(13);
		obj.getScore(ChatColor.BLACK.toString()).setScore(12);
		obj.getScore("  ").setScore(11);
		obj.getScore("§bK/D").setScore(10);
		obj.getScore(ChatColor.BOLD.toString()).setScore(9);
		obj.getScore("   ").setScore(8);
		obj.getScore("§bTeamspeak").setScore(7);
		obj.getScore("§eArdnarun.com").setScore(6);
		p.setScoreboard(b);
	}
	
	public static void update() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					String uuid = uuidfetcher.getUUID(p).toString();
					if(Utils.getTypes().get(p).equals(ChatType.LOBBY)) {
						p.getScoreboard().getTeam("Money").setSuffix("§e"+Utils.getMoneyAmmount(Utils.User.get(uuid).Arden()));
						p.getScoreboard().getTeam("KD").setSuffix("§e"+Utils.getUser().get(uuid).KD());
					}
				}
			}
		}, 0, 1000*30);
	}
}
