package me.iran.factions.listeners;

import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionEvents implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		event.setJoinMessage(null);
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(faction.getMemberList().contains(p.getUniqueId().toString())) {
					p.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.YELLOW + " has logged in");
				}
			}
			
			player.sendMessage(ChatColor.YELLOW + "[Announcement] " + ChatColor.LIGHT_PURPLE + faction.getMotd());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		event.setQuitMessage(null);
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(faction.getMemberList().contains(p.getUniqueId().toString())) {
					p.sendMessage(ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW + " is now offline");
				}
			}
			
		}
	}
}
