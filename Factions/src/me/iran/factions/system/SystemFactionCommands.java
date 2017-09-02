package me.iran.factions.system;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SystemFactionCommands implements CommandExecutor {

	private static HashMap<String, SystemClaim> claiming = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("systemfaction")) {
			
			if(args.length < 1) {
				//send msg
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length < 2) {
					//send msg
					return true;
				}
				
				SystemFactionManager.getManager().createFaction(player, args[1]);
			}
			
			if(args[0].equalsIgnoreCase("claim")) {
				if(args.length < 2) {
					//send msg
					return true;
				}
				
				if(!SystemFactionManager.getManager().doesFactionExist(args[1])) {
					//send msg
					return true;
				}
				
				claiming.put(player.getName(), new SystemClaim(null, null, SystemFactionManager.getManager().getFactionByName(args[1])));
			
				ItemStack wand = new ItemStack(Material.STICK);
				ItemMeta wMeta = wand.getItemMeta();
				
				wMeta.setDisplayName(ChatColor.GOLD + "System Claiming Wand");
				wMeta.setLore(Arrays.asList("", ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Left Click to set Corner 1",
						ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Right Click to set Corner 2",
						ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Shift and Right Click to the Claim the land",
						"", "",
						ChatColor.RED.toString() + ChatColor.BOLD + "[Note] " + ChatColor.AQUA + "You may use a regular stick to claim"));
				
				wand.setItemMeta(wMeta);
				
				player.getInventory().addItem(wand);
			}
			
		}
		
		return true;
	}
	
	public static HashMap<String, SystemClaim> getClaiming() {
		return claiming;
	}

	
}
