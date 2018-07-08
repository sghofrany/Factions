package me.iran.factions;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.iran.factions.faction.ClaimEvent;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.faction.cmd.FactionAdminCommands;
import me.iran.factions.faction.cmd.FactionCommands;
import me.iran.factions.listeners.BlockChangeInClaim;
import me.iran.factions.listeners.EnteringClaim;
import me.iran.factions.listeners.FactionChat;
import me.iran.factions.listeners.FactionDeathEvent;
import me.iran.factions.listeners.InteractWithItemsInClaim;
import me.iran.factions.listeners.MoveWhileTeleporting;
import me.iran.factions.listeners.PlaceItemsInClaim;
import me.iran.factions.listeners.PlayerConnectionEvents;
import me.iran.factions.listeners.PvPTimer;
import me.iran.factions.system.SystemClaimEvent;
import me.iran.factions.system.SystemFactionCommands;
import me.iran.factions.system.SystemFactionManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Factions extends JavaPlugin {

	//TODO: Stop people from breaking inside of system faction claims
	
	private File file = null;
	
	public static Factions instance;
	
	FactionRunnables run = new FactionRunnables(this);
	
	public static Economy economy = null;
	
	public void onEnable() {
		
		instance = this;

		if(!setupEconomy()) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Log] Could not find a Economy plugin, disabling Factions!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		run.runTaskTimer(this, 0, 20L);
		
		registerEvents();
		
		registerCommands();
		
		setupFiles(file);
		
		FactionManager.getManager().loadFactions();
		
		SystemFactionManager.getManager().loadFactions();
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
	}
	
	public void onDisable() {
		FactionManager.getManager().saveFactions();
		SystemFactionManager.getManager().saveFactions();
	}

	private void setupFiles(File file) {
		file = new File(this.getDataFolder() + "/Factions");

		if (!file.exists()) {
		
			file.mkdir();
			file = new File(this.getDataFolder() + "/Factions", "deleteme.yml");
			new YamlConfiguration();

			YamlConfiguration delete = YamlConfiguration
					.loadConfiguration(file);
			delete.createSection("Delete");

			delete.set("Delete", "Delete this file, but leave the folder!");
			try {
				delete.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Log] Created the directory 'Factions' successfully!");
	
		}

		file = new File(this.getDataFolder() + "/SysFactions");

		if (!file.exists()) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Log] Could not find any factions!");
			file.mkdir();
			file = new File(this.getDataFolder() + "/SysFactions", "deleteme.yml");
			new YamlConfiguration();

			YamlConfiguration delete = YamlConfiguration
					.loadConfiguration(file);
			delete.createSection("Delete");

			delete.set("Delete", "Delete this file, but leave the folder!");
			try {
				delete.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Log] Created the directory 'Factions' successfully!");
		}
		
		file = new File(this.getDataFolder(), "factions.yml");
		
		if (!file.exists()) {
			
			file = new File(this.getDataFolder(), "factions.yml");

			new YamlConfiguration();

			YamlConfiguration config = YamlConfiguration
					.loadConfiguration(file);

			config.createSection("factions");
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Created factions.yml");
		}
		
		file = new File(this.getDataFolder(), "factions.yml");
		
		if (!file.exists()) {
			
			file = new File(this.getDataFolder(), "sysfactions.yml");

			new YamlConfiguration();

			YamlConfiguration config = YamlConfiguration
					.loadConfiguration(file);

			config.createSection("factions");
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Created sysfactions.yml");
		}
	}

	private void registerEvents() {
		
		Bukkit.getPluginManager().registerEvents(new ClaimEvent(), this);
		Bukkit.getPluginManager().registerEvents(new FactionDeathEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockChangeInClaim(), this);
		Bukkit.getPluginManager().registerEvents(new PlaceItemsInClaim(), this);
		Bukkit.getPluginManager().registerEvents(new EnteringClaim(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerConnectionEvents(), this);
		Bukkit.getPluginManager().registerEvents(new MoveWhileTeleporting(), this);
		Bukkit.getPluginManager().registerEvents(new InteractWithItemsInClaim(), this);
		Bukkit.getPluginManager().registerEvents(new FactionChat(), this);
		Bukkit.getPluginManager().registerEvents(new SystemClaimEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PvPTimer(), this);
	
	}
	
	private void registerCommands() {
	
		getCommand("faction").setExecutor(new FactionCommands());
		
		getCommand("systemfaction").setExecutor(new SystemFactionCommands());
	
		getCommand("factionadmin").setExecutor(new FactionAdminCommands());
	
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
    
    public static Factions getInstance() {
    	return instance;
    }
	
}
