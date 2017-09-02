package me.iran.factions.listeners;

import java.util.HashSet;
import java.util.Set;

import me.iran.factions.Factions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPTimer implements Listener {

	Factions plugin;
	
	public PvPTimer(Factions plugin) {
		this.plugin = plugin;
	}
	
	private Set<String> pvp = new HashSet<String>();
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			//Player hit = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			
			if(!pvp.contains(damager.getName())) {
				pvp.add(damager.getName());
			} else {
				pvp.remove(damager.getName());
			}
			
		}
		
	}
	
	public Set<String> getTimer() {
		return pvp;
	}
	
}
