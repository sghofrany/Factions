package me.iran.factions.listeners;

import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import net.md_5.bungee.api.ChatColor;

import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockChangeInClaim implements Listener {

	Factions plugin;
	
	public BlockChangeInClaim (Factions plugin) {
		this.plugin = plugin;
	}
	
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
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {

		List<Block> destroyed = event.blockList();
        
		Iterator<Block> it = destroyed.iterator();
        
		if (event.getEntity() instanceof TNTPrimed) {
			while (it.hasNext()) {
				Block block = it.next();
				if (FactionManager.getManager().insideClaim(block.getLocation()))
					it.remove();
			}
		}

	}
	
}
