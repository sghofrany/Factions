package me.iran.factions.listeners;

import java.util.List;
import me.iran.factions.Factions;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractWithItemsInClaim implements Listener {
	Factions plugin;

	public InteractWithItemsInClaim(Factions plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getAction() == null) {
			return;
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			BlockState block = event.getClickedBlock().getState();
			
			if (FactionManager.getManager().isPlayerInFaction(player)) {
				
				Faction faction = FactionManager.getManager().getFactionByPlayer(player);
				
				if (FactionManager.getManager().insideClaim(block.getLocation())) {
					
					if (!FactionManager.getManager().getClaimByLocation(block.getLocation()).getName().equalsIgnoreCase(faction.getName())) {
						
						Faction blockFac = FactionManager.getManager().getClaimByLocation(block.getLocation());

						List<String> materialList = Factions.getInstance().getConfig().getStringList("interact-in-claim");
						for (String s : materialList) {

							Material material = Material.getMaterial(s);
							
							if (block.getType() == material) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "Can't touch " + ChatColor.LIGHT_PURPLE+ s.replace("_", " ").toLowerCase() + ChatColor.RED + " inside the claim of " + ChatColor.YELLOW + blockFac.getName());
								return;
							}
						}
					}
				}
			
			} else {
				Faction blockFac = FactionManager.getManager().getClaimByLocation(block.getLocation());

				List<String> materialList = Factions.getInstance().getConfig().getStringList("interact-in-claim");
				
				for (String s : materialList) {

					Material material = Material.getMaterial(s);
					
					if (block.getType() == material) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "Can't touch " + ChatColor.LIGHT_PURPLE+ s.replace("_", " ").toLowerCase() + ChatColor.RED + " inside the claim of " + ChatColor.YELLOW + blockFac.getName());
						return;
					}
				}
			}
		}
	}
}
