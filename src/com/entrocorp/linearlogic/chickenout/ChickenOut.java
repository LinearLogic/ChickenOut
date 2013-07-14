package com.entrocorp.linearlogic.chickenout;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ChickenLayEggEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChickenOut extends JavaPlugin implements Listener {

	private final String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Chicken" + ChatColor.YELLOW + ChatColor.ITALIC + "Out!" + ChatColor.GRAY + "] ";

	@Override
	public void onEnable() {
		getLogger().info("Loading the config...");
		saveDefaultConfig();
		getLogger().info("Registering listener...");
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("SQUAWK! Plugin successfully enabled.");
	}

	@Override
	public void onDisable() {
		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0)
			return sendMsg(sender, "Running version " + ChatColor.AQUA + getDescription().getVersion() + ChatColor.GRAY + " by LinearLogic.");
		if (args[0].equalsIgnoreCase("reload")) {
			if (sender instanceof Player && !sender.hasPermission("chickenout.reload"))
				return sendMsg(sender, "no perms");
			reloadConfig();
			return sendMsg(sender, ChatColor.GREEN + "Reloaded the config.");
		}
		return sendMsg(sender, "Available commands: " + ChatColor.YELLOW + "/co [reload]");
	}

	public boolean sendMsg(CommandSender sender, String msg) {
		sender.sendMessage(prefix + (msg.equals("no perms") ? "" + ChatColor.RED + ChatColor.ITALIC + "SQUAWK! " +
				ChatColor.GRAY + "Access denied." : msg));
		return true;
	}

	@EventHandler
	public void onEggDrop(ChickenLayEggEvent event) {
		Chicken chicken = event.getEntity();

		// Test validity of the event entity by using its location to spawn exp:
		if (getConfig().getInt("drop-exp") > 0) {
			ExperienceOrb exp = (ExperienceOrb) chicken.getWorld().spawnEntity(chicken.getLocation(), EntityType.EXPERIENCE_ORB);
			exp.setExperience(getConfig().getInt("drop-exp"));
		}

		// Test drop rate modification:
		if (getConfig().getInt("drop-rate") >= 0) // Plugin has specified a custom drop rate
			event.setTicksUntilNextEgg(getConfig().getInt("drop-rate"));

		// Test event cancellation:
		if (getConfig().getBoolean("disable-egg-laying.global")) {
			event.setCancelled(true);
			return;
		}
		String biome = chicken.getWorld().getBiome(chicken.getLocation().getBlockX(), chicken.getLocation().getBlockZ()).name();
		for (String blockedBiome : getConfig().getStringList("disable-egg-laying.in-biomes")) {
			if (blockedBiome.equalsIgnoreCase(biome)) {
				event.setCancelled(true);
				return;
			}
		}
	}
}
