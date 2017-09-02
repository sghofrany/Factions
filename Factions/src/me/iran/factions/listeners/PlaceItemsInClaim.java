package me.iran.factions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;

public class PlaceItemsInClaim implements Listener {

	Factions plugin;
	
	public PlaceItemsInClaim (Factions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().insideClaim(event.getBlock().getLocation())) {
			
			Faction blockFac = FactionManager.getManager().getClaimByLocation(event.getBlock().getLocation());
			

			if(FactionManager.getManager().isPlayerInFaction(player)) {
				Faction faction = FactionManager.getManager().getFactionByPlayer(player);
				
				if(!blockFac.getName().equalsIgnoreCase(faction.getName()) && !blockFac.isRaidable()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Can't place blocks in the territory of " + ChatColor.LIGHT_PURPLE + blockFac.getName());
				}
				
			} else {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Can't place blocks in the territory of " + ChatColor.LIGHT_PURPLE + blockFac.getName());
			}
			
		}
	}
}
