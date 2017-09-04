package me.iran.factions.listeners;

import me.iran.factions.faction.cmd.FactionCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveWhileTeleporting implements Listener {
	
	private FactionCommands cmd = new FactionCommands();
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(cmd.getHome().containsKey(player.getName())) {
			if(!(event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())) {
				cmd.getHome().remove(player.getName());
				player.sendMessage(ChatColor.RED + "Teleportation canceled!");
			}
		}
	}
}
