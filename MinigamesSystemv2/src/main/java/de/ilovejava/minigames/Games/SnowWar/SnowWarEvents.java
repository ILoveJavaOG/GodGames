package de.ilovejava.minigames.Games.SnowWar;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.Communication.Tracker;
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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.fastBall;
import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.presentPlaceHolder;

/**
 * Class to handle events during the fish fight
 */
public class SnowWarEvents implements Events {

	//Holder class
	private final SnowWar snowWar;

	//Current edit sessions for the supply drops
	protected final Set<EditSession> oldSessions = Sets.newConcurrentHashSet();

	//Locations for all blocks which are build
	protected final Set<Location> buildBlocks = Sets.newConcurrentHashSet();

	//Map which contains projectiles which should have trails
	protected final ConcurrentHashMap<Projectile, Integer> particleTrails = new ConcurrentHashMap<>();

	//Active players with rapid fire
	private final Set<Player> rapidFire = Sets.newConcurrentHashSet();

	/**
	 * Constructor for the event class
	 *
	 * @param snowWar(SnowWar): The SnowWar instance which is using this class
	 */
	public SnowWarEvents(SnowWar snowWar) {
		this.snowWar = snowWar;
	}

	/**
	 * Event which is called when a block is changing state
	 *
	 * @param event(EntityChangeBlockEvent): Event on change
	 */
	@IsUsed
	public void onFallingBlockChange(EntityChangeBlockEvent event) {
		//Remove entity from tracker
		Player thrower = Tracker.consume(event.getEntity());
		//Get falling block
		FallingBlock fallingBlock = (FallingBlock) event.getEntity();
		Material blockType = fallingBlock.getBlockData().getMaterial();
		Location location = fallingBlock.getLocation();
		World world = location.getWorld();
		assert world != null;
		//Kill the falling block
		fallingBlock.remove();
		assert thrower != null;
		//BigBall or Bomber
		if (blockType == Material.SNOW_BLOCK) {
			event.setCancelled(true);
			//Animate hit
			world.createExplosion(location, 3.0f, false, false, thrower);
			world.spawnParticle(Particle.SNOWBALL, location, 150 , 1.5,1.5,1.5);
		//Supply drop
		} else {
			//Load the tree
			loadTree(thrower, location);
			Random random = new Random();
			int drop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
				//Drop items
				int numDropsSnowBall = random.nextInt(5);
				int numDropsSnow = random.nextInt(3);
				int xDir = random.nextInt(4) - 3;
				int zDir = random.nextInt(4) - 3;
				Location targetLocation = location.clone().add(xDir, 0, zDir);
				for (int i = 0; i < numDropsSnowBall; i++) {
					world.dropItem(targetLocation, fastBall);
				}
				for (int i = 0; i < numDropsSnow; i++) {
					world.dropItem(targetLocation, new ItemStack(Material.SNOW_BLOCK));
				}
			}, 20L, 5L);
			//Drop a random shovel
			world.dropItem(location, SnowWarItemStacks.shovels[random.nextInt(5)]);
			//Stop dropping items
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> Bukkit.getScheduler().cancelTask(drop), 70L);
		}
	}

	/**
	 * Event which is called when click inside the player inventory happens
	 *
	 * @param event(InventoryClickEvent): Event on inventory click
	 */
	@IsUsed
	public void onInventoryClick(InventoryClickEvent event) {
		int raw = event.getRawSlot();
		//Streak or item clicked. Or armor contents
		if ((5 <= raw && raw <= 8) || 36 <= raw && raw <= 39) {
			event.setCancelled(true);
		} else {
			Player dropper = (Player) event.getWhoClicked();
			int slot = event.getSlot();
			//Inventory stuff
			Inventory inventory = event.getInventory();
			ItemStack[] contents = inventory.getContents();
			//Check if a player is inside the crafting inventory
			boolean isCrafting = (event.getSlotType() == InventoryType.SlotType.CRAFTING);
			//Item at target slot
			ItemStack item = isCrafting? contents[slot] : dropper.getInventory().getItem(slot);
			if (item != null && slot >= 0) {
				InventoryAction action = event.getAction();
				//Checks if items should be dropped
				boolean oneSlot = (action == InventoryAction.DROP_ONE_SLOT);
				boolean doesDrop = (oneSlot || action == InventoryAction.DROP_ALL_SLOT);
				event.setCancelled(doesDrop);
				if (doesDrop) {
					//Setup for item drop
					Location eyeLocation = dropper.getEyeLocation();
					World world = eyeLocation.getWorld();
					Vector direction = eyeLocation.getDirection().normalize().multiply(0.3);
					ItemStack drop = item.clone();
					ItemStack old = item.clone();
					//Update items
					if (oneSlot) {
						drop.setAmount(1);
						old.setAmount(item.getAmount() - 1);
					} else {
						old = new ItemStack(Material.AIR);
					}
					//Change inventory
					if (isCrafting) {
						contents[slot] = old;
						inventory.setContents(contents);
					} else {
						inventory.setItem(slot, old);
					}
					assert world != null;
					//Dropping item
					Item droppedItem = world.dropItem(eyeLocation, drop);
					droppedItem.setPickupDelay(40);
					droppedItem.setVelocity(direction);
				}
			}
		}
	}

	//Slots which should not be accessible (as raw slots). Hot bar 1-4 and Armor
	private static final List<Integer> forbiddenSlots = Arrays.asList(5, 6, 7 , 8, 36, 37, 38, 39);

	/**
	 * Event which is called when the player drags items through the inventory
	 *
	 * @param event(InventoryClickEvent): Event on inventory drag
	 */
	@IsUsed
	public void onInventoryDrag(InventoryDragEvent event) {
		//Check if forbidden slots are accessed
		if (!Collections.disjoint(forbiddenSlots, event.getRawSlots())) {
			event.setCancelled(true);
		}
	}

	/**
	 * Event which is called when an item is dropped
	 *
	 * @param event(PlayerDropItemEvent): Event on item drop
	 */
	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		//Check if item is dropped from first slots of hot bar
		event.setCancelled(event.getPlayer().getInventory().getHeldItemSlot() <= 3);
	}

	/**
	 * Event which is called when an projectile is launched
	 *
	 * @param launchEvent(ProjectileLaunchEvent): Event of projectile launch
	 */
	@IsUsed
	public void onProjectileLaunch(ProjectileLaunchEvent launchEvent) {
		//Check if projectile is a snowball
		Projectile projectile = launchEvent.getEntity();
		if (projectile instanceof Snowball) {
			//Make snowballs fly straight
			projectile.setGravity(false);
			//Mark projectile
			onLaunch(projectile);
			Player shooter = (Player) launchEvent.getEntity().getShooter();
			assert shooter != null;
			//If rapid fire is active give player back snowballs
			if (rapidFire.contains(shooter)) shooter.getInventory().addItem(fastBall);

		}
	}

	/**
	 * Event which is called when an projectile hits
	 *
	 * @param hitEvent(ProjectileHitEvent): Event of projectile hit
	 */
	@IsUsed
	public void onProjectileHit(ProjectileHitEvent hitEvent) {
		Projectile projectile = hitEvent.getEntity();
		//Snowball which hit
		if (projectile instanceof Snowball) {
			//Remove projectile
			int task = particleTrails.remove(projectile);
			projectile.remove();
			Bukkit.getScheduler().cancelTask(task);
			Entity hitEntity = hitEvent.getHitEntity();
			ProjectileSource shooter = projectile.getShooter();
			if (hitEntity instanceof Player && shooter instanceof Player) {
				Player source = (Player) shooter;
				Player killer = (Player) hitEntity;
				//Get the teams
				int sourceTeam = snowWar.playerData.get(source).getTeam();
				int killerTeam = snowWar.playerData.get(killer).getTeam();
				//Mark hits
				if (sourceTeam != killerTeam) {
					snowWar.playerHit(killer);
					snowWar.increaseHit(source);
				}
			}
		}
	}

	/**
	 * Utility method to create trails for the snowballs
	 *
	 * @param projectile(Projectile): Projectile which gets a tail
	 */
	private void onLaunch(Projectile projectile) {
		AtomicInteger counter = new AtomicInteger(0);
		//Task to create trail
		int trailTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			//Stop trails after time
			if (counter.incrementAndGet() > 80) {
				int task = particleTrails.get(projectile);
				particleTrails.remove(projectile);
				projectile.remove();
				Bukkit.getScheduler().cancelTask(task);
			} else {
				//Create particle
				projectile.getWorld().spawnParticle(Particle.SNOWBALL, projectile.getLocation(), 5);
			}
		}, 0L, 5L);
		particleTrails.put(projectile, trailTask);
	}

	/**
	 * Event which is called when a player interacts with another entity
	 *
	 * @param event(PlayerInteractAtEntityEvent): Event when a player interacts with a entity
	 */
	@IsUsed
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Entity interacted = event.getRightClicked();
		if (event.getHand() == EquipmentSlot.HAND) {
			//Check if snowball is clicked
			if (interacted instanceof Snowman) {
				event.setCancelled(true);
				Player clicker = event.getPlayer();
				ItemStack handItem = clicker.getInventory().getItemInMainHand();
				//Add snowball back if clicked with snowball
				if (handItem.getType() == Material.SNOWBALL) handItem.setAmount(handItem.getAmount() + 1);
				takeSnowball(clicker, interacted);
			}
		}
	}

	/**
	 * Method to retrieve a snowball for the player
	 *
	 * @param taker(Player): Player who took snowball
	 * @param interacted(Entity): SnowMan which was clicked
	 */
	private void takeSnowball(Player taker, Entity interacted) {
		Location snowBallSpot = interacted.getLocation();
		//Check if spot is still taken
		if (snowWar.takenSnowballSpots.containsKey(snowBallSpot)) {
			//Increment count and give player item
			int currentCount = snowWar.takenSnowballSpots.get(snowBallSpot).getCount().incrementAndGet();
			taker.getInventory().addItem(fastBall);
			//Remove snowman
			if (currentCount == 10) {
				interacted.remove();
				snowWar.takenSnowballSpots.remove(snowBallSpot);
				//Add spot back to possible list
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> snowWar.freeSnowBallSpots.add(snowBallSpot), 30*20L);
			}
		}
	}

	/**
	 * Event which is called when a player interacts
	 *
	 * @param event(PlayerInteractEvent): Event when a player interacts
	 */
	@IsUsed
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (event.getHand() == EquipmentSlot.HAND) {
			//Check if player contains key item
			if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
				PlayerInventory inventory = player.getInventory();
				int heldItemSlot = inventory.getHeldItemSlot();
				boolean isKillStreak = heldItemSlot < 3;
				//Kill streak is used
				if (isKillStreak) {
					event.setCancelled(true);
					KillStreak streak = KillStreak.values()[heldItemSlot];
					ItemStack heldItem = inventory.getItem(heldItemSlot);
					boolean validSelection = true;
					assert heldItem != null;
					//Check if kill streak is active
					if (heldItem.equals(streak.getDisplay())) {
						ItemStack firstItem = inventory.getItem(0);
						assert firstItem != null;
						boolean firstStreakUsed;
						//Check which streak is used and if activasion is valid
						if (0 < heldItemSlot) {
							firstStreakUsed = !firstItem.equals(KillStreak.SONAR.getDisplay());
							validSelection = firstStreakUsed;
							if (heldItemSlot == 2) {
								ItemStack secondItem = inventory.getItem(1);
								assert secondItem != null;
								validSelection = firstStreakUsed && !secondItem.equals(KillStreak.SUPPLYDROP.getDisplay());
							}
						}
						//Valid streak activation
						if (validSelection) {
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
						}
					}
				//Game item is activated
				} else if (heldItemSlot == 3) {
					event.setCancelled(true);
					//Activate item box
					if (ItemBox.hasBox(player)) {
						ItemBox box = ItemBox.getBox(player);
						if (box.isActive()) {
							player.getInventory().setItem(3, presentPlaceHolder);
							box.getActiveItem().activate();
						}
					}
				}
			}
		}
	}

	/**
	 * Event which is called when a player places a block
	 *
	 * @param event(BlockPlaceEvent): Event when a player placed a block
	 */
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		//Stop building during loading
		if (snowWar.gameState == InternalGameState.INITIAL) {
			event.setCancelled(true);
		} else {
			buildBlocks.add(event.getBlock().getLocation());
		}
	}

	/**
	 * Event which is called when a player break a block
	 *
	 * @param event(BlockPlaceEvent): Event when a player broke a block
	 */
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		//Check if breaking is allowed
		if (snowWar.gameState != InternalGameState.INITIAL) {
			Player breaker = event.getPlayer();
			Block broken = event.getBlock();
			Location breakLocation = broken.getLocation();
			//Check if present is broken
			if (snowWar.takenPresentSpots.contains(breakLocation)) {
				event.setCancelled(true);
				snowWar.takenPresentSpots.remove(breakLocation);
				breakLocation.getBlock().setType(Material.AIR, true);
				Random random = new Random();
				//Add location back to free spots
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> snowWar.freePresentSpots.add(breakLocation), (random.nextInt(3) + 1) * 20L*30);
				//Check if player already has an active item
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
			} else {
				//Break already build block
				if (buildBlocks.contains(broken.getLocation())) {
					buildBlocks.remove(event.getBlock().getLocation());
					event.getBlock().breakNaturally();
					return;
				}
				event.setCancelled(true);
			}
		}
	}

	/**
	 * Method which is to be called when a player activates rapid fire
	 *
	 * @param holder(Player): Player who activated the item
	 */
	public void enableRapidFire(Player holder) {
		//Add player
		rapidFire.add(holder);
		//Remove player after 3 seconds
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> rapidFire.remove(holder), 3*20L);
	}

	/**
	 * Event which is to be called when a entity is being damaged by another entity
	 *
	 * @param event(EntityDamageByEntityEvent): Event which is called if a entity is damaging another entity
	 */
	@IsUsed
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		//Cancel damage
		event.setCancelled(true);
		//Snow bomb explodes
		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
			Player damaged = (Player) event.getEntity();
			int playerTeam = snowWar.playerData.get(damaged).getTeam();
			Entity damageSource = event.getDamager();
			Player damager;
			int damagerTeam;
			//Get who damaged
			if (damageSource instanceof FallingBlock) {
				damager = Tracker.consume(damageSource);
			} else if (damageSource instanceof Player) {
				damager = (Player) damageSource;
			} else {
				event.setCancelled(true);
				return;
			}
			//Mark as hit
			damagerTeam = snowWar.playerData.get(damager).getTeam();
			if (playerTeam != damagerTeam) {
				snowWar.playerHit(damaged);
				snowWar.increaseHit(damager);
			}
		//Check for hit
		} else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				Entity damagedEntity = event.getEntity();
				//Player by player damage (Melee hit)
				if (damagedEntity instanceof Player) {
					event.setCancelled(true);
					Player damaged = (Player) damagedEntity;
					ItemStack handItem = damager.getInventory().getItemInMainHand();
					Material itemType = handItem.getType();
					//Check if item in hand is shovel
					if (itemType == Material.WOODEN_SHOVEL ||
						itemType == Material.STONE_SHOVEL  ||
					    itemType == Material.IRON_SHOVEL   ||
						itemType == Material.GOLDEN_SHOVEL ||
						itemType == Material.DIAMOND_SHOVEL) {
						//Remove shovel and do instant kill
						damager.getInventory().setItemInMainHand(null);
						damager.playSound(damaged.getLocation(), Sound.ENTITY_BLAZE_HURT, 0.5f, 1.0f);
						snowWar.instantKill(damaged);
					} else {
						//Do double hit
						damaged.playSound(damaged.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
						snowWar.playerHit(damaged);
						snowWar.playerHit(damaged);
					}
					//Mark hit
					damager.playSound(damager.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1);
					snowWar.increaseHit(damager);
				} else if (damagedEntity instanceof Snowman) {
					//Stop snowman from taking damage and give snowball
					event.setCancelled(true);
					takeSnowball(damager, damagedEntity);
				}
			}
		}
	}

	/**
	 * Event which is called when a supply drop is being activated
	 *
	 * @param event(EntityDamageByEntityEvent): Event which is called when a firework explodes
	 */
	@IsUsed
	public void onFireWorkExplosion(FireworkExplodeEvent event) {
		Firework supplyDrop = event.getEntity();
		Player shooter = Tracker.consume(supplyDrop);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
			assert shooter != null;
			//Spawn a falling block for display of physical drop
			Location explosion = supplyDrop.getLocation();
			FallingBlock sapling = shooter.getWorld().spawnFallingBlock(explosion, Bukkit.createBlockData(Material.DARK_OAK_LOG));
			sapling.setHurtEntities(false);
			sapling.setDropItem(false);
			//Mark entity
			Tracker.bindEntity(sapling, shooter);
		});
	}

	/**
	 * Utility method to build a tree using the world edit api
	 *
	 * @param loader(Player): Player who activated the streak
	 * @param dropSpot(Location): Location of where to spawn the tree
	 */
	private void loadTree(Player loader, Location dropSpot) {
		World world = loader.getWorld();
		//Add needed offset
		Location shifted = dropSpot.clone().add(-7, 9, 1);
		File myTreeFile = new File("plugins/Minigames/SnowWar/Schematics/ChristmasTree.schem");
		ClipboardFormat format = ClipboardFormats.findByFile(myTreeFile);
		try {
			assert format != null;
			//Spawn tree
			BlockVector3 start = BlockVector3.at(shifted.getBlockX(), shifted.getBlockY(), shifted.getBlockZ());
			ClipboardReader reader = format.getReader(new FileInputStream(myTreeFile));
			Clipboard clipboard = reader.read();
			com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(world);
			EditSession editSession = WorldEdit.getInstance()
					.newEditSessionBuilder()
					.world(adapted)
					.build();
			Operation operation = new ClipboardHolder(clipboard)
					.createPaste(editSession)
					.to(start)
					.ignoreAirBlocks(true)
					.build();
			Operations.completeBlindly(operation);
			editSession.close();
			//Remove tree from world
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				oldSessions.remove(editSession);
				editSession.undo(editSession);
			}, 5*20L);
		//Possible errors
		} catch (FileNotFoundException e) {
			System.out.println("DID NOT FIND FILE");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO ERROR");
			e.printStackTrace();
		}
	}

	/**
	 * Utility function to rotate a vector using the given cos and sin values
	 *
	 * @param vector(Vector): Vector which is to be changed
	 * @param cosine(double): cos value
	 * @param sine(double): sin value
	 *
	 * @return Rotated vector
	 */
	private static Vector rotate(Vector vector, double cosine, double sine) {
		double x = vector.getX();
		double z = vector.getZ();
		//Rotation
		return new Vector(cosine * x - sine * z, 0, sine * x + cosine * z);
	}

	//Arrays containing the cos and sin values for a ring
	private static final double[] cosineRingValues = {1.0, 0.86602540378, 0.5, 0, -0.5, -0.86602540378};
	private static final double[] sineRingValues   = {0.0, 0.5, 0.86602540378, 1, 0.86602540378, 0.5};

	//Arrays containing the value for the vector rotations
	private static final double[] cosineValues = {0, -0.3420201, -0.42261826, -0.3420201, -0.42261826};
	private static final double[] sineValues   = {0,  0.9396926, -0.90630778,  0.9396926, -0.90630778};

	/**
	 * Method which is called when the player activates the bomber
	 *
	 * @param event(PlayerTeleportEvent): Event which is called on player teleport
	 */
	@IsUsed
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		//Must be a pearl
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
			Location from = event.getFrom();
			Location to = event.getTo();
			assert to != null;
			double xDirection = to.getX() - from.getX();
			double zDirection = to.getZ() - from.getZ();
			//Get direction vector
			@NotNull Vector direction = new Vector(xDirection, 0, zDirection).normalize();
			//Set pitch and yaw
			to.setDirection(direction);
			World world = to.getWorld();
			Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 5.0f);
			assert world != null;
			//Spawn ring
			for (int i = 0; i < 6; i++) {
				world.spawnParticle(Particle.REDSTONE, to.clone().add(cosineRingValues[i], 0, sineRingValues[i]), 5, dustOptions);
				world.spawnParticle(Particle.REDSTONE, to.clone().add(-cosineRingValues[i], 0, -sineRingValues[i]), 5, dustOptions);
			}
			to.add(0, 20, 0);
			//Arrays for entities
			Arrow[] formation = new Arrow[15];
			Phantom[] aboveGround = new Phantom[15];
			//Spawn bombers
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				Location spawn;
				//Create starting offset
				to.subtract(direction.clone().multiply(15));
				//Iterate over waves
				for (int wave = 0; wave < 3; wave++) {
					//Spawn 5 phantoms per wave
					for (int phantomNumber = 0; phantomNumber < 5; phantomNumber++) {
						//Get current row
						int row = Math.floorDiv(phantomNumber + 1, 2);
						//Set pitch and yaw of location
						spawn = to.clone().add(rotate(direction, cosineValues[phantomNumber], sineValues[phantomNumber]).multiply(row*10));
						//Spawn phantoms as high as possible
						Location above = new Location(spawn.getWorld(), spawn.getX(), 255, spawn.getZ());
						Phantom passenger = world.spawn(above, Phantom.class);
						passenger.setInvulnerable(true);
						passenger.setAI(false);
						//Bind entity
						Tracker.bindEntity(passenger, event.getPlayer());
						//Spawn arrows
						int index = wave * 5 + phantomNumber;
						aboveGround[index] = passenger;
						Arrow arrow = world.spawn(spawn, Arrow.class);
						arrow.setGravity(false);
						arrow.addPassenger(passenger);
						formation[index] = arrow;
					}
					//Offset for the next wave
					to.add(direction.clone().multiply(20));
				}
				//Setup entities
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
					for (int i = 0; i < 15; i++) {
						//Make phantoms ride arrows
						aboveGround[i].teleport(formation[i].getLocation().add(0, 1, 0).setDirection(direction), PlayerTeleportEvent.TeleportCause.PLUGIN);
						formation[i].addPassenger(aboveGround[i]);
						formation[i].setVelocity(direction.clone().multiply(0.4));
					}
				}, 20L*3);
			}, 0L);

			//Task while bombing
			AtomicInteger bombingTask = new AtomicInteger(
					Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
						//Spawn bombs
						for (Arrow arrow : formation) {
							Location dropSpot = arrow.getLocation();
							FallingBlock snowball = world.spawnFallingBlock(dropSpot, Bukkit.createBlockData(Material.SNOW_BLOCK));
							snowball.setHurtEntities(false);
							snowball.setDropItem(false);
							//Bind snowball
							Tracker.bindEntity(snowball, event.getPlayer());
						}
					}, 5*20L, 20L*3)
			);
			//Stop bombing and remove entities
			Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
				Bukkit.getScheduler().cancelTask(bombingTask.get());
				for (Arrow arrow : formation) {
					for (Entity passenger : arrow.getPassengers()) {
						arrow.removePassenger(passenger);
						Tracker.consume(passenger);
						passenger.remove();
					}
					arrow.remove();
				}
			}, 24*20L);
		}
	}
}
