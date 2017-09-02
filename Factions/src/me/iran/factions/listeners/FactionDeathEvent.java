package me.iran.factions.listeners;

import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FactionDeathEvent implements Listener {

	Factions plugin;
	
	public FactionDeathEvent (Factions plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			faction.setDtr(faction.getDtr() - 1.0);
			
			if(faction.getDtr() < 0) {
				faction.setRaidable(true);
			}
			
			faction.setFreezeTime(10);
			faction.setFrozen(true);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(faction.getMemberList().contains(p.getUniqueId().toString())) {
					FactionManager.getManager();
					p.sendMessage(ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has died " + ChatColor.YELLOW + "(DTR: " + FactionManager.formatDouble(faction.getDtr()) + ")");
				}
			}
			
		}
	}
	
}
