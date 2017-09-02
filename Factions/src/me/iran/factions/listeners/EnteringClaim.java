package me.iran.factions.listeners;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iran.factions.Factions;
import me.iran.factions.customlisteners.EnterClaimEvent;
import me.iran.factions.customlisteners.EnterSystemFactionEvent;
import me.iran.factions.customlisteners.LeaveClaimEvent;
import me.iran.factions.customlisteners.LeaveSystemFactionEvent;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.system.SystemFactionManager;
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

		if(!SystemFactionManager.getManager().isInsideClaim(event.getFrom()) && SystemFactionManager.getManager().isInsideClaim(event.getTo())) {
			Bukkit.getServer().getPluginManager().callEvent(new EnterSystemFactionEvent(player, SystemFactionManager.getManager().getFactionByLocation(event.getTo())));
		}
		
	}

	@EventHandler
	public void onLeave(PlayerMoveEvent event) {
	
		Player player = event.getPlayer();
		
		if(FactionManager.getManager().insideClaim(event.getFrom()) && !FactionManager.getManager().insideClaim(event.getTo())) {
			Bukkit.getServer().getPluginManager().callEvent(new LeaveClaimEvent(player, FactionManager.getManager().getClaimByLocation(event.getFrom())));
		}
		
		if(SystemFactionManager.getManager().isInsideClaim(event.getFrom()) && !SystemFactionManager.getManager().isInsideClaim(event.getTo())) {
			Bukkit.getServer().getPluginManager().callEvent(new LeaveSystemFactionEvent(player, SystemFactionManager.getManager().getFactionByLocation(event.getFrom())));
		}
		
	}
	
	@EventHandler
	public void enterSystemClaim(EnterSystemFactionEvent event) {
		Player player = event.getPlayer();
		
		if(event.getFaction().isDeathban()) {
			player.sendMessage(ChatColor.YELLOW + "Entering " + event.getFaction().getColor() + event.getFaction().getName() + ChatColor.YELLOW + "'s Claim " + ChatColor.GRAY + "(" + ChatColor.RED + "Deathban" + ChatColor.GRAY + ")");
		} else {
			player.sendMessage(ChatColor.YELLOW + "Entering " + event.getFaction().getColor() + event.getFaction().getName() + ChatColor.YELLOW + "'s Claim " + ChatColor.GRAY + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GRAY + ")");
		}
		
	}
	
	@EventHandler
	public void leaveSystemClaim(LeaveSystemFactionEvent event) {
		Player player = event.getPlayer();
		
		if(event.getFaction().isDeathban()) {
			player.sendMessage(ChatColor.YELLOW + "Leaving " + event.getFaction().getColor() + event.getFaction().getName() + ChatColor.YELLOW + "'s Claim " + ChatColor.GRAY + "(" + ChatColor.RED + "Deathban" + ChatColor.GRAY + ")");
		} else {
			player.sendMessage(ChatColor.YELLOW + "Leaving " + event.getFaction().getColor() + event.getFaction().getName() + ChatColor.YELLOW + "'s Claim " + ChatColor.GRAY + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GRAY + ")");
		}
		
	}
	
	@EventHandler
	public void enterFactionClaim(EnterClaimEvent event) {
		
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
	public void leaveFactionClaim(LeaveClaimEvent event) {
		
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
