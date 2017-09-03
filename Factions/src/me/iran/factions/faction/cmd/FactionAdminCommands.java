package me.iran.factions.faction.cmd;

import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import me.iran.factions.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionAdminCommands implements CommandExecutor {
	
	private Utils utils = new Utils();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("factionadmin")) {
			if(!player.hasPermission("faction.admin")) {
				player.sendMessage(ChatColor.RED + "No Permission.");
				return true;
			}
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.YELLOW + "/fa setdtr <faction> <dtr>");
				player.sendMessage(ChatColor.YELLOW + "/fa setfreezetime <faction> <time in seconds>");
				player.sendMessage(ChatColor.YELLOW + "/fa sethome <faction>");
				player.sendMessage(ChatColor.YELLOW + "/fa kick <faction> <player>");
				player.sendMessage(ChatColor.YELLOW + "/fa invite <faction> <player>");
				player.sendMessage(ChatColor.YELLOW + "/fa revoke|uninivte <faction> <player>");
				player.sendMessage(ChatColor.YELLOW + "/fa disband <faction>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setdtr")) {
				if(args.length < 3) {
					player.sendMessage(ChatColor.RED + "/fa setdtr <faction> <dtr>");
					return true;
				}
				
				try {
					
					if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
						Faction faction = FactionManager.getManager().getFactionByName(args[1]);
						
						double d = Double.parseDouble(args[2]);
						
						if(d < 0) {
							faction.setRaidable(true);
						}

						faction.setDtr(d);
						
						player.sendMessage(ChatColor.RED + "You have set the DTR of faction " + ChatColor.YELLOW + faction.getName() + " " + ChatColor.RED + utils.formatDouble(d));
						
					}
					
					
				} catch(Exception e) {
					player.sendMessage(ChatColor.RED + "Incorrect format /fa setdtr <faction> <dtr>");
				}
				

				
			}
			
			if(args[0].equalsIgnoreCase("setfreezetime")) {
				if(args.length < 3) {
					player.sendMessage(ChatColor.RED + "/fa setfreezetime <faction> <time in seconds>");
					return true;
				}
				
				try {
					
					if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
						Faction faction = FactionManager.getManager().getFactionByName(args[1]);
						
						int t = Integer.parseInt(args[2]);
						
						if(t < 0) {
							player.sendMessage(ChatColor.RED + "Can't set the freezetime to negative");
							return true;
						}
						
						faction.setFreezeTime(t);
						faction.setFrozen(true);
						
						player.sendMessage(ChatColor.RED + "You have set the Freeze Time of faction " + ChatColor.YELLOW + faction.getName() + " " + ChatColor.RED + utils.toMMSS(t));
						
					}
				} catch(Exception e) {
					player.sendMessage(ChatColor.RED + "Incorrect format /fa setfreezetime <faction> <time in seconds>");
				}

				
			}
			
			if(args[0].equalsIgnoreCase("sethome")) {
				if(args.length < 2) {
					player.sendMessage(ChatColor.RED + "/fa sethome <faction>");
					return true;
				}
				
				
				if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
					
					Faction faction = FactionManager.getManager().getFactionByName(args[1]);
					
					if(FactionManager.getManager().insideClaim(player.getLocation())) {
						if(FactionManager.getManager().getClaimByLocation(player.getLocation()).getName().equals(faction.getName())) {
							faction.setHome(player.getLocation());
							
							for(Player p : Bukkit.getOnlinePlayers()) {
								if(faction.getMemberList().contains(p.getUniqueId().toString())) {
									
									p.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.YELLOW + " has set the Faction Home! user /f home");
									
									player.sendMessage(ChatColor.RED + "You have set the faction home of " + ChatColor.YELLOW + faction.getName());
									
									
								}
							}
						} else {
							player.sendMessage(ChatColor.RED + "Can't place this factions f home inside of another factions claim!");
						}
					}  else {
						player.sendMessage(ChatColor.RED + "Can't place this factions f home inside of another factions claim!");
					}
					
				}
				
			}
			
			if(args[0].equalsIgnoreCase("kick")) {
				if(args.length < 3) {
					player.sendMessage(ChatColor.RED + "/fa kick <faction> <player>");
					return true;
				}
				
				if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
					Faction faction = FactionManager.getManager().getFactionByName(args[1]);
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
					
					if(faction.getMemberList().contains(target.getUniqueId().toString())) {
						
						if(faction.getLeader().equals(target.getUniqueId().toString())) {
							player.sendMessage(ChatColor.RED + "You are trying to kick the leader, you must pick a new leader /fa leader <faction> <player>");
							return true;
						}
						
						if(faction.getCaptainList().contains(target.getUniqueId().toString())) {
							faction.getCaptainList().remove(target.getUniqueId().toString());
						}
						
						faction.getMemberList().remove(target.getUniqueId().toString());
						
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(faction.getMemberList().contains(p.getUniqueId().toString())) {
								
								p.sendMessage(ChatColor.RED + target.getName() + ChatColor.YELLOW + " has been force kicked by " + player.getDisplayName());
								
							}
						}
						
						player.sendMessage(ChatColor.GOLD + "Kicked " + target.getName() + " from the faction " + faction.getName());
					}
					
				}
				
			}
			
			if(args[0].equalsIgnoreCase("invite")) {
				if(args.length < 3) {
					player.sendMessage(ChatColor.RED + "/fa invite <faction> <player>");
					return true;
				}
				
				if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
					Faction faction = FactionManager.getManager().getFactionByName(args[1]);
					
					Player target = Bukkit.getPlayer(args[2]);
					
					if(!faction.getMemberList().contains(target.getUniqueId().toString()) && !faction.getInvitesList().contains(target.getUniqueId().toString())) {
						
						faction.getInvitesList().add(target.getUniqueId().toString());
						
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(faction.getMemberList().contains(p.getUniqueId().toString())) {
								
								p.sendMessage(ChatColor.DARK_GREEN + target.getName() + ChatColor.YELLOW + " has been force invited by " + player.getDisplayName());
								
							}
						}
						
						player.sendMessage(ChatColor.GOLD + "Invited " + target.getName() + " to the faction " + faction.getName());
						target.sendMessage(ChatColor.YELLOW + "You have been invited to join " + ChatColor.DARK_GREEN + faction.getName());
					} else {
						player.sendMessage(ChatColor.RED + "Make sure that this person has not been invited or is currently in that faction");
					}
					
				}
				
			}
			
			if(args[0].equalsIgnoreCase("revoke") || args[0].equalsIgnoreCase("uninvite")) {
				if(args.length < 3) {
					player.sendMessage(ChatColor.RED + "/fa revoke|uninvite <player> <faction>");
					return true;
				}
				
				if(FactionManager.getManager().getAllFactions().contains(FactionManager.getManager().getFactionByName(args[1]))) {
					Faction faction = FactionManager.getManager().getFactionByName(args[1]);
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
					
					if(faction.getInvitesList().contains(target.getUniqueId().toString())) {
						
						faction.getInvitesList().remove(target.getUniqueId().toString());
						
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(faction.getMemberList().contains(p.getUniqueId().toString())) {
								
								p.sendMessage(ChatColor.DARK_GREEN + target.getName() + ChatColor.YELLOW + " has been force revoked by " + player.getDisplayName());
								
							}
						}
						
						player.sendMessage(ChatColor.GOLD + "Revoked " + target.getName() + " to the faction " + faction.getName());
					} else {
						player.sendMessage(ChatColor.RED + "Make sure that this person has been invited");
					}
					
				}
				
			}
			
			if (args[0].equalsIgnoreCase("disband")) {
				if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "/fa disband <faction>");
					return true;
				}

				if (FactionManager.getManager().getAllFactions()
						.contains(FactionManager.getManager().getFactionByName(args[1]))) {
					Faction faction = FactionManager.getManager().getFactionByName(args[1]);

					Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName()
							+ " has force disbanded the faction " + ChatColor.GOLD.toString() + ChatColor.BOLD
							+ faction.getName());

					faction.getMemberList().clear();
					faction.getCaptainList().clear();
					faction.getInvitesList().clear();

					FactionManager.getManager().getAllFactions().remove(faction);

				}

			}
			
			
		}
		
		return true;
	}

}
