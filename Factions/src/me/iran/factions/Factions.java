package me.iran.factions;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.iran.factions.faction.ClaimEvent;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.faction.cmd.FactionCommands;
import me.iran.factions.listeners.BlockChangeInClaim;
import me.iran.factions.listeners.EnteringClaim;
import me.iran.factions.listeners.FactionChat;
import me.iran.factions.listeners.FactionDeathEvent;
import me.iran.factions.listeners.InteractWithItemsInClaim;
import me.iran.factions.listeners.MoveWhileTeleporting;
import me.iran.factions.listeners.PlaceItemsInClaim;
import me.iran.factions.listeners.PlayerConnectionEvents;
import net.milkbowl.vault.economy.Economy;

public class Factions extends JavaPlugin {

	File file = null;
	File fFile = null;
	
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
		Bukkit.getPluginManager().registerEvents(new FactionDeathEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockChangeInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new PlaceItemsInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new EnteringClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerConnectionEvents(this), this);
		Bukkit.getPluginManager().registerEvents(new MoveWhileTeleporting(this), this);
		Bukkit.getPluginManager().registerEvents(new InteractWithItemsInClaim(this), this);
		Bukkit.getPluginManager().registerEvents(new FactionChat(this), this);
		
		run.runTaskTimer(this, 0, 20L);
		
		file = new File(this.getDataFolder(), "factions.yml");
		
		fFile = new File(this.getDataFolder() + "/Factions");

		if (!fFile.exists()) {
			System.out.println("[Factions] Couldn't Find any factions");
			file.mkdir();
			fFile = new File(this.getDataFolder() + "/Factions", "deleteme.yml");
			new YamlConfiguration();

			YamlConfiguration delete = YamlConfiguration
					.loadConfiguration(fFile);
			delete.createSection("Delete");

			delete.set("Delete", "Delete this file, but leave the folder!");
			try {
				delete.save(fFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[Factions] Created the directory 'Factions' with no errors!");
		}

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
		
		FactionManager.getManager().loadFactions();
		
		getCommand("team").setExecutor(new FactionCommands(this));
		//getCommand("factionadmin").setExecutor(new FactionAdminCommands(this));

		getConfig().options().copyDefaults(true);
		saveConfig();
		
	}
	
	public void onDisable() {
		FactionManager.getManager().saveFactions();
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
