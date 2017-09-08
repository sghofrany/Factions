package me.iran.factions.listeners;

import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FactionDeathEvent implements Listener {

	private Utils utils = new Utils();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		if(FactionManager.getManager().isPlayerInFaction(player)) {
			Faction faction = FactionManager.getManager().getFactionByPlayer(player);
			
			faction.setDtr(faction.getDtr() - 1.0);
			
			faction.setFreezeTime(10);
			faction.setFrozen(true);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(faction.getMemberList().contains(p.getUniqueId().toString())) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getConfig().getString("death-message").replace("%dtr%", utils.formatDouble(faction.getDtr())).replace("%player%", player.getName())));
				}
			}
			
		}
	}
	
}
