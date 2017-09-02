package me.iran.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.faction.cmd.FactionCommands;

public class FactionChat implements Listener {

	Factions plugin;
	
	public FactionChat(Factions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		String msg = event.getMessage();
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			
			if(FactionCommands.getChat().contains(player.getName())) {
				event.setCancelled(true);
				
				Faction faction = FactionManager.getManager().getFactionByPlayer(player);
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					
					if(faction.getMemberList().contains(p.getUniqueId().toString())) {
						p.sendMessage(ChatColor.DARK_AQUA + "[" + faction.getName() + "] " + player.getName() + ": " + ChatColor.YELLOW + msg);
					}
					
				}
				
			}
			
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if (FactionManager.getManager().isPlayerInFaction(player)) {

			if (FactionCommands.getChat().contains(player.getName())) {

				FactionCommands.getChat().remove(player.getName());
			}

		}
	}
	
}
