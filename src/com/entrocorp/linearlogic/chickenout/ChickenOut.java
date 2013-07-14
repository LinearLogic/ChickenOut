package com.entrocorp.linearlogic.chickenout;

import org.bukkit.plugin.java.JavaPlugin;

public class ChickenOut extends JavaPlugin {

	public void onEnable() {
		getLogger().info("Loading the config...");
		saveDefaultConfig();
		getLogger().info("SQUAWK! Plugin successfully enabled.");
	}

	public void onDisable() {
		saveConfig();
	}
}
