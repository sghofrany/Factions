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
						
						String loc1World = fac.getString("factions." + f + ".loc1.world");
						String loc2World = fac.getString("factions." + f + ".loc2.world");
						
						Location loc1 = new Location(Bukkit.getWorld(loc1World), x1, 0, z1);
						Location loc2 = new Location(Bukkit.getWorld(loc2World), x2, 0, z2);
					
						faction.setLoc1(loc1);
						faction.setLoc2(loc2);
					}
					
					if(fac.contains("factions." + f + ".home")) {
						
						int x = fac.getInt("factions." + f + ".home.x");
						int z = fac.getInt("factions." + f + ".home.z");
						
						String world = fac.getString("factions." + f + ".home.world");
					
						Location home = new Location(Bukkit.getWorld(world), x, 0, z);
					
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
					facConfig.set("factions." + name + ".deathban", faction.isDeathban());
					
					if (faction.getLoc1() != null) {
						facConfig.createSection("factions." + name + ".loc1.x");
						facConfig.createSection("factions." + name + ".loc1.z");
						facConfig.createSection("factions." + name + ".loc1.world");
						
						facConfig.set("factions." + name + ".loc1.x", faction.getLoc1().getBlockX());
						facConfig.set("factions." + name + ".loc1.z", faction.getLoc1().getBlockZ());
						facConfig.set("factions." + name + ".loc1.world", faction.getLoc1().getWorld().getName());
					}

					if (faction.getLoc2() != null) {
						facConfig.createSection("factions." + name + ".loc2.x");
						facConfig.createSection("factions." + name + ".loc2.z");
						facConfig.createSection("factions." + name + ".loc2.world");
						
						facConfig.set("factions." + name + ".loc2.x", faction.getLoc2().getBlockX());
						facConfig.set("factions." + name + ".loc2.z", faction.getLoc2().getBlockZ());
						facConfig.set("factions." + name + ".loc2.world", faction.getLoc2().getWorld().getName());
					}

					if (faction.getHome() != null) {
						facConfig.createSection("factions." + name + ".home.x");
						facConfig.createSection("factions." + name + ".home.z");
						facConfig.createSection("factions." + name + ".home.world");

						facConfig.set("factions." + name + ".home.x", faction.getHome().getBlockX());
						facConfig.set("factions." + name + ".home.z", faction.getHome().getBlockZ());
						facConfig.set("factions." + name + ".home.world", faction.getHome().getWorld().getName());

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
						facConfig.set("factions." + name + ".loc1.world", faction.getLoc1().getWorld().getName());
					} else {
						facConfig.set("factions." + name + ".loc1", null);
					}

					if (faction.getLoc2() != null) {
						facConfig.set("factions." + name + ".loc2.x", faction.getLoc2().getBlockX());
						facConfig.set("factions." + name + ".loc2.z", faction.getLoc2().getBlockZ());
						facConfig.set("factions." + name + ".loc2.world", faction.getLoc2().getWorld().getName());
					} else {
						facConfig.set("factions." + name + ".loc2", null);
					}

					if (faction.getHome() != null) {
						facConfig.set("factions." + name + ".home.x", faction.getHome().getBlockX());
						facConfig.set("factions." + name + ".home.z", faction.getHome().getBlockZ());
						facConfig.set("factions." + name + ".home.world", faction.getLoc1().getWorld().getName());

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
			
		} else {
			
			fFile = new File(Factions.getInstance().getDataFolder(), "sysfactions.yml");

			YamlConfiguration listConfig = YamlConfiguration.loadConfiguration(fFile);
			
			listConfig.createSection("factions"); 
			
			List<String> list = new ArrayList<>();
			
			for(SystemFaction fac : factions) {
				list.add(fac.getName());
			}
			
			listConfig.set("factions", list);
			
			try {
				listConfig.save(fFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public boolean isInsideClaim(Location loc) {
		for(SystemFaction faction : SystemFactionManager.getManager().getAllFactions()) {
			
			if(faction.getLoc1() != null && faction.getLoc2() != null) {
				Location loc1 = faction.getLoc1();
				Location loc2 = faction.getLoc2();
				

				int xMax = Math.max(loc1.getBlockX(), loc2.getBlockX());
				int zMax = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

				int xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
				int zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

				if ((loc.getBlockX() >= xMin) && (loc.getBlockX() <= xMax)) {
					if ((loc.getBlockZ() >= zMin) && (loc.getBlockZ() <= zMax)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public SystemFaction getFactionByLocation(Location loc) {
		for(SystemFaction faction : factions) {
			
			if(faction.getLoc1() != null && faction.getLoc2() != null) {
				Location loc1 = faction.getLoc1();
				Location loc2 = faction.getLoc2();
				

				int xMax = Math.max(loc1.getBlockX(), loc2.getBlockX());
				int zMax = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

				int xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
				int zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

				if ((loc.getBlockX() >= xMin) && (loc.getBlockX() <= xMax)) {
					if ((loc.getBlockZ() >= zMin) && (loc.getBlockZ() <= zMax)) {
						return faction;
					}
				}
			}
		}
		
		return null;
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
	
	public ArrayList<SystemFaction> getAllFactions() {
		return factions;
	}
	
}
