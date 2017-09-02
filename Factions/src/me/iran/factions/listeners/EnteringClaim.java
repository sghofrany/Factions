package me.iran.factions.listeners;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iran.factions.Factions;
import me.iran.factions.customlisteners.EnterClaimEvent;
import me.iran.factions.customlisteners.LeaveClaimEvent;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import net.md_5.bungee.api.ChatColor;

public class EnteringClaim implements Listener {

	Factions plugin;

	public EnteringClaim (Factions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEnter(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if(!FactionManager.getManager().insideClaim(event.getFrom()) && FactionManager.getManager().insideClaim(event.getTo())) {
			Bukkit.getServer().getPluginManager().callEvent(new EnterClaimEvent(player, FactionManager.getManager().getClaimByLocation(event.getTo())));
		}

	}

	@EventHandler
	public void onLeave(PlayerMoveEvent event) {
	
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().insideClaim(event.getFrom()) && !FactionManager.getManager().insideClaim(event.getTo())) {
			Bukkit.getServer().getPluginManager().callEvent(new LeaveClaimEvent(player, FactionManager.getManager().getClaimByLocation(event.getFrom())));
		}
		
	}
	
	@EventHandler
	public void enterClaim(EnterClaimEvent event) {
		
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			if(faction.getName().equalsIgnoreCase(event.getFaction().getName())) {
				
				player.sendMessage(ChatColor.YELLOW + "You have entered the claim of " + ChatColor.GREEN + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
				
			} else {
				player.sendMessage(ChatColor.YELLOW + "You have entered the claim of " + ChatColor.RED + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
			}
			
		} else {
			player.sendMessage(ChatColor.YELLOW + "You have entered the claim of " + ChatColor.RED + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
		}
		
	}
	
	@EventHandler
	public void leaveClaim(LeaveClaimEvent event) {
		
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			if(faction.getName().equalsIgnoreCase(event.getFaction().getName())) {
				
				player.sendMessage(ChatColor.YELLOW + "You have left the claim of " + ChatColor.GREEN + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
				
			} else {
				player.sendMessage(ChatColor.YELLOW + "You have left the claim of " + ChatColor.RED + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
			}
			
		} else {
			player.sendMessage(ChatColor.YELLOW + "You have left the claim of " + ChatColor.RED + event.getFaction().getName() + ChatColor.GRAY + " (" + formatDouble(event.getFaction().getDtr()) + ")");
		}
		
	}
	
	public static String formatDouble(double db) {
		DecimalFormat f = new DecimalFormat("#0.00");
		
		return f.format(db);
	}
	
	
}
