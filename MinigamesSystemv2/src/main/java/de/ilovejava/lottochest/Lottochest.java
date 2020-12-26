package de.ilovejava.lottochest;

import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class Lottochest {
	boolean isgived = false;
	int closeInt = 0;
	
	int open = 0;
	ArrayList<Integer> prices = new ArrayList<Integer>(); 
	
	Player p;
	
	Inventory inv;
	
	public Lottochest(Player p) {
		this.p = p;
		this.inv =Bukkit.createInventory(null, 54, "§5LottoChest");
		this.loadPrices();
	}
	
	private void loadPrices() {
		for(int i = 0; i < 6; i++) {
			Random rnd = new Random();
			int cat = rnd.nextInt(101);
			if(cat > 1 && cat < 9) {
				prices.add(rnd.nextInt(50));
			}else if(cat > 10 && cat < 12) {
				prices.add(rnd.nextInt(100));
			}else if(cat > 13 && cat < 16) {
				prices.add(rnd.nextInt(200));
			}else if(cat > 16 && cat < 40) {
				prices.add(rnd.nextInt(70));
			}else if(cat > 40 && cat < 80) {
				prices.add(rnd.nextInt(90));
			}else if(cat > 81 && cat < 98) {
				prices.add(rnd.nextInt(300));
			}else if(cat == 99) {
				prices.add(rnd.nextInt(5000));
			}else if(cat == 100) {
				prices.add(rnd.nextInt(50000));
			}
		}
		
		this.closeInt = prices.get(0) + prices.get(1)+prices.get(2)+prices.get(3)+prices.get(4);
		
		setUpInventory();
	}
	
	public int getSadPrice() {
		return this.closeInt;
	}
	
	public void setUpInventory() {
		ItemStack it = new ItemStack(Material.CHEST);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§e???????");
		it.setItemMeta(im);
		for(int i = 0; i < 54; i++) {
			this.inv.setItem(i, it);
		}
		Utils.getWaitLottochest().add(p);
		this.openInventory();
	}
	
	public boolean isChestEnd() {
		if(open == 5) {
			return true;
		}
		return false;
	}
	
	public int getPrice() {
		int p = this.prices.get(open);
		open++;
		if(open == 5) {
			System.out.println("Starte Timer!");
			this.startEndTimer();
		}
		return p;
	}
	
	private void openInventory() {
		this.p.closeInventory();
		this.p.openInventory(inv);
		Utils.getLottoCards().put(p, this);
	}
	
	@SuppressWarnings("deprecation")
	private void startEndTimer() {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Utils.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				setItems();
			}
		},30);
	}
	
	private void setItems() {
		
		this.inv.clear();
		
		ItemStack i = new ItemStack(Material.EMERALD);
		ItemMeta im = i.getItemMeta();
		
		ItemStack normal = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta inormal = normal.getItemMeta();
		inormal.setDisplayName("§aNormal");
		normal.setItemMeta(inormal);
		
		ItemStack wertvoll = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta iwert = wertvoll.getItemMeta();
		iwert.setDisplayName("§cWertvoll");
		wertvoll.setItemMeta(iwert);
		
		if(prices.get(0) > 1000) {
			inv.setItem(11, wertvoll);
			inv.setItem(29, wertvoll);
		}else {
			inv.setItem(11, normal);
			inv.setItem(29, normal);
		}
		
		im.setDisplayName("§e"+prices.get(0));
		i.setItemMeta(im);
		inv.setItem(20, i);
		
		if(prices.get(1) >= 1000) {
			inv.setItem(12, wertvoll);
			inv.setItem(30, wertvoll);
		}else {
			inv.setItem(12, normal);
			inv.setItem(30, normal);
		}
		
		im.setDisplayName("§e"+prices.get(1));
		i.setItemMeta(im);
		inv.setItem(21, i);
		
		if(prices.get(2) >= 1000) {
			inv.setItem(13, wertvoll);
			inv.setItem(31, wertvoll);
		}else {
			inv.setItem(13, normal);
			inv.setItem(31, normal);
		}
		
		im.setDisplayName("§e"+prices.get(2));
		i.setItemMeta(im);
		inv.setItem(22, i);
		
		if(prices.get(1) >= 1000) {
			inv.setItem(14, wertvoll);
			inv.setItem(32, wertvoll);
		}else {
			inv.setItem(14, normal);
			inv.setItem(32, normal);
		}
		
		im.setDisplayName("§e"+prices.get(3));
		i.setItemMeta(im);
		inv.setItem(23, i);
		
		if(prices.get(1) >= 1000) {
			inv.setItem(15, wertvoll);
			inv.setItem(33, wertvoll);
		}else {
			inv.setItem(15, normal);
			inv.setItem(33, normal);
		}
		
		im.setDisplayName("§e"+prices.get(4));
		i.setItemMeta(im);
		inv.setItem(24, i);
		
		ItemStack all = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		ItemMeta iall = all.getItemMeta();
		int al = prices.get(0) + prices.get(1)+prices.get(2)+prices.get(3)+prices.get(4);
		iall.setDisplayName("§6" + al);
		all.setItemMeta(iall);
		inv.setItem(4, all);
		
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		p.updateInventory();
		
		if(Utils.getWaitLottochest().contains(p)) {
			this.closeInventoryTimer(al);
		}
	}
	
	public boolean isGive() {
		return this.isgived;
	}
	
	@SuppressWarnings("deprecation")
	private void closeInventoryTimer(int price) {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Utils.getInstance(), new Runnable() {
			@Override
			public void run() {
				p.closeInventory();
				Utils.getEco().depositPlayer(p, price);
				isgived = true;
			}
		}, 100);
	}
}
