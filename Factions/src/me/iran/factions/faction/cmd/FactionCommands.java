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

	Factions plugin;
	
	private static HashMap<String, Integer> home = new HashMap<String, Integer>();
	
	private static ArrayList<String> chat = new ArrayList<>();
	
	public FactionCommands(Factions plugin) {
		this.plugin = plugin;
	}
	
	ClaimEvent claimEvent = new ClaimEvent(plugin);
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("faction")) {
			
			if(args.length < 1) {
				
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-= " + ChatColor.GOLD + "Team Commands" + ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "=-");
				player.sendMessage(ChatColor.YELLOW + "");
				player.sendMessage(ChatColor.RED + "Team Captain Commands");
				player.sendMessage(ChatColor.YELLOW + "/f create <name>");
				player.sendMessage(ChatColor.YELLOW + "/f disband");
				player.sendMessage(ChatColor.YELLOW + "/f invite <player>");
				player.sendMessage(ChatColor.YELLOW + "/f revoke <player>");
				player.sendMessage(ChatColor.YELLOW + "/f kick <player>");
				player.sendMessage(ChatColor.YELLOW + "/f leader <player>");
				player.sendMessage(ChatColor.YELLOW + "/f promote <player>");
				player.sendMessage(ChatColor.YELLOW + "/f demote <player>");
				player.sendMessage(ChatColor.YELLOW + "/f claim");
				player.sendMessage(ChatColor.YELLOW + "/f withdraw <amount>");
				player.sendMessage(ChatColor.YELLOW + "/f sethome");
				player.sendMessage(ChatColor.YELLOW + "/f announce <message>");
				player.sendMessage(ChatColor.YELLOW + "");
				player.sendMessage(ChatColor.DARK_GRAY + "Team Member Commands");
				player.sendMessage(ChatColor.GRAY + "/f join <f>");
				player.sendMessage(ChatColor.GRAY + "/f leave");
				player.sendMessage(ChatColor.GRAY + "/f chat");
				player.sendMessage(ChatColor.GRAY + "/f join <f>");
				player.sendMessage(ChatColor.GRAY + "/f who <f>");
				player.sendMessage(ChatColor.GRAY + "/f map");
				player.sendMessage(ChatColor.GRAY + "/f balance");
				player.sendMessage(ChatColor.GRAY + "/f deposit <amount>");
				player.sendMessage(ChatColor.GRAY + "/f home ");
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				
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
	
}
