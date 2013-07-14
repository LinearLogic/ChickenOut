package com.entrocorp.linearlogic.chickenout;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		if (getConfig().getBoolean("disable-egg-laying"))
			event.setCancelled(true);
	}
}
