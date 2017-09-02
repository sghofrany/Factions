package me.iran.factions.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.iran.factions.Factions;

public class SystemFactionManager {

	File fFile = null;
	File file = null;
	
	private static ArrayList<SystemFaction> factions = new ArrayList<>();
	
	private static SystemFactionManager fm;

	private SystemFactionManager() {}

	public static SystemFactionManager getManager() {
		if (fm == null)
			fm = new SystemFactionManager();

		return fm;
	}
	
	public  void loadFactions() {
		
		fFile = new File(Factions.getInstance().getDataFolder(), "sysfactions.yml");
		
		if(fFile.exists()) {
			fFile = new File(Factions.getInstance().getDataFolder(), "sysfactions.yml");
			
			YamlConfiguration listConfig = YamlConfiguration.loadConfiguration(fFile);
			listConfig.get("factions");
			
			List<String> allFac = listConfig.getStringList("factions");

			for(int i = 0; i < allFac.size(); i++) {
				String name = allFac.get(i);
				
				file = new File(Factions.getInstance().getDataFolder() + "/SysFactions", name + ".yml");
				
				YamlConfiguration fac = YamlConfiguration.loadConfiguration(file);
				
				for(String f : fac.getConfigurationSection("factions").getKeys(false)) {
				
					String motd = fac.getString("factions." + f + ".motd");
					String facName = fac.getString("factions." + f + ".name");
					
					boolean deathban = fac.getBoolean("factions." + f + ".deathban");
					
					SystemFaction faction = new SystemFaction(facName);
					
					if(fac.contains("factions." + f + ".loc1") && fac.contains("factions." + f + ".loc2")) {
						int x1 = fac.getInt("factions." + f + ".loc1.x");
						int z1 = fac.getInt("factions." + f + ".loc1.z");
						
						int x2 = fac.getInt("factions." + f + ".loc2.x");
						int z2 = fac.getInt("factions." + f + ".loc2.z");
						
						Location loc1 = new Location(Bukkit.getWorld(Factions.getInstance().getConfig().getString("faction-world")), x1, 0, z1);
						Location loc2 = new Location(Bukkit.getWorld(Factions.getInstance().getConfig().getString("faction-world")), x2, 0, z2);
					
						faction.setLoc1(loc1);
						faction.setLoc2(loc2);
					}
					
					if(fac.contains("factions." + f + ".home")) {
						
						int x = fac.getInt("factions." + f + ".home.x");
						int y = fac.getInt("factions." + f + ".home.y");
						int z = fac.getInt("factions." + f + ".home.z");
						float pitch = fac.getFloat("factions." + f + ".home.pitch");
						float yaw = fac.getFloat("factions." + f + ".home.yaw");
					
						Location home = new Location(Bukkit.getWorld(Factions.getInstance().getConfig().getString("faction-world")), x, y, z);
						
						home.setPitch(pitch);
						home.setYaw(yaw);
					
						faction.setHome(home);
					}
					
					faction.setName(facName);
					faction.setDeathban(deathban);
					faction.setMotd(motd);
					faction.setColor(ChatColor.AQUA);
					
					factions.add(faction);
				}
			}
			
			System.out.println("All System Factions were loaded in correctly!");
		}
		
	}
	

