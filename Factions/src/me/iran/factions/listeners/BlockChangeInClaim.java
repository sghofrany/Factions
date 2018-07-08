package me.iran.factions.listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.system.SystemFactionManager;

public class BlockChangeInClaim implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().insideClaim(event.getBlock().getLocation())) {
			
			Faction blockFac = FactionManager.getManager().getClaimByLocation(event.getBlock().getLocation());
			

			if(FactionManager.getManager().isPlayerInFaction(player)) {
				Faction faction = FactionManager.getManager().getFactionByPlayer(player);
				
				if(!blockFac.getName().equalsIgnoreCase(faction.getName()) && !blockFac.isRaidable()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Can't break in the territory of " + ChatColor.LIGHT_PURPLE + blockFac.getName());
				}
				
			} else {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Can't break in the territory of " + ChatColor.LIGHT_PURPLE + blockFac.getName());
			}
		}
		
		if(SystemFactionManager.getManager().isInsideClaim(event.getBlock().getLocation())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "Can't break in the territory of " + ChatColor.LIGHT_PURPLE + SystemFactionManager.getManager().getFactionByLocation(event.getBlock().getLocation()).getName());
		}
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {

		List<Block> destroyed = event.blockList();
        
		Iterator<Block> it = destroyed.iterator();
   
		while (it.hasNext()) {
			Block block = it.next();
			if (FactionManager.getManager().insideClaim(block.getLocation()))
				it.remove();
		}
		
		while (it.hasNext()) {
			Block block = it.next();
			if (SystemFactionManager.getManager().isInsideClaim(block.getLocation()))
				it.remove();
		}

	}
	
}
