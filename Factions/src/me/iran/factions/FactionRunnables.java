package me.iran.factions;

import me.iran.factions.faction.ClaimEvent;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.faction.cmd.FactionCommands;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FactionRunnables extends BukkitRunnable {

	Factions plugin;
	
	public FactionRunnables(Factions plugin) {
		this.plugin = plugin;
	}
	
	ClaimEvent claimEvent = new ClaimEvent(plugin);
	FactionCommands cmd = new FactionCommands(plugin);
	
	@SuppressWarnings("deprecation")
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(ClaimEvent.getMap().contains(player.getName())) {
				claimEvent.factionMap(player);
			}
			
			if(cmd.getHome().containsKey(player.getName())) {
				
				
				cmd.getHome().put(player.getName(), cmd.getHome().get(player.getName()) - 1);
				
				int timer = cmd.getHome().get(player.getName());
				
				player.sendMessage(ChatColor.GOLD + "Teleporting home in " + ChatColor.RED + timer + ChatColor.GOLD + " seconds");
				
				if(timer == 0) {
					cmd.getHome().remove(player.getName());
					player.teleport(FactionManager.getManager().getFactionByPlayer(player).getHome());
					player.sendMessage(ChatColor.GOLD + "Teleporting Home...");
				}
				
			}
			
		}
		
		for (Faction faction : FactionManager.getManager().getAllFactions()) {

			if (faction.getFreezeTime() > 0 && faction.isFrozen()) {
				faction.setFreezeTime(faction.getFreezeTime() - 1);

				if (faction.getFreezeTime() <= 0) {
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						if(faction.getMemberList().contains(p.getUniqueId().toString())) {
							p.sendMessage(ChatColor.DARK_AQUA + "Your faction is now regenerating DTR");
						}
					}
					
					faction.setFreezeTime(0);
				}

			}
			
			if (faction.getFreezeTime() <= 0) {

				if (faction.isRaidable()) {
					
					faction.setDtr(faction.getDtr() + 0.001);
					
					if(faction.getDtr() >= 0) {
						faction.setRaidable(false);
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(faction.getMemberList().contains(p.getUniqueId().toString())) {
								p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Your faction is no longer Raidable!");
							}
						}
					}
					
				} else if (faction.getDtr() < faction.getMaxDtr()) {
					
					faction.setDtr(faction.getDtr() + 0.001);
					
					if (faction.getDtr() < faction.getMaxDtr()) {
						if (faction.getDtr() >= faction.getMaxDtr()) {
							faction.setDtr(faction.getMaxDtr());
						}
					}
				} else if(faction.getDtr() > faction.getMaxDtr()) {
					faction.setDtr(faction.getMaxDtr());
				}

			}
			
		}
	}
}
