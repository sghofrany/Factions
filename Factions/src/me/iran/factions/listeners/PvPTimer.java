package me.iran.factions.listeners;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.iran.factions.Factions;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.system.SystemFactionManager;

public class PvPTimer implements Listener {

	private static HashMap<String, Integer> pvp = new HashMap<>();
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			Player hit = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			
			if(FactionManager.getManager().isSameFaction(hit, damager)) {
				damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("same-faction").replace("%player%", hit.getName())));
				event.setCancelled(true);
				return;
			}
			
			if(SystemFactionManager.getManager().isInsideClaim(hit.getLocation())) {
				if(!SystemFactionManager.getManager().getFactionByLocation(hit.getLocation()).isDeathban()) {
					damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("other-inside-safezone").replace("%player%", hit.getName())));
					event.setCancelled(true);
					return;
				}
			} else if(SystemFactionManager.getManager().isInsideClaim(damager.getLocation())) {
				if(!SystemFactionManager.getManager().getFactionByLocation(damager.getLocation()).isDeathban()) {
					damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("self-inside-safezone").replace("%player%", hit.getName())));
					event.setCancelled(true);
					return;
				}
			}
			
			pvp.put(damager.getName(), Factions.getInstance().getConfig().getInt("pvp-timer"));
			pvp.put(hit.getName(), Factions.getInstance().getConfig().getInt("pvp-timer"));
			
			hit.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("pvp-timer-message-hit")
					.replace("%timer%", Factions.getInstance().getConfig().getInt("pvp-timer") + "").replace("%player%", damager.getName())));
			damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("pvp-timer-message-damager")
					.replace("%timer%", Factions.getInstance().getConfig().getInt("pvp-timer") + "").replace("%player%", hit.getName())));

		}
		
	}
	
	public HashMap<String, Integer> getTimer() {
		return pvp;
	}
	
}
