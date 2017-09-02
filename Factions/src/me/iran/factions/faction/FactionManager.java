package me.iran.factions.faction;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import me.iran.factions.Factions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FactionManager {

	File fFile = null;
	File file = null;
	
	private static ArrayList<Faction> factions = new ArrayList<>();
	
	private static FactionManager fm;

	private FactionManager() {}

	public static FactionManager getManager() {
		if (fm == null)
			fm = new FactionManager();

		return fm;
	}
	
	public  void loadFactions() {
		
		fFile = new File(Factions.getInstance().getDataFolder(), "factions.yml");
		
		if(fFile.exists()) {
			fFile = new File(Factions.getInstance().getDataFolder(), "factions.yml");
			
			YamlConfiguration listConfig = YamlConfiguration.loadConfiguration(fFile);
			listConfig.get("factions");
			
			List<String> allFac = listConfig.getStringList("factions");

			for(int i = 0; i < allFac.size(); i++) {
				String name = allFac.get(i);
				
				file = new File(Factions.getInstance().getDataFolder() + "/Factions", name + ".yml");
				
				YamlConfiguration fac = YamlConfiguration.loadConfiguration(file);
				
				for(String f : fac.getConfigurationSection("factions").getKeys(false)) {
				
					String leader = fac.getString("factions." + f + ".leader");
					
					List<String> members = fac.getStringList("factions." + f + ".members");
					List<String> captains = fac.getStringList("factions." + f + ".captains");
					
					
					String motd = fac.getString("factions." + f + ".motd");
					
					int balance = fac.getInt("factions." + f + ".balance");
					int freezeTime = fac.getInt("factions." + f + ".freezeTime");
					double dtr = fac.getDouble("factions." + f + ".dtr");
					double maxDtr = fac.getDouble("factions." + f + ".maxdtr");
					
					String facName = fac.getString("factions." + f + ".name");
					
					boolean regen = fac.getBoolean("factions." + f + ".regen");
					boolean frozen = fac.getBoolean("factions." + f + ".frozen");
					boolean raidable = fac.getBoolean("factions." + f + ".raidable");
					
					Faction faction = new Faction(leader, facName);
					
					if(fac.contains("factions." + f + ".loc1") && fac.contains("factions." + f + ".loc2")) {
						int x1 = fac.getInt("factions." + f + ".loc1.x");
						int z1 = fac.getInt("factions." + f + ".loc1.z");
						
						int x2 = fac.getInt("factions." + f + ".loc2.x");
						int z2 = fac.getInt("factions." + f + ".loc2.z");
						
						Location loc1 = new Location(Bukkit.getWorld("world"), x1, 0, z1);
						Location loc2 = new Location(Bukkit.getWorld("world"), x2, 0, z2);
					
						faction.setLoc1(loc1);
						faction.setLoc2(loc2);
					}
					
					if(fac.contains("factions." + f + ".home")) {
						
						int x = fac.getInt("factions." + f + ".home.x");
						int y = fac.getInt("factions." + f + ".home.y");
						int z = fac.getInt("factions." + f + ".home.z");
						float pitch = fac.getFloat("factions." + f + ".home.pitch");
						float yaw = fac.getFloat("factions." + f + ".home.yaw");
					
						Location home = new Location(Bukkit.getWorld("world"), x, y, z);
						
						home.setPitch(pitch);
						home.setYaw(yaw);
					
						faction.setHome(home);
					}
					
					faction.setName(facName);
					faction.setBalance(balance);
					faction.setCaptainList(captains);
					faction.setMemberList(members);
					faction.setDtr(dtr);
					faction.setMaxDtr(maxDtr);
		
					faction.setMotd(motd);
					faction.setRegen(regen);
					faction.setFrozen(frozen);
					faction.setRaidable(raidable);
					faction.setFreezeTime(freezeTime);

					
					factions.add(faction);
				}
			}
			
			System.out.println("All Factions were loaded in correctly!");
		}
		
	}

	public void saveFactions() {
		// teams.yml file

		fFile = new File(Factions.getInstance().getDataFolder(), "factions.yml");

		if (fFile.exists()) {

			fFile = new File(Factions.getInstance().getDataFolder(), "factions.yml");

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

				Faction faction = getFactionByName(name);

				file = new File(Factions.getInstance().getDataFolder() + "/Factions", name + ".yml");

				if (!file.exists()) {

					file = new File(Factions.getInstance().getDataFolder() + "/Factions", name + ".yml");

					YamlConfiguration facConfig = YamlConfiguration.loadConfiguration(file);

					facConfig.createSection("factions." + name + ".name");
					facConfig.createSection("factions." + name + ".leader");
					facConfig.createSection("factions." + name + ".captains"); // List
					facConfig.createSection("factions." + name + ".members"); // List
					facConfig.createSection("factions." + name + ".motd");

					facConfig.createSection("factions." + name + ".balance");
					facConfig.createSection("factions." + name + ".dtr");
					facConfig.createSection("factions." + name + ".maxdtr");
					facConfig.createSection("factions." + name + ".freezeTime");

					facConfig.createSection("factions." + name + ".regen");
					facConfig.createSection("factions." + name + ".frozen");
					facConfig.createSection("factions." + name + ".raidable");

					facConfig.set("factions." + name + ".name", faction.getName());
					facConfig.set("factions." + name + ".leader", faction.getLeader());
					facConfig.set("factions." + name + ".motd", faction.getMotd());

					facConfig.set("factions." + name + ".captains", faction.getCaptainList());

					facConfig.set("factions." + name + ".members", faction.getMemberList());

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

					facConfig.set("factions." + name + ".balance", faction.getBalance());
					facConfig.set("factions." + name + ".dtr",
					faction.getDtr());
					facConfig.set("factions." + name + ".maxdtr",
					faction.getMaxDtr());
					facConfig.set("factions." + name + ".freezeTime", faction.getFreezeTime());

					facConfig.set("factions." + name + ".regen", faction.isRegen());
					facConfig.set("factions." + name + ".frozen", faction.isFrozen());
					facConfig.set("factions." + name + ".raidable", faction.isRaidable());

					try {
						facConfig.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {

					file = new File(Factions.getInstance().getDataFolder() + "/Factions", name + ".yml");

					YamlConfiguration facConfig = YamlConfiguration.loadConfiguration(file);

					facConfig.set("factions." + name + ".name", faction.getName());
					facConfig.set("factions." + name + ".leader", faction.getLeader());
					facConfig.set("factions." + name + ".motd", faction.getMotd());

					facConfig.set("factions." + name + ".captains", faction.getCaptainList());

					facConfig.set("factions." + name + ".members", faction.getMemberList());

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

					facConfig.set("factions." + name + ".balance", faction.getBalance());
					facConfig.set("factions." + name + ".dtr",
					faction.getDtr());
					facConfig.set("factions." + name + ".maxdtr",
					faction.getMaxDtr());
					facConfig.set("factions." + name + ".freezeTime", faction.getFreezeTime());

					facConfig.set("factions." + name + ".regen", faction.isRegen());
					facConfig.set("factions." + name + ".frozen", faction.isFrozen());
					facConfig.set("factions." + name + ".raidable", faction.isRaidable());

					try {
						facConfig.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// Avoid memory leaks
				faction.getInvitesList().clear();

			}

			// clear current list
			
		}
	}

	public void createFaction(Player leader, String name) {
		
		String[] notAllowed = { ",", ";", "!", "@", "#", "$",
				"%", "^", "&", "*", "(", ")", "+", "=", "`",
				"~", ".", "<", ">", "/", "\"", ":", ";", "{",
				"}", "?" };
		
		for(String no : notAllowed) {
			if (name.contains(no)) {
				leader.sendMessage(ChatColor.RED + "You can't use those characters in your team name!");
				return;
			}
		}
		
		if(name.length() < 3 || name.length() > 12) {
			leader.sendMessage(ChatColor.RED + " Team name has to be atleast 3 letters but no more than 12!");
			return;
		}
		
		if(isPlayerInFaction(leader)) {
			leader.sendMessage(ChatColor.RED + "Must leave your current team before making a new one!");
			return;
		}
		
		if(factions.contains(getFactionByName(name))) {
			leader.sendMessage(ChatColor.RED + "That team already exists");
			return;
		}
		
		Faction faction = new Faction(leader.getUniqueId().toString(), name);
		
		factions.add(faction);
		
		leader.sendMessage(ChatColor.GOLD + "Successfully created the team " + ChatColor.RED + name);
		
	}
	
	public void disbandFaction(Player player) {
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a team! /t create [name]");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(!factions.contains(faction)) {
			return;
		}
		
	/*	if(faction.isRaidable()) {
			player.sendMessage(ChatColor.RED + "Can't disband a team while raidable!");
			return;
		}*/
		
		if(faction.getLeader().equalsIgnoreCase(player.getUniqueId().toString())) {
			faction.getMemberList().clear();
			faction.getCaptainList().clear();
			factions.remove(faction);
			
			Bukkit.broadcastMessage(ChatColor.RED + "The team " + ChatColor.YELLOW + faction.getName() + ChatColor.RED + " has been disbanded!");
		} else {
			player.sendMessage(ChatColor.RED + "This command is only for team Leaders");
		}
		
	}
	
	public boolean isPlayerInFaction(OfflinePlayer pl) {
		for(Faction faction : factions) {
			if(faction.getMemberList().contains(pl.getUniqueId().toString())) {
				return true;
			}
		}
		
		return false;
	}
	
	public Faction getFactionByName(String name) {
		for(Faction faction : factions) {
			if(faction.getName().equalsIgnoreCase(name)) {
				return faction;
			}
		}
		
		return null;
	}
	
	public Faction getFactionByPlayer(OfflinePlayer player) {
		for(Faction faction : factions) {
			if(faction.getMemberList().contains(player.getUniqueId().toString())) {
				return faction;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void invitePlayer(Player player, Player target) {
		
		if(isPlayerInFaction(player)) {
			Faction faction = getFactionByPlayer(player);
			
			if(faction.getLeader().equals(player.getUniqueId().toString()) || faction.getCaptainList().contains(player.getUniqueId().toString())) {
				
				if(faction.getMemberList().contains(target.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GOLD + " is already in your team");
					return;
				}
				
				if(faction.getInvitesList().contains(target.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GOLD + " has already been invited to your team");
					return;
				}
				
					
				faction.addInvite(target.getUniqueId().toString());

				target.sendMessage(ChatColor.GOLD + "Faction " + ChatColor.RED + faction.getName() + ChatColor.GOLD + " has invited you to join their faction!");

				for (Player p : Bukkit.getOnlinePlayers()) {

					if (faction.getMemberList().contains(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.DARK_GREEN + target.getName() + ChatColor.YELLOW + " has been invited to join your faction!");

					}

				}
					
				
			} else {
				player.sendMessage(ChatColor.RED + "Only team leader and captains can do this command");
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "You're not in a Team! /t create [name]");
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void revokeInvite(Player player, Player target) {
		if(isPlayerInFaction(player)) {
			Faction faction = getFactionByPlayer(player);
			
			if(faction.getLeader().equals(player.getUniqueId().toString()) || faction.getCaptainList().contains(player.getUniqueId().toString())) {
				
				if(faction.getMemberList().contains(target.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GOLD + " is already in your team");
					return;
				}
				
				if(!faction.getInvitesList().contains(target.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GOLD + " has not been invited to your team");
					return;
				}
				
					
				faction.removeInvite(target.getUniqueId().toString());

				target.sendMessage(ChatColor.GOLD + "Faction " + ChatColor.RED + faction.getName() + ChatColor.GOLD + " has revoked your to join their team!");

				for (Player p : Bukkit.getOnlinePlayers()) {

					if (faction.getMemberList().contains(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.RED + target.getName() + ChatColor.YELLOW + " has been un-invited to join your team!");

					}

				}
					
				
			} else {
				player.sendMessage(ChatColor.RED + "Only team leader and captains can do this command");
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "You're not in a Team! /t create [name]");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void joinFaction(Player player, String name) {
		
		if(isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You must leave your current team to join another one!");
			return;
		}
		
		Faction faction = getFactionByName(name);
		
		if(!factions.contains(faction)) {
			player.sendMessage(ChatColor.RED + "Can't find a team named " + name);
			return;
		}
		
		if(faction.getInvitesList().contains(player.getUniqueId().toString())) {

			if(faction.getMemberList().size() >= Factions.getInstance().getConfig().getInt("faction-limit")) {
				player.sendMessage(ChatColor.RED + "This team has reached it's limit of " + Factions.getInstance().getConfig().getInt("faction-limit") + " players");
				return;
			}
			
			
			faction.getMemberList().add(player.getUniqueId().toString());
			faction.getInvitesList().remove(player.getUniqueId().toString());
			
			faction.setMaxDtr(faction.getMaxDtr() + 0.75);
			
			if(faction.getMaxDtr() > 7.5) {
				faction.setMaxDtr(7.5);
			}
			
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (faction.getMemberList().contains(p.getUniqueId().toString())) {

					p.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.YELLOW + " has joined your team!");

				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "That team has not invited you!");
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void leaveFaction(Player player) {
		
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a team!");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(!factions.contains(faction)) {
			player.sendMessage(ChatColor.RED + "Couldn't find your team");
			return;
		}
		
		if(faction.getMemberList().contains(player.getUniqueId().toString())) {

			if(faction.getLeader().equals(player.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "You are the team Leader, please appoint a new leader before leaving");
				return;
			}
			
			faction.getMemberList().remove(player.getUniqueId().toString());
			
			faction.setMaxDtr(faction.getMaxDtr() - 0.75);
			
			if(faction.getMaxDtr() < 1.01) {
				faction.setMaxDtr(1.01);
			}
			
			if(faction.getCaptainList().contains(player.getUniqueId().toString())) {
				faction.getCaptainList().remove(player.getUniqueId().toString());
			}
			
			player.sendMessage(ChatColor.RED + "You have left the team " + ChatColor.YELLOW + faction.getName());
			
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (faction.getMemberList().contains(p.getUniqueId().toString())) {

					p.sendMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has left your team!");

				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "You are not in a team");
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void makeLeader(Player player, Player target) {
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a Team! /t create [name]");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(!faction.getLeader().equals(player.getUniqueId().toString())) {
			player.sendMessage(ChatColor.RED + "Only leaders can perform this command");
			return;
		}
		
		if(faction.getMemberList().contains(target.getUniqueId().toString())) {
			if(faction.getLeader().equals(target.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "You are already the leader, please choose another person");
				return;
			}
			
			faction.setLeader(target.getUniqueId().toString());
			
			if(faction.getCaptainList().contains(target.getUniqueId().toString())) {
				faction.removeCaptain(target.getUniqueId().toString());
			}
			
			if(!faction.getCaptainList().contains(player.getUniqueId().toString())) {
				faction.addCaptain(player.getUniqueId().toString());
			}
			
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (faction.getMemberList().contains(p.getUniqueId().toString())) {

					p.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has made " + target.getName() + " the new Leader");

				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "That player is not in your team");
		}
	}

	@SuppressWarnings("deprecation")
	public void makeCaptain(Player player, Player target) {
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a faction! /f create [name]");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(!faction.getLeader().equals(player.getUniqueId().toString())) {
			player.sendMessage(ChatColor.RED + "Only leaders can perform this command");
			return;
		}
		
		if(faction.getMemberList().contains(target.getUniqueId().toString())) {
			
			if(faction.getCaptainList().contains(target.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "That player is already a captain");
				return;
			}
		
			faction.addCaptain(target.getUniqueId().toString());
			
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (faction.getMemberList().contains(p.getUniqueId().toString())) {

					p.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has promoted " + target.getName() + " to Captain");

				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "That player is not in your faction");
		}
	}

	@SuppressWarnings("deprecation")
	public void demoteCaptain(Player player, Player target) {
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a Team! /t create [name]");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(!faction.getLeader().equals(player.getUniqueId().toString())) {
			player.sendMessage(ChatColor.RED + "Only leaders can perform this command");
			return;
		}
		
		if(faction.getMemberList().contains(target.getUniqueId().toString())) {
			
			if(!faction.getCaptainList().contains(target.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "That player is not a captain");
				return;
			}
		
			faction.removeCaptain(target.getUniqueId().toString());
			
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (faction.getMemberList().contains(p.getUniqueId().toString())) {

					p.sendMessage(ChatColor.RED + player.getName() + " has demoted " + target.getName() + " to Member");

				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "That player is not in your team");
		}
	}
	
	public void factionInfoByName(Player player, String name) {
		
		Faction faction = getFactionByName(name);
		
		if(!factions.contains(faction)) {
			player.sendMessage(ChatColor.RED + "Couldn't find a team by the name of " + ChatColor.YELLOW + name);
			return;
		}
		
		List<String> online = new ArrayList<>();
		List<String> offline = new ArrayList<>();
		List<String> captains = new ArrayList<>();
		
		String line = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------";
		
		player.sendMessage(line);
		
		for(String p : faction.getMemberList()) {
			OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(p));
			
			if(pl.isOnline()) {
				online.add(pl.getName());
			} else {
				offline.add(pl.getName());
			}
			
		}
		
		for(String p : faction.getMemberList()) {
			
			OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(p));
			
			if(faction.getCaptainList().contains(pl.getUniqueId().toString())) {
				captains.add(pl.getName());
			}
			
		}
		
		String onlinePlayers = "";
		
		if(online.size() > 0) {
			for(int i = 0; i < online.size(); i++) {
				onlinePlayers = ChatColor.GREEN + onlinePlayers + " " + online.get(i);
			}
		}
		
		String offlinePlayers = "";
		
		if(offline.size() > 0) {
			for(int i = 0; i < offline.size(); i++) {
				offlinePlayers = ChatColor.RED + offlinePlayers + " " + offline.get(i);
			}
			
		}
		
		String cap = "";
		
		if(captains.size() > 0) {
			for(int i = 0; i < captains.size(); i++) {
				onlinePlayers = ChatColor.YELLOW + cap + " " + captains.get(i);
			}
		}
		

		int total = online.size() + offline.size();
		
		player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + faction.getName() + ChatColor.GRAY + " [" + online.size() + "/" + total + "]");
		player.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.RED + Bukkit.getOfflinePlayer(UUID.fromString(faction.getLeader())).getName());
		//player.sendMessage(ChatColor.YELLOW + "Home: " + ChatColor.GRAY + "X:" + faction.getHome().getBlockX() + " - Z:" + faction.getHome().getBlockZ());
		player.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.GREEN + "$" + faction.getBalance());
		
		if(captains.size() > 0) {
			player.sendMessage(ChatColor.GRAY + "Captains: " + cap);
		}

		player.sendMessage(ChatColor.GRAY + "Members: " + onlinePlayers + offlinePlayers);
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		if(faction.getDtr() > 0) {
			player.sendMessage(ChatColor.BLUE + "DTR: " + ChatColor.GREEN + f.format(faction.getDtr()));
		} else {
			player.sendMessage(ChatColor.BLUE + "DTR: " + ChatColor.RED +  f.format(faction.getDtr()) + ChatColor.RED.toString() + ChatColor.BOLD + " [RAIDABLE]");
		}
		
		
		if(faction.getFreezeTime() > 0) {
			String time = toMMSS(faction.getFreezeTime());
			
			player.sendMessage(ChatColor.RED + "Freeze Time: " + ChatColor.GOLD + time);
		}
		player.sendMessage(line);
	}
	
	public void factionInfo(Player player, String name) {
		
		Faction faction = getFactionByName(name);
		
		if(!factions.contains(faction)) {
			player.sendMessage(ChatColor.RED + "Couldn't find a team by the name of " + ChatColor.YELLOW + name);
			return;
		}
		
		List<String> online = new ArrayList<>();
		List<String> offline = new ArrayList<>();
		List<String> captains = new ArrayList<>();
		
		String line = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------";
		
		player.sendMessage(line);
		
		for(String p : faction.getMemberList()) {
			OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(p));
			
			if(pl.isOnline()) {
				online.add(pl.getName());
			} else {
				offline.add(pl.getName());
			}
			
		}
		
		for(String p : faction.getMemberList()) {
			
			OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(p));
			
			if(faction.getCaptainList().contains(pl.getUniqueId().toString())) {
				captains.add(pl.getName());
			}
			
		}
		
		String onlinePlayers = "";
		
		if(online.size() > 0) {
			for(int i = 0; i < online.size(); i++) {
				onlinePlayers = ChatColor.GREEN + onlinePlayers + " " + online.get(i);
			}
		}
		
		String offlinePlayers = "";
		
		if(offline.size() > 0) {
			for(int i = 0; i < offline.size(); i++) {
				offlinePlayers = ChatColor.RED + offlinePlayers + " " + offline.get(i);
			}
			
		}
		
		String cap = "";
		
		if(captains.size() > 0) {
			for(int i = 0; i < captains.size(); i++) {
				onlinePlayers = ChatColor.YELLOW + cap + " " + captains.get(i);
			}
		}
		

		int total = online.size() + offline.size();
	
		player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + faction.getName() + ChatColor.GRAY + " [" + online.size() + "/" + total + "]");
		player.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.RED + Bukkit.getOfflinePlayer(UUID.fromString(faction.getLeader())).getName());
		
		if(faction.getHome() != null) {
			player.sendMessage(ChatColor.YELLOW + "Home: " + ChatColor.GRAY + "X:" + faction.getHome().getBlockX() + " - Z:" + faction.getHome().getBlockZ());
		} else {
			player.sendMessage(ChatColor.YELLOW + "Home: " + ChatColor.RED + "Not Set");
		}
		
		player.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.GREEN + "$" + faction.getBalance());
		
		if(captains.size() > 0) {
			player.sendMessage(ChatColor.GRAY + "Captains: " + cap);
		}

		player.sendMessage(ChatColor.GRAY + "Members: " + onlinePlayers + offlinePlayers);
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		if(faction.getDtr() > 0) {
			player.sendMessage(ChatColor.BLUE + "DTR: " + ChatColor.GREEN + f.format(faction.getDtr()));
		} else {
			player.sendMessage(ChatColor.BLUE + "DTR: " + ChatColor.RED +  f.format(faction.getDtr()) + ChatColor.RED.toString() + ChatColor.BOLD + " [RAIDABLE]");
		}
		player.sendMessage("");
		player.sendMessage(ChatColor.YELLOW + "[Announcement] " + ChatColor.LIGHT_PURPLE + faction.getMotd());
		
		if(faction.getFreezeTime() > 0) {
			String time = toMMSS(faction.getFreezeTime());
			
			player.sendMessage(ChatColor.RED + "Freeze Time: " + ChatColor.GOLD + time);
		}
		player.sendMessage(line);
	}
	
	@SuppressWarnings("deprecation")
	public void kickPlayer(Player player, OfflinePlayer target) {
		if(!isPlayerInFaction(player)) {
			player.sendMessage(ChatColor.RED + "You are not in a Team! /t create [name]");
			return;
		}
		
		Faction faction = getFactionByPlayer(player);
		
		if(faction.getLeader().equals(player.getUniqueId().toString())) {
		
			if(faction.getMemberList().contains(target.getUniqueId().toString())) {
				
				if(faction.getCaptainList().contains(target.getUniqueId().toString())) {
				
					faction.removeMember(target.getUniqueId().toString());
					faction.removeCaptain(target.getUniqueId().toString());
				
				} else {
					faction.removeMember(target.getUniqueId().toString());
				}
			
				faction.setMaxDtr(faction.getMaxDtr() - 0.75);
				
				if(faction.getMaxDtr() < 1.01) {
					faction.setMaxDtr(1.01);
				} 
				
				if(target.isOnline()) {
					((CommandSender) target).sendMessage(ChatColor.RED + "You have been kicked from the team " + ChatColor.YELLOW + faction.getName());
				}
				
				for (Player p : Bukkit.getOnlinePlayers()) {

					if (faction.getMemberList().contains(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has kicked " + ChatColor.DARK_RED + target.getName() + ChatColor.YELLOW + " from the team");

					}
				}
				
			} else {
				player.sendMessage(ChatColor.RED + "That player is not in your team");
			}
		
		} else if(faction.getCaptainList().contains(player.getUniqueId().toString())) {
			
			if(faction.getMemberList().contains(target.getUniqueId().toString())) {
				
				if(faction.getCaptainList().contains(target.getUniqueId().toString())) {
				
					player.sendMessage(ChatColor.RED + "Only a Leader can remove a Captain from the team");
				
				} else {
					faction.removeMember(target.getUniqueId().toString());
				}
			
				
				for (Player p : Bukkit.getOnlinePlayers()) {

					if (faction.getMemberList().contains(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has kicked " + ChatColor.DARK_RED + target.getName() + ChatColor.YELLOW + " from the team");

					}
				}
				
			} else {
				player.sendMessage(ChatColor.RED + "That player is not in your team");
			}
		}
		
	
	}
	
	public boolean insideClaim(Location loc) {
		for(Faction faction : factions) {
			
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
	
	public Faction getClaimByLocation(Location loc) {
		for(Faction faction : factions) {
			
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
	
	public boolean canClaim(Location loc1, Location loc2) {
		
		for(Faction faction : factions) {
			
			if(faction.getLoc1() != null & faction.getLoc2() != null) {
				Location facLoc1 = faction.getLoc1();
				Location facLoc2 = faction.getLoc2();
				
				//Faction values
				int facMax_x = Math.max(facLoc1.getBlockX(), facLoc2.getBlockX());
				int facMin_x = Math.min(facLoc1.getBlockX(), facLoc2.getBlockX());
				
				int facMax_z = Math.max(facLoc1.getBlockZ(), facLoc2.getBlockZ());
				int facMin_z = Math.min(facLoc1.getBlockZ(), facLoc2.getBlockZ());
				
				//Claim values
				int locMax_x = Math.max(loc1.getBlockX(), loc2.getBlockX());
				int locMin_x = Math.min(loc1.getBlockX(), loc2.getBlockX());
				
				int locMax_z = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
				int locMin_z = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
				
				
				for(int i = facMin_x; i < facMax_x + 1; i++) {
					for(int j = facMin_z; j < facMax_z + 1; j++) {
						//for(int k = 0; k < 256; k++) {
							Location loc = new Location(Bukkit.getWorld("world"), i, 0, j);
							
							//System.out.println("x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ());
							
							if((loc.getBlockX() <= locMax_x) && (loc.getBlockX() >= locMin_x)) {
								if((loc.getBlockZ() <= locMax_z) && (loc.getBlockZ() >= locMin_z)) {
									return false;
								}
							}
							
						//}
					}
				}
			}
			
			return true;
			
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void setMotd(Player player, String msg) {
		if(isPlayerInFaction(player)) {
			
			Faction faction = getFactionByPlayer(player);
			
			if(faction.getLeader().equals(player.getUniqueId().toString()) || faction.getCaptainList().contains(player.getUniqueId().toString())) {
				
				faction.setMotd(msg);
				
				
				for (Player p : Bukkit.getOnlinePlayers()) {

					if (faction.getMemberList().contains(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.YELLOW + "[Update] " + ChatColor.LIGHT_PURPLE + msg);

					}
				}
				
			}
			
		}
	}
	
	public void factionList() {
		
		TreeMap<Faction, Integer> fac = new TreeMap<>();
		
		for(Faction faction : factions) {
		
			fac.put(faction, 0);
			
			for(int i = 0; i < faction.getMemberList().size(); i++) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(faction.getMemberList().get(i)));
				
				if(player.isOnline()) {
					fac.put(faction, fac.get(faction) + 1);
				}
				
			}
			
		}
		
	}
	
	public static String toMMSS(long dura){
		int minute = (int)(dura / 60.0D);
		long second = dura - (minute * 60);
		String formatted = "";
		{
			if(minute < 10){
				formatted += "";
			}
			formatted += minute;
			formatted += ":";
			if(second < 10){
				formatted += "0";
			}
			formatted += second;
		}
		return formatted;
	}
	
	public static String formatDouble(double db) {
		DecimalFormat f = new DecimalFormat("#0.00");
		
		return f.format(db);
	}
	
	public ArrayList<Faction> getAllFactions() {
		return factions;
	}
}