	public void saveFactions() {
		// teams.yml file

		fFile = new File(Factions.getInstance().getDataFolder(), "sysfactions.yml");

		if (fFile.exists()) {

			fFile = new File(Factions.getInstance().getDataFolder(), "sysfactions.yml");

			YamlConfiguration listConfig = YamlConfiguration.loadConfiguration(fFile);

			List<String> f = listConfig.getStringList("factions");

			f.clear();

			for (int i = 0; i < factions.size(); i++) {
				f.add(factions.get(i).getName());

			}
			// set new list to config
			listConfig.set("factions", f);

			try {
				listConfig.save(fFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (String name : f) {

				SystemFaction faction = getFactionByName(name);

				file = new File(Factions.getInstance().getDataFolder() + "/SysFactions", name + ".yml");

				if (!file.exists()) {

					file = new File(Factions.getInstance().getDataFolder() + "/SysFactions", name + ".yml");

					YamlConfiguration facConfig = YamlConfiguration.loadConfiguration(file);

					facConfig.createSection("factions." + name + ".name");
					facConfig.createSection("factions." + name + ".motd");

					facConfig.createSection("factions." + name + ".deathban");

					facConfig.set("factions." + name + ".name", faction.getName());
					facConfig.set("factions." + name + ".motd", faction.getMotd());

					if (faction.getLoc1() != null) {
						facConfig.createSection("factions." + name + ".loc1.x");
						facConfig.createSection("factions." + name + ".loc1.z");

						facConfig.set("factions." + name + ".loc1.x", faction.getLoc1().getBlockX());
						facConfig.set("factions." + name + ".loc1.z", faction.getLoc1().getBlockZ());
					}

					if (faction.getLoc2() != null) {
						facConfig.createSection("factions." + name + ".loc2.x");
						facConfig.createSection("factions." + name + ".loc2.z");

						facConfig.set("factions." + name + ".loc2.x", faction.getLoc2().getBlockX());
						facConfig.set("factions." + name + ".loc2.z", faction.getLoc2().getBlockZ());
					}

					if (faction.getHome() != null) {
						facConfig.createSection("factions." + name + ".home.x");
						facConfig.createSection("factions." + name + ".home.y");
						facConfig.createSection("factions." + name + ".home.z");
						facConfig.createSection("factions." + name + ".home.pitch");
						facConfig.createSection("factions." + name + ".home.yaw");

						facConfig.set("factions." + name + ".home.x", faction.getHome().getBlockX());
						facConfig.set("factions." + name + ".home.y", faction.getHome().getBlockY());
						facConfig.set("factions." + name + ".home.z", faction.getHome().getBlockZ());
						facConfig.set("factions." + name + ".home.pitch", faction.getHome().getPitch());
						facConfig.set("factions." + name + ".home.yaw", faction.getHome().getYaw());

					}

					try {
						facConfig.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {

					file = new File(Factions.getInstance().getDataFolder() + "/SysFactions", name + ".yml");

					YamlConfiguration facConfig = YamlConfiguration.loadConfiguration(file);

					facConfig.set("factions." + name + ".name", faction.getName());
					facConfig.set("factions." + name + ".motd", faction.getMotd());

					if (faction.getLoc1() != null) {
						facConfig.set("factions." + name + ".loc1.x", faction.getLoc1().getBlockX());
						facConfig.set("factions." + name + ".loc1.z", faction.getLoc1().getBlockZ());
					} else {
						facConfig.set("factions." + name + ".loc1", null);
					}

					if (faction.getLoc2() != null) {
						facConfig.set("factions." + name + ".loc2.x", faction.getLoc2().getBlockX());
						facConfig.set("factions." + name + ".loc2.z", faction.getLoc2().getBlockZ());
					} else {
						facConfig.set("factions." + name + ".loc2", null);
					}

					if (faction.getHome() != null) {
						facConfig.set("factions." + name + ".home.x", faction.getHome().getBlockX());
						facConfig.set("factions." + name + ".home.y", faction.getHome().getBlockY());
						facConfig.set("factions." + name + ".home.z", faction.getHome().getBlockZ());

						facConfig.set("factions." + name + ".home.pitch", faction.getHome().getPitch());
						facConfig.set("factions." + name + ".home.yaw", faction.getHome().getYaw());

					} else {
						facConfig.set("factions." + name + ".home", null);
					}

					facConfig.set("factions." + name + ".deathban", faction.isDeathban());

					try {
						facConfig.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			// clear current list
			
		}
	}

	public void createFaction(Player player, String name) {
		
		if(doesFactionExist(name)) {
			player.sendMessage(ChatColor.RED + "That system faction already exists");
			return;
		}
		
		SystemFaction faction = new SystemFaction(name);
		
		factions.add(faction);
		
	}
	
	public SystemFaction getFactionByName(String name) {
		for(SystemFaction fac : factions) {
			if(fac.getName().equalsIgnoreCase(name)) {
				return fac;
			}
		}
		return null;
	}
	
	public boolean doesFactionExist(String name) {
		for(SystemFaction fac : factions) {
			if(fac.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
}
