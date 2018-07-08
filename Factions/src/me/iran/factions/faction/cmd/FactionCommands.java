package me.iran.factions.faction.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.iran.factions.Factions;
import me.iran.factions.faction.Claim;
import me.iran.factions.faction.ClaimEvent;
import me.iran.factions.faction.Faction;
import me.iran.factions.faction.FactionManager;
import net.milkbowl.vault.economy.EconomyResponse;

public class FactionCommands implements CommandExecutor {

	private static HashMap<String, Integer> home = new HashMap<String, Integer>();
	
	private static ArrayList<String> chat = new ArrayList<>();
	
	private ClaimEvent claimEvent = new ClaimEvent();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("faction")) {
			
			if(args.length < 1) {
				factionHelp(player);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f create [name]");
					return true;
				}
				
				FactionManager.getManager().createFaction(player, args[1]);
			}
			
			if(args[0].equalsIgnoreCase("disband")) {
				FactionManager.getManager().disbandFaction(player);
			}
			
			if(args[0].equalsIgnoreCase("invite")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f invite [player]");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target == player) {
					player.sendMessage(ChatColor.RED + "You can't invite yourself to a faction");
					return true;
				}
				
				FactionManager.getManager().invitePlayer(player, target);
			}
			
			if(args[0].equalsIgnoreCase("revoke") || args[0].equalsIgnoreCase("uninvite")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f revoke|uninvite [player]");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target == player) {
					player.sendMessage(ChatColor.RED + "You can't do that.");
					return true;
				}
				
				FactionManager.getManager().revokeInvite(player, target);
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f join [team]");
					return true;
				}
				
				FactionManager.getManager().joinFaction(player, args[1]);
			}
			
			if(args[0].equalsIgnoreCase("leave")) {
				FactionManager.getManager().leaveFaction(player);
			}
			
			if(args[0].equalsIgnoreCase("kick")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f kick [player]");
					return true;
				}
				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target.getUniqueId().toString().equals(player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + "You can't do that.");
					return true;
				}
				
				FactionManager.getManager().kickPlayer(player, target);
			}
		
			if(args[0].equalsIgnoreCase("leader")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f leader [player]");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target == player) {
					player.sendMessage(ChatColor.RED + "You can't do that.");
					return true;
				}
				
				FactionManager.getManager().makeLeader(player, target);
			}
			
			if(args[0].equalsIgnoreCase("promote")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f promote [player]");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target == player) {
					player.sendMessage(ChatColor.RED + "You can't do that.");
					return true;
				}
				
				FactionManager.getManager().makeCaptain(player, target);
			}
			
			if(args[0].equalsIgnoreCase("demote")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/f promote [player]");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find player " + ChatColor.YELLOW + args[1]);
					return true;
				}
				
				if(target == player) {
					player.sendMessage(ChatColor.RED + "You can't do that.");
					return true;
				}
				
				FactionManager.getManager().demoteCaptain(player, target);
			}
			
			if(args[0].equalsIgnoreCase("claim")) {
				
				if(!FactionManager.getManager().isPlayerInFaction(player)) {
					player.sendMessage(ChatColor.RED + "Must be in a faction to claim land! /f create [name]");
					return true;
				}
				
				if(FactionManager.getManager().isPlayerInFaction(player) && !FactionManager.getManager().getFactionByPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + "Only faction leaders can do this command");
					return true;
				}
				
				if(FactionManager.getManager().getFactionByPlayer(player).getLoc1() != null && FactionManager.getManager().getFactionByPlayer(player).getLoc1() != null) {
					player.sendMessage(ChatColor.RED + "Your faction already has a claim, you must unclaim your current land first!");
					return true;
				}
				
				if(claimEvent.getClaim().containsKey(player.getName())) {
					player.sendMessage(ChatColor.GOLD + "Seems like you are already in Claiming Mode, use a stick to pick your two corners and then Shift and Right Click the Air");
					return true;
				}
				
				claimEvent.getClaim().put(player.getName(), new Claim(null, null, FactionManager.getManager().getFactionByPlayer(player)));
				
				ItemStack wand = new ItemStack(Material.STICK);
				ItemMeta wMeta = wand.getItemMeta();
				
				wMeta.setDisplayName(ChatColor.GREEN + "Claiming Wand");
				wMeta.setLore(Arrays.asList("", ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Left Click to set Corner 1",
						ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Right Click to set Corner 2",
						ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.YELLOW + "Shift and Right Click to the Claim the land",
						"", "",
						ChatColor.RED.toString() + ChatColor.BOLD + "[Note] " + ChatColor.AQUA + "You may use a regular stick to claim"));
				
				wand.setItemMeta(wMeta);
				player.getInventory().addItem(wand);
				player.sendMessage(ChatColor.GREEN + "You are now in Claiming mode, please follow the instructions on the Claiming Wand!");
			}
			
			if(args[0].equalsIgnoreCase("unclaim")) {
				
				if(!FactionManager.getManager().isPlayerInFaction(player)) {
					player.sendMessage(ChatColor.RED + "Must be in a faction to claim land! /f create [name]");
					return true;
				}
				
				Faction faction = FactionManager.getManager().getFactionByPlayer(player);
				
				if(!faction.getCaptainList().contains(player.getUniqueId().toString()) && !faction.getLeader().equalsIgnoreCase(player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.RED + "Only Captains and Leaders can unclaim land");
					return true;
				}
				
				faction.setHome(null);
				faction.setLoc1(null);
				faction.setLoc2(null);
				
				faction.sendMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has unclaimed your factions land!");
				
			}
			
			if(args[0].equalsIgnoreCase("map")) {
				if(ClaimEvent.getMap().contains(player.getName())) {
					claimEvent.removeFactionMap(player);
					ClaimEvent.getMap().remove(player.getName());
					
					player.sendMessage(ChatColor.YELLOW + "Pillars have been hidden");
					
				} else {
					ClaimEvent.getMap().add(player.getName());
					claimEvent.factionMap(player);
					player.sendMessage(ChatColor.YELLOW + "Pillars are being shown");
				}
			}
			
			if(args[0].equalsIgnoreCase("who") || args[0].equalsIgnoreCase("info")) {
				if(args.length < 2) {
					
					if(FactionManager.getManager().isPlayerInFaction(player)) {
						Faction faction = FactionManager.getManager().getFactionByPlayer(player);
						
						FactionManager.getManager().factionInfo(player, faction.getName());
					} else {
						player.sendMessage(ChatColor.RED + "/t who [faction/player]");
					}
					
					return true;
				}
				
				Faction faction = FactionManager.getManager().getFactionByName(args[1]);
				
				OfflinePlayer pl = Bukkit.getOfflinePlayer(args[1]);
				
				if(pl != null) {
					
					if(FactionManager.getManager().isPlayerInFaction(pl)) {
						FactionManager.getManager().factionInfoByName(player, FactionManager.getManager().getFactionByPlayer(pl).getName());
					}
					
				}
				
				if(FactionManager.getManager().getAllFactions().contains(faction)) {
					FactionManager.getManager().factionInfoByName(player, faction.getName());
				} 
				
				
			}
			
			if(args[0].equalsIgnoreCase("balance")) {
				
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + faction.getName() + ": " + ChatColor.GREEN + "$" + faction.getBalance());
				}
				
				
			}
			
			if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("d")) {

				if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "/t deposit [amount]");
					return true;
				}

				if (FactionManager.getManager().isPlayerInFaction(player)) {

					Faction faction = FactionManager.getManager().getFactionByPlayer(player);

					int balance = (int) Factions.economy.getBalance(player);

					try {

						if (args[1].equalsIgnoreCase("all")) {
							
							EconomyResponse r = Factions.economy.withdrawPlayer(player, balance);

							if (r.transactionSuccess() && balance > 0) {

								faction.setBalance(faction.getBalance() + balance);

								for (Player p : Bukkit.getOnlinePlayers()) {

									if (faction.getMemberList().contains(p.getUniqueId().toString())) {

										p.sendMessage(ChatColor.DARK_AQUA + player.getName() + " has deposited "
												+ ChatColor.GREEN + "$" + balance);

									}
								}
							} else {
								player.sendMessage(ChatColor.RED + "You don't have enough money");
							}
							return true;
						}

						double r = Factions.economy.getBalance(player);

						int amount = Integer.parseInt(args[1]);

						if (r > amount) {

							Factions.economy.withdrawPlayer(player, amount);

							faction.setBalance(faction.getBalance() + amount);

							for (Player p : Bukkit.getOnlinePlayers()) {

								if (faction.getMemberList().contains(p.getUniqueId().toString())) {

									p.sendMessage(ChatColor.DARK_AQUA + player.getName() + " has deposited "
											+ ChatColor.GREEN + "$" + amount);

								}
							}

						} else {
							player.sendMessage(ChatColor.RED + "You don't have enough money");
						}

					} catch (Exception e) {
						player.sendMessage(ChatColor.RED + "/t deposit <amount/all>");
					}

				}

			}
			
			if(args[0].equalsIgnoreCase("announce")) {
				
				String msg = "";
				for (int i = 1; i < args.length; i++) {
					msg = msg + args[i] + ' ';
				}
				
				FactionManager.getManager().setMotd(player, msg);
				
			}
			
			if(args[0].equalsIgnoreCase("chat")) {
				
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					
					if(!chat.contains(player.getName())) {
						chat.add(player.getName());
						player.sendMessage(ChatColor.YELLOW + "Talking in faction chat now");
					} else {
						chat.remove(player.getName());
						player.sendMessage(ChatColor.YELLOW + "Talking in public chat now");
					}
					
				} else {
					player.sendMessage(ChatColor.RED + "Must be in a faction to do this command");
				}
				
			} 
			
			if(args[0].equalsIgnoreCase("sethome")) {
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					if(faction.getLeader().equals(player.getUniqueId().toString()) || faction.getCaptainList().contains(player.getUniqueId().toString())) {
					
						if(FactionManager.getManager().insideClaim(player.getLocation())) {
							if(FactionManager.getManager().getClaimByLocation(player.getLocation()).getName().equals(faction.getName())) {
								faction.setHome(player.getLocation());
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									if(faction.getMemberList().contains(p.getUniqueId().toString())) {
										
										p.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.YELLOW + " has set the Faction Home! user /t home");
										
									}
								}
							} else {
								player.sendMessage(ChatColor.RED + "You can only set Faction Home inside your own claim!");
							}
						}  else {
							player.sendMessage(ChatColor.RED + "You can only set Faction Home inside your own claim!");
						}
					}
				}
			}
			
			if(args[0].equalsIgnoreCase("home")) {
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					if(faction.getHome() == null) {
						player.sendMessage(ChatColor.RED + "Your faction has not set a home yet");
						return true;
					}
					
					home.put(player.getName(), 10);
					
				} else {
					player.sendMessage(ChatColor.RED + "You're not in a faction");
				}
			}
			
		}
		
		return true;
	}
	
	public HashMap<String, Integer> getHome() {
		return home;
	}
	
	public static ArrayList<String> getChat() {
		return chat;
	}
	
	private void factionHelp(Player player) {
		
		String message = "";
		
		message = message + ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------" + "\n";
		
		message = message  + ChatColor.BLUE + "Faction Help   " + leaderTag() + ChatColor.GRAY + " Leader " + captainTag() + ChatColor.GRAY + " Captain" + "\n";
		message = message + ChatColor.GRAY + "  /f create" + ChatColor.YELLOW + " - Create a faction." + "\n";
		message = message + ChatColor.GRAY + "  /f join" + ChatColor.YELLOW + " - Accept a faction invitation." + "\n";
		message = message + ChatColor.GRAY + "  /f leave" + ChatColor.YELLOW + " - Leave your faction." + "\n";
		message = message + ChatColor.GRAY + "  /f chat" + ChatColor.YELLOW + " - Switch between public and faction chat." + "\n";
		message = message + ChatColor.GRAY + "  /f show" + ChatColor.YELLOW + " - View faction information." + "\n";
		message = message + ChatColor.GRAY + "  /f map" + ChatColor.YELLOW + " - Pillars that indicate faction claims." + "\n";
		message = message + ChatColor.GRAY + "  /f balance" + ChatColor.YELLOW + " - View your factions balance." + "\n";
		message = message + ChatColor.GRAY + "  /f home" + ChatColor.YELLOW + " - Teleport to your factions home." + "\n";
		message = message + ChatColor.GRAY + "  /f deposit" + ChatColor.YELLOW + " - Deposit your money into the faction." + "\n";
		message = message + ChatColor.GRAY + "  /f invite " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Invite a member to your faction." + "\n";
		message = message + ChatColor.GRAY + "  /f uninvite " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Remove a players invitation to your faction." + "\n";
		message = message + ChatColor.GRAY + "  /f kick " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Kick a member from the faction." + "\n";
		message = message + ChatColor.GRAY + "  /f claim " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Claim land for your faction." + "\n";
		message = message + ChatColor.GRAY + "  /f kick " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Kick a member from the faction." + "\n";
		message = message + ChatColor.GRAY + "  /f withdraw " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Withdraw money for the factions balance." + "\n";
		message = message + ChatColor.GRAY + "  /f sethome " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Set your factions home location." + "\n";
		message = message + ChatColor.GRAY + "  /f announce " + leaderTag() + captainTag() + ChatColor.YELLOW + " - Announce a message to everyone in your faction." + "\n";
		message = message + ChatColor.GRAY + "  /f disband " + leaderTag() + ChatColor.YELLOW + " - Disband your faction, this can NOT be un-done." + "\n";
		message = message + ChatColor.GRAY + "  /f promote " + leaderTag() + ChatColor.YELLOW + " - Promote a member to the captain role." + "\n";
		message = message + ChatColor.GRAY + "  /f demote " + leaderTag() + ChatColor.YELLOW + " - Demote a captain to the member role." + "\n";
		message = message + ChatColor.GRAY + "  /f leader " + leaderTag() + ChatColor.YELLOW + " - Make someone else in the faction the leader." + "\n";
		message = message + ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------" + "\n";
		
		player.sendMessage(message);
	}
	
	private String leaderTag() {
		return ChatColor.GRAY + "[" + ChatColor.AQUA + "L" + ChatColor.GRAY + "]";
	}
	
	private String captainTag() {
		return ChatColor.GRAY + "[" + ChatColor.RED + "C" + ChatColor.GRAY + "]";
	}
}
