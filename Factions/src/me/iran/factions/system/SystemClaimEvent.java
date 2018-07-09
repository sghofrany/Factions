package me.iran.factions.system;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SystemClaimEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(event.getAction() == null) {
			return;
		}
		
		if(player.getItemInHand().getType() == null) {
			return;
		}
		
		if(!player.getItemInHand().hasItemMeta()) {
			return;
		}
		
		if(!player.getItemInHand().getItemMeta().hasDisplayName()) {
			return;
		}
		
		if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.getItemInHand().getType() == Material.STICK && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "System Claiming Wand")) {
			if(SystemFactionCommands.getClaiming().containsKey(player.getName()) && player.hasPermission("factions.staff")) {
				SystemClaim claim = SystemFactionCommands.getClaiming().get(player.getName());
				
				claim.setLoc1(event.getClickedBlock().getLocation());
				player.sendMessage(ChatColor.GREEN + "Set location one for " + claim.getFaction().getName() + "'s claim");
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.STICK && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "System Claiming Wand")) {
			if(SystemFactionCommands.getClaiming().containsKey(player.getName()) && player.hasPermission("factions.staff")) {
				SystemClaim claim = SystemFactionCommands.getClaiming().get(player.getName());
				
				claim.setLoc2(event.getClickedBlock().getLocation());
				player.sendMessage(ChatColor.GREEN + "Set location two for " + claim.getFaction().getName() + "'s claim");
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.STICK && player.isSneaking() && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "System Claiming Wand")) {
			if(SystemFactionCommands.getClaiming().containsKey(player.getName()) && player.hasPermission("factions.staff")) {
				SystemClaim claim = SystemFactionCommands.getClaiming().get(player.getName());
				
				if(claim.getLoc1() != null && claim.getLoc2() != null) {
					claim.getFaction().setLoc1(claim.getLoc1());
					claim.getFaction().setLoc2(claim.getLoc2());
					player.sendMessage(ChatColor.GREEN + "You have claimed land for the system faction " + ChatColor.BLUE + claim.getFaction().getName());
					SystemFactionCommands.getClaiming().remove(player.getName());
					player.setItemInHand(null);
				} else {
					player.sendMessage(ChatColor.RED + " have not selected two locations");
				}
				
			}
		}
		
	}
	
}
