package de.ilovejava.tdg;

import de.ilovejava.tdg.EntityHider.Policy;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TDG {
	
	private List<Player> view = new ArrayList<Player>();
	private List<Player> toHide = new ArrayList<Player>();
	private List<ArmorStand> icons = new ArrayList<ArmorStand>();
	String euler = "-1.1 1.5 1.4";
	
	private EntityHider entityHider = new EntityHider(Utils.getInstance(), Policy.BLACKLIST);
	private Utils utils = new Utils();
	
	public void addIcon(Player p, Location loc, String name, ItemStack item, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 0.0, 0.0);
        }
        if(positionY == 3) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = p.getWorld().spawn(loc, ArmorStand.class);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a.setHelmet(item);
		a.setArms(true);
		icons.add(a);
		view.add(p);
		Utils.getInstance().entities.add(a);
		Metadata.set(a, p.getName(), p);
		Location locb = a.getLocation();
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a.setFireTicks(0);
	        		toHide.clear();
	        		for (Player all : Bukkit.getOnlinePlayers()) {
	        			toHide.add(all);
	        			toHide.remove(p);
	        		}
	        		for (Player hide : toHide) {
	        		    entityHider.hideEntity(hide, a);
	        		}
	                if (Targeter.getTargetEntity(p) == a) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                } else {
	                    a.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Utils.getInstance().entities.remove(a);
						a.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Utils.getInstance().entities.remove(a);
	        			a.remove();
	        			Utils.getInstance().pmenu.put(p, null);
	        		}
	        	}
	        }
	    }.runTaskTimer(Utils.getInstance(), 0, 0);
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public void addIcon(Player p, Location loc, String name, String texture, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 0.0, 0.0);
        }
        if(positionY == 3) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = p.getWorld().spawn(loc, ArmorStand.class);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a.setCollidable(false);
		if (texture.contains("textures.minecraft.net")) {
			a.setHelmet(Skull.getSkull(texture));
		}
		else {
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(texture.replace("%player%", p.getName()));
			skull.setItemMeta(meta);
			a.setHelmet(skull);
		}
		a.setArms(true);
		icons.add(a);
		view.add(p);
		Utils.getInstance().entities.add(a);
		Metadata.set(a, p.getName(), p);
		Location locb = a.getLocation();
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a.setFireTicks(0);
	        		toHide.clear();
	        		for (Player all : Bukkit.getOnlinePlayers()) {
	        			toHide.add(all);
	        			toHide.remove(p);
	        		}
	        		for (Player hide : toHide) {
	        		    entityHider.hideEntity(hide, a);
	        		}
	                if (Targeter.getTargetEntity(p) == a) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                } else {
	                    a.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Utils.getInstance().entities.remove(a);
						a.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Utils.getInstance().entities.remove(a);
	        			a.remove();
	        			Utils.getInstance().pmenu.put(p, null);
	        		}
	        	}
	        }
	    }.runTaskTimer(Utils.getInstance(), 0, 0);
	}
	
	@SuppressWarnings("unused")
	public void addIcon(Player p, Location loc, String name, ItemStack item, boolean hand, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 0.0, 0.0);
        }
        if(positionY == 3) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = p.getWorld().spawn(loc, ArmorStand.class);
		Vector v = a.getLocation().getDirection();
		Vector v2 = v.clone().setX(v.getZ()).setZ(-v.getX());
		Location locs = a.getLocation();
		locs.setDirection(v2);
		ArmorStand a2 = p.getWorld().spawn(locs, ArmorStand.class);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a2.setVisible(false);
		a2.setCustomName(name.replace("&", "§"));
		a2.setRightArmPose(new EulerAngle(4.7, 4.8, 6.3));
		a2.setItemInHand(item);
		a.setArms(true);
		a2.setArms(true);
		a.setCollidable(false);
		a2.setCollidable(false);
		icons.add(a);
		icons.add(a2);
		view.add(p);
		Utils.getInstance().entities.add(a);
		Utils.getInstance().entities.add(a2);
		Metadata.set(a, p.getName(), p);
		Metadata.set(a2, p.getName(), p);
		Location locb = a.getLocation();
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a2.teleport(locs);
	        		a.setFireTicks(0);
	        		a2.setFireTicks(0);
	        		toHide.clear();
	        		for (Player all : Bukkit.getOnlinePlayers()) {
	        			toHide.add(all);
	        			toHide.remove(p);
	        		}
	        		for (Player hide : toHide) {
	        		    entityHider.hideEntity(hide, a);
	        		    entityHider.hideEntity(hide, a2);
	        		}
	                if (Targeter.getTargetEntity(p) == a || (Targeter.getTargetEntity(p) == a2)) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                	a2.setGravity(true);
		                a2.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		        		a2.teleport(locs);
	                } else {
	                    a.setGravity(false);
	                    a2.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Utils.getInstance().entities.remove(a);
						Utils.getInstance().entities.remove(a2);
						a.remove();
						a2.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Utils.getInstance().entities.remove(a);
	        			Utils.getInstance().entities.remove(a2);
	        			a.remove();
	        			a2.remove();
	        			Utils.getInstance().pmenu.put(p, null);
	        		}
	        	}
	        }
	    }.runTaskTimer(Utils.getInstance(), 0, 0);
	}
	
	@SuppressWarnings("unused")
	public void addIcon(Player p, Location loc, String name, ItemStack item, boolean hand, boolean tool, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 0.0, 0.0);
        }
        if(positionY == 3) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = p.getWorld().spawn(loc, ArmorStand.class);
		@SuppressWarnings("static-access")
		Location locs = utils.getLeftSide(a.getLocation().add(0.0, -0.3, 0.0), 0.4);
		ArmorStand a2 = p.getWorld().spawn(locs, ArmorStand.class);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a2.setVisible(false);
		a2.setCustomName(name.replace("&", "§"));
		a2.setRightArmPose(new EulerAngle(-1.1, 1.7, 1.4));
		a2.setItemInHand(item);
		a.setArms(true);
		a2.setArms(true);
		a.setCollidable(false);
		a2.setCollidable(false);
		icons.add(a);
		icons.add(a2);
		view.add(p);
		Utils.getInstance().entities.add(a);
		Utils.getInstance().entities.add(a2);
		Metadata.set(a, p.getName(), p);
		Metadata.set(a2, p.getName(), p);
		Location locb = a.getLocation();
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a2.teleport(locs);
	        		a.setFireTicks(0);
	        		a2.setFireTicks(0);
	        		toHide.clear();
	        		for (Player all : Bukkit.getOnlinePlayers()) {
	        			toHide.add(all);
	        			toHide.remove(p);
	        		}
	        		for (Player hide : toHide) {
	        		    entityHider.hideEntity(hide, a);
	        		    entityHider.hideEntity(hide, a2);
	        		}
	                if (Targeter.getTargetEntity(p) == a || (Targeter.getTargetEntity(p) == a2)) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                	a2.setGravity(true);
		                a2.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		        		a2.teleport(locs);
	                } else {
	                    a.setGravity(false);
	                    a2.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Utils.getInstance().entities.remove(a);
						Utils.getInstance().entities.remove(a2);
						a.remove();
						a2.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Utils.getInstance().entities.remove(a);
	        			Utils.getInstance().entities.remove(a2);
	        			a.remove();
	        			a2.remove();
	        			Utils.getInstance().pmenu.put(p, null);
	        		}
	        	}
	        }
	    }.runTaskTimer(Utils.getInstance(), 0, 0);
	}
	
	public boolean hasMenuOpened(Player player) {
		if (view.contains(player)) {
			return true;
		}
		return false;
	}
	
	public void closeMenu(Player player) {
		for (Entity en : player.getWorld().getEntities()) {
			if (Metadata.hasKey(en, player.getName())) {
				en.remove();
			}
		}
		/*
		 * 
		 * String action = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".click-action.action"); // <- close-action
		String value = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".click-action.value"); // <- close-action
		IconAction ac = new IconAction(ActionType.valueOf(action), player, value);
		if (action.equals("COMMAND")) {
			String executefrom = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-action.executefrom");
			ac.setExecutor(executefrom);
		}
		if (action.equals("PARTICLES")) {
			int amount = Main.getInstance().getMenuConfig().getInt(Main.getInstance().pmenu.get(player) + ".close-action.amount");
			float speed = Main.getInstance().getMenuConfig().getLong(Main.getInstance().pmenu.get(player) + ".close-action.speed");
			ac.setParticlesData(Main.getInstance().lastLoc.get(player).add(0, 2.5, 0), amount, speed);
		}
		ac.runAction();
		 * */
		Utils.getInstance().pmenu.put(player, null);
		view.remove(player);
	}
}
