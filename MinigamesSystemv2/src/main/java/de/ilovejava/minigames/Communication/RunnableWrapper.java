package de.ilovejava.minigames.Communication;

import de.ilovejava.lobby.Lobby;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class RunnableWrapper extends BukkitRunnable {

	private final Runnable runnable;
	private final @NotNull Plugin plugin = Lobby.getPlugin();

	public RunnableWrapper(Runnable runnable) {
		this.runnable = runnable;
	}
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		runnable.run();
	}


	public synchronized @NotNull BukkitTask runTask() throws IllegalArgumentException, IllegalStateException {
		return super.runTask(plugin);
	}

	public synchronized @NotNull BukkitTask runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
		return super.runTaskAsynchronously(plugin);
	}

	public synchronized @NotNull BukkitTask runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
		return super.runTaskLater(plugin, delay);
	}

	public synchronized @NotNull BukkitTask runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
		return super.runTaskLaterAsynchronously(plugin, delay);
	}

	public synchronized @NotNull BukkitTask runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
		return super.runTaskTimer(plugin, delay, period);
	}

	public synchronized @NotNull BukkitTask runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
		return super.runTaskTimerAsynchronously(plugin, delay, period);
	}
}
