package de.ilovejava.minigames.Games.SnowWar;

import com.google.common.collect.Sets;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Events;
import de.ilovejava.minigames.Games.SnowWar.Items.SnowWarItems;
import de.ilovejava.minigames.Items.GameItem;
import de.ilovejava.minigames.Items.ItemBox;
import de.ilovejava.minigames.MapTools.ProbabilityMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to handle events during the fish fight
 */
public class SnowWarEvents implements Events {

	private final SnowWar snowWar;

	public SnowWarEvents(SnowWar snowWar) {
		this.snowWar = snowWar;
	}

	private final ConcurrentHashMap<Projectile, Integer> particleTrails = new ConcurrentHashMap<>();

	private final Set<Player> rapidFire = Sets.newConcurrentHashSet();

	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		//This need to be defined so that the projectile launch event will not be triggered
	}

	public void onFallingBlockChange(EntityChangeBlockEvent event) {
		System.out.println("BLOCK LANDED");
		event.setCancelled(true);
		List<MetadataValue> data = event.getEntity().getMetadata("UUID");
		Player thrower = Bukkit.getPlayer(UUID.fromString(data.get(0).asString()));
		FallingBlock fallingBlock = (FallingBlock) event.getEntity();
		Material blockType = fallingBlock.getBlockData().getMaterial();
		Location location = fallingBlock.getLocation();
		World world = location.getWorld();
		assert world != null;
		fallingBlock.remove();
		assert thrower != null;
		if (blockType == Material.SNOW_BLOCK) {
			Objects.requireNonNull(location.getWorld()).createExplosion(location, 6.0f, false, false, fallingBlock);
			world.spawnParticle(Particle.SNOWBALL, location, 150 , 1.5,1.5,1.5);
		} else if (blockType == Material.DARK_OAK_LOG) {
			Random random = new Random();
			int drop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
				int numDrops = random.nextInt(5);
				int xDir = random.nextInt(4) - 3;
				int zDir = random.nextInt(4) - 3;
				Location targetLocation = location.add(xDir, 0, zDir);
				for (int i = 0; i < numDrops; i++) {
					world.dropItem(targetLocation, SnowWar.fastBall);
				}
			}, 5L, 0L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> Bukkit.getScheduler().cancelTask(drop), 50L);
		}
	}

	public void onHit(ProjectileHitEvent hitEvent) {
		Projectile projectile = hitEvent.getEntity();
		if (projectile instanceof Snowball) {
			int task = particleTrails.remove(projectile);
			Bukkit.getScheduler().cancelTask(task);
			Entity hitEntity = hitEvent.getHitEntity();
			ProjectileSource shooter = projectile.getShooter();
			if (hitEntity instanceof Player && shooter instanceof Player) {
				Player source = (Player) shooter;
				Player killer = (Player) hitEntity;
				int sourceTeam = snowWar.playerData.get(source).getThird();
				int killerTeam = snowWar.playerData.get(killer).getThird();
				if (sourceTeam != killerTeam) {
					snowWar.playerKill(killer);
					snowWar.increaseKill(source);
				}

			}
		} else if (projectile instanceof EnderPearl) {
			EnderPearl pearl = (EnderPearl) projectile;
			@NotNull Vector direction = pearl.getLocation().getDirection().normalize();
			pearl.remove();
			Block hitBlock = hitEvent.getHitBlock();
			System.out.println("DIR: " + direction);
		}
	}

	private void onLaunch(Projectile projectile) {
		AtomicInteger counter = new AtomicInteger(0);
		int trailTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			int task = particleTrails.get(projectile);
			counter.getAndAdd(1);
			if (counter.get() > 40) {
				particleTrails.remove(projectile);
				projectile.remove();
				Bukkit.getScheduler().cancelTask(task);
			} else {
				Location current = projectile.getLocation();
				projectile.getWorld().spawnParticle(Particle.SNOWBALL, current, 15);
			}
		}, 10L, 0L);
		particleTrails.put(projectile, trailTask);
	}

	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (event.getHand() == EquipmentSlot.HAND) {
			event.setCancelled(true);
			boolean clickedSnowballSpot = false;
			//Check if snowball spot is clicked
			if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
				clickedSnowballSpot = clickBlock(player, event.getClickedBlock());
			}
			//Check if player contains key item
			if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
				ItemStack itemInHand = event.getItem();
				if (itemInHand != null) {
					boolean isFastBall = itemInHand.getType() == Material.FIREWORK_STAR;
					if (itemInHand.getType() == Material.SNOWBALL || isFastBall) {
						if (!clickedSnowballSpot) {
							Projectile thrown = player.launchProjectile(Snowball.class);
							if (isFastBall) thrown.setGravity(false);
							onLaunch(thrown);
							if(!rapidFire.contains(player)) itemInHand.setAmount(itemInHand.getAmount()-1);
						}
					} else {
						boolean isKillStreak = false;
						for (KillStreak streak : KillStreak.values()) {
							if (streak.getDisplay().equals(itemInHand)) {
								switch (streak) {
									case SONAR:
										snowWar.activateSonar(player);
										break;
									case BOMBER:
										snowWar.activateBomber(player);
										break;
									case SUPPLYDROP:
										Block clickedBlock = event.getClickedBlock();
										snowWar.activateSupplyDrop(player, clickedBlock != null ? clickedBlock.getLocation() : player.getLocation());
										break;
									default:
										break;
								}
								isKillStreak = true;
								break;
							}
						}
						if (!isKillStreak) {
							if (ItemBox.hasBox(player)) {
								ItemBox box = ItemBox.getBox(player);
								if (box.isActive()) {
									player.getInventory().setItem(3, SnowWar.presentPlaceHolder);
									box.getActiveItem().activate();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Player breaker = event.getPlayer();
		Block broken = event.getBlock();
		event.setCancelled(true);
		if (snowWar.presentSpots.containsKey(broken.getLocation())) {
			if (!ItemBox.hasBox(breaker)) {
				//Build probabilities for items
				ProbabilityMap probabilities = snowWar.getGameMap().getProbabilities();
				List<GameItem> items = new ArrayList<>();
				List<Double> possibilities = new ArrayList<>();
				for (String item : probabilities.getItems()) {
					SnowWarItems enumValue = SnowWarItems.valueOf(item);
					items.add(enumValue.createItem(breaker));
					possibilities.add(probabilities.getProb(item));
				}
				ItemBox.addBox(new ItemBox(breaker, items, possibilities, 3), breaker);
			}
		}
	}

	private boolean clickBlock(Player clicking, Block clicked) {
		if (clicked != null) {
			if (snowWar.snowballSpots.containsKey(clicked.getLocation())) {
				clicking.getInventory().addItem(SnowWar.defaultBall);
				return true;
			}
		}
		return false;
	}

	public void enableRapidFire(Player holder) {
		rapidFire.add(holder);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> rapidFire.remove(holder), 3*20L);
	}

	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
			event.setCancelled(true);
			Player damaged = (Player) event.getEntity();
			int playerTeam = snowWar.playerData.get(damaged).getThird();
			Entity damager = event.getDamager();
			Player killer;
			int killerTeam;
			if (damager instanceof FallingBlock) {
				UUID uuid = UUID.fromString(damager.getMetadata("UUID").get(0).asString());
				killer = Bukkit.getPlayer(uuid);
				killerTeam = snowWar.playerData.get(killer).getThird();
			} else if (damager instanceof Player) {
				killer = (Player) damager;
				killerTeam = snowWar.playerData.get(killer).getThird();
			} else {
				return;
			}
			if (playerTeam != killerTeam) {
				snowWar.playerKill(damaged);
				snowWar.increaseKill(killer);
			}
		}
	}

	public void onFireWorkExplosion(FireworkExplodeEvent event) {
		Firework supplyDrop = event.getEntity();
		Player shooter = Bukkit.getPlayer(UUID.fromString(supplyDrop.getMetadata("UUID").get(0).asString()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
			System.out.println("DROPPING SUPPLY DROP");
			assert shooter != null;
			World world = shooter.getWorld();
			Location explosion =supplyDrop.getLocation();
			explosion.setY(explosion.getY() + 50.0);
			FallingBlock snowball = world.spawnFallingBlock(explosion, Bukkit.createBlockData(Material.DARK_OAK_LOG));
			snowball.setHurtEntities(false);
			snowball.setDropItem(false);
			snowball.setMetadata("UUID", new FixedMetadataValue(Lobby.getPlugin(), shooter.getUniqueId().toString()));
		});
	}

	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
		}
	}
}
