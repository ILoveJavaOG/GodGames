package de.ilovejava.skullCard;

import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class SkullCard {
	private Player p;
	
	private ItemStack skull;
	private ItemStack skull1;
	private ItemStack skull2;
	private ItemStack win;
	private boolean canPlay = false;
	public SkullCard(Player p) {
		this.p = p;
		this.win = new ItemStack(Material.WHITE_STAINED_GLASS_PANE,1,(short)13);
		ItemMeta i = this.win.getItemMeta();
		i.setDisplayName(" ");
		this.win.setItemMeta(i);
		
	}
	
	public String getWinItem() {
		String name = "Nichts";
		Random r = new Random();
		int tit = range(r.nextInt(1000));
		
		switch(tit) {
			case 1:
				name = "DieMingos";
				break;
			case 2:
				name = "Ch3ckerJan";
				break;
			case 3:
				name = "Stacy148";
				break;
			case 4:
				name = "Serratt";
				break;
			case 5:
				name = "Icetea3125";
				break;
			case 6:
				name = "VicoKasa";
				break;
			case 7:
				name = "martoffel_2";
				break;
			case 8:
				name = "_Minemacs_";
				break;
			case 9:
				name = "Noronei";
				break;
			case 10:
				name = "KingMontegro";
				break;
			case 11:
				name = "Hyzenthay_Rah";
				break;
			case 12:
				name = "ILoveJava";
				break;
		}
		return name;
	}
	
	public boolean canPlay() {
		return this.canPlay;
	}
	
	public void setCanPlay(boolean b) {
		this.canPlay = b;
	}
	
	public Integer range(int range) {
		if(range > 0 && range < 799)return 1;
		if(range > 800 && range < 819)return 2;
		if(range > 820 && range < 829)return 3;
		if(range > 830 && range < 839)return 4;
		if(range > 840 && range < 849)return 5;
		if(range > 850 && range < 859)return 6;
		if(range > 860 && range < 869)return 7;
		if(range > 870 && range < 879)return 8;
		if(range > 880 && range < 996)return 9;
		if(range == 997) return 10;
		if(range == 998) return 11;
		if(range == 999) return 12;
		return 10;
	}
	
	public void loadWins() {
		skull = Utils.getSkullbase().getSkull(getWinItem());
		skull1 = Utils.getSkullbase().getSkull(getWinItem());
		skull2 = Utils.getSkullbase().getSkull(getWinItem());
		
		Utils.getSkullcards().put(this.p, this);
		System.out.println("Gewinn wurde geladen!");
	}
	
	public void showWin(Inventory inv, Integer slot,String uuid) {
		inv.clear();
		String name = null;
		if(slot == 12) {
			inv.setItem(3, this.win);
			inv.setItem(21, this.win);
			name = skull.getItemMeta().getDisplayName().replace("§c", "");
		}else if(slot == 13) {
			inv.setItem(4, this.win);
			inv.setItem(22, this.win);
			name = skull1.getItemMeta().getDisplayName().replace("§c", "");
		}else if(slot == 14) {
			inv.setItem(5, this.win);
			inv.setItem(23, this.win);
			name = skull2.getItemMeta().getDisplayName().replace("§c", "");
		}
		
		inv.setItem(12, skull);
		inv.setItem(13, skull1);
		inv.setItem(14, skull2);
		this.p.updateInventory();
		
		User s = Utils.getUser().get(uuid);
		if(!s.skulls().getSkulls().get(name)) {
			s.skulls().updateSkull(name, true);
			s.skulls().getSkulls().put(name, true);
		}else {
			p.sendMessage(Utils.getPrefix() + "§bDa du diesen Kopf leider schon besitzt bekommst du §e20§b Arden zur§ck!");
			Utils.getEco().depositPlayer(p, 20.0);
		}
	}
}
