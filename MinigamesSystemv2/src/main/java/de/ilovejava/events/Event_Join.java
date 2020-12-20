package de.ilovejava.events;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.crafttitle.API_CraftTitleApi;
import de.ilovejava.scoreboard.Scoreboard;
import de.ilovejava.skull.Skulls;
import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Event_Join implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		String uuid = uuidfetcher.getUUID(e.getPlayer().getName()).toString();
		
		for(int i = 0; i < 20; i++) {
			e.getPlayer().sendMessage("");
		}
		
		e.getPlayer().sendMessage("§bWillkommen §e" + e.getPlayer().getName() + "§b auf dem GodGames Server von Ardnarun.com");
		e.getPlayer().sendMessage("§bDas Ardnarun.com Server Team wünscht dir viel Spaß!");
		
		API_CraftTitleApi.sendFullTitle(e.getPlayer(), 40, 40, 40, "§bWillkommen", "§6" + e.getPlayer().getName());
		
		checkUser(uuid);
		checkSkulls(uuid);
		Utils.getTypes().put(e.getPlayer(), ChatType.LOBBY);
		
		new Scoreboard(e.getPlayer());
		giveLobbyItems(e.getPlayer());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				e.getPlayer().teleport(Utils.getSpawn());
			}
		}, 4);
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
	}

	public static void giveLobbyItems(Player p) {
		p.getInventory().clear();
		
		ItemStack i = new ItemStack(Material.CLOCK);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§e§o§lSpiel wählen§8(§cRechtklick§8)");
		i.setItemMeta(im);
		
		p.getInventory().setItem(4, i);
		
		i = Utils.getTimeSkull();
		SkullMeta sm = (SkullMeta) i.getItemMeta();
		sm.setDisplayName("§6§o§lSammlerköpfe");
		i.setItemMeta(sm);
		
		p.getInventory().setItem(2, i);
		
		i = new ItemStack(Material.NETHER_STAR);
		im = i.getItemMeta();
		im.setDisplayName("§b§o§lSpieler verstecken");
		i.setItemMeta(im);
		p.getInventory().setItem(6, i);
	}
	
	private void checkUser(String uuid) {
		if(!Utils.getUser().containsKey(uuid)) {
			User s = new User(uuid);
			s.create();
			s.setOnline(true);
			s.update();
		}else {
			User s = Utils.getUser().get(uuid);
			s.setOnline(true);
			s.update();
		}
	}
	
	private void checkSkulls(String uuid) {
		Skulls s = new Skulls(uuid);
		if(!s.isExists()) {s.create();}
	}
}
