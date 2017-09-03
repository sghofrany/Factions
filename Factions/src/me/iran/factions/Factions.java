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
import me.iran.factions.system.SystemClaimEvent;
import me.iran.factions.system.SystemFactionCommands;
import me.iran.factions.system.SystemFactionManager;
import net.milkbowl.vault.economy.Economy;

public class Factions extends JavaPlugin {

	File file = null;
	
	public static Factions instance;
	
	FactionRunnables run = new FactionRunnables(this);
	
	public static Economy economy = null;
	
	public void onEnable() {
		
		instance = this;

		if(!setupEconomy()) {
			System.out.println("Couldn't Hook into Economy Plugin, Disabling Factions");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		Bukkit.getPluginManager().registerEvents(new ClaimEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new FactionDeathEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockChangeInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new PlaceItemsInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new EnteringClaim(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerConnectionEvents(this), this);
		Bukkit.getPluginManager().registerEvents(new MoveWhileTeleporting(this), this);
		Bukkit.getPluginManager().registerEvents(new InteractWithItemsInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new FactionChat(this), this);
		Bukkit.getPluginManager().registerEvents(new SystemClaimEvent(), this);
		
		run.runTaskTimer(this, 0, 20L);
		
		file = new File(this.getDataFolder() + "/Factions");

		if (!file.exists()) {
			System.out.println("[Factions] Couldn't Find any factions");
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
			System.out.println("[Factions] Created the directory 'Factions' with no errors!");
		}

		file = new File(this.getDataFolder() + "/SysFactions");

		if (!file.exists()) {
			System.out.println("[Factions] Couldn't Find any factions");
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
			System.out.println("[Factions] Created the directory 'Factions' with no errors!");
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
		
		FactionManager.getManager().loadFactions();
		
		SystemFactionManager.getManager().loadFactions();
		
		getCommand("faction").setExecutor(new FactionCommands(this));
		getCommand("systemfaction").setExecutor(new SystemFactionCommands());
		getCommand("factionadmin").setExecutor(new FactionAdminCommands());

		getConfig().options().copyDefaults(true);
		saveConfig();
		
	}
	
	public void onDisable() {
		FactionManager.getManager().saveFactions();
		SystemFactionManager.getManager().saveFactions();
	}

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public static Factions getInstance() {
    	return instance;
    }
	
}
