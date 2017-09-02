package me.iran.factions.faction;

import java.util.ArrayList;
import java.util.HashMap;

import me.iran.factions.Factions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ClaimEvent implements Listener {

	Factions plugin;
	
	private static HashMap<String, Claim> claiming = new HashMap<>();
	private static ArrayList<String> map = new ArrayList<>();
	
	public ClaimEvent (Factions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getAction() == null) {
			return;
		}
		
		if(!player.getItemInHand().hasItemMeta()) {
			return;
		}
		
		if(player.getItemInHand().getItemMeta().getDisplayName().equals(null)) {
			return;
		}
		
		if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.getItemInHand().getType() == Material.STICK && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Claiming Wand")) {
			
			if(claiming.containsKey(player.getName())) {

				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					if(faction.getLeader().equals(player.getUniqueId().toString())) {
						
						if(FactionManager.getManager().insideClaim(event.getClickedBlock().getLocation())) {
							player.sendMessage(ChatColor.RED + "Can't set location inside someone else's claim");
						} else {

							Claim claim = claiming.get(player.getName());
							
							claiming.get(player.getName()).setLoc1(event.getClickedBlock().getLocation());
							claiming.get(player.getName()).setLoc2(event.getClickedBlock().getLocation());
							
							if(faction.getBalance() >= claimCost(claim.getLoc1(), claim.getLoc2())) {
								player.sendMessage(ChatColor.GREEN + "Picked Corner 1 " + ChatColor.GRAY + "(X:" + claim.getLoc1().getBlockX() + " Z:" + claim.getLoc1().getBlockZ() + ") " 
										+ ChatColor.GREEN + "$" + claimCost(claim.getLoc1(), claim.getLoc2()));
								
							} else {
								player.sendMessage(ChatColor.GREEN + "Picked Corner 1 " + ChatColor.GRAY + "(X:" + claim.getLoc1().getBlockX() + " Z:" + claim.getLoc1().getBlockZ() + ") " 
										+ ChatColor.RED + "$" + claimCost(claim.getLoc1(), claim.getLoc2()));
							}
							
						}
						
					}
				}
				
			}
			
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.STICK && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Claiming Wand")) {
			
			if(claiming.containsKey(player.getName())) {
				
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					if(faction.getLeader().equals(player.getUniqueId().toString())) {
						
						if(FactionManager.getManager().insideClaim(event.getClickedBlock().getLocation())) {
							player.sendMessage(ChatColor.RED + "Can't set location inside someone else's claim");
						} else {

							Claim claim = claiming.get(player.getName());
							
							claiming.get(player.getName()).setLoc2(event.getClickedBlock().getLocation());
							
							if(faction.getBalance() >= claimCost(claim.getLoc1(), claim.getLoc2())) {
								player.sendMessage(ChatColor.GREEN + "Picked Corner 2 " + ChatColor.GRAY + "(X:" + claim.getLoc2().getBlockX() + " Z:" + claim.getLoc2().getBlockZ() + ") " 
										+ ChatColor.GREEN + "$" + claimCost(claim.getLoc1(), claim.getLoc2()));
								
							} else {
								player.sendMessage(ChatColor.GREEN + "Picked Corner 2 " + ChatColor.GRAY + "(X:" + claim.getLoc2().getBlockX() + " Z:" + claim.getLoc2().getBlockZ() + ") " 
										+ ChatColor.RED + "$" + claimCost(claim.getLoc1(), claim.getLoc2()));
							}
							
						}
					}
				}
				
			}
			
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.STICK && player.isSneaking() && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Claiming Wand")) {
			
			if(claiming.containsKey(player.getName())) {
				
				Claim claim = claiming.get(player.getName());
				
				if(claim.getLoc1() == null || claim.getLoc2() == null) {
					player.sendMessage(ChatColor.RED + "You have not seleted two corners yet!");
					return;
				}
				
				if(claim.getLoc1().equals(claim.getLoc2())) {
					player.sendMessage(ChatColor.RED + "You have not seleted two corners yet!");
					return;
				}
				
				if(FactionManager.getManager().isPlayerInFaction(player)) {
					Faction faction = FactionManager.getManager().getFactionByPlayer(player);
					
					if(faction.getLeader().equals(player.getUniqueId().toString())) {
						
						if(FactionManager.getManager().canClaim(claim.getLoc1(), claim.getLoc2())) {

							if(faction.getBalance() >= claimCost(claim.getLoc1(), claim.getLoc2())) {
								faction.setLoc1(claiming.get(player.getName()).getLoc1());
								faction.setLoc2(claiming.get(player.getName()).getLoc2());
								
								faction.setBalance(faction.getBalance() - claimCost(claim.getLoc1(), claim.getLoc2()));
								
								player.sendMessage(ChatColor.GREEN + "You have claimed this land for " + ChatColor.GRAY + "$" + claimCost(claim.getLoc1(), claim.getLoc2()));
								player.setItemInHand(null);
								claiming.remove(player.getName());
							} else {
								player.sendMessage(ChatColor.RED + "You are missing " + ChatColor.YELLOW + "$" + Math.abs((faction.getBalance() - claimCost(claim.getLoc1(), claim.getLoc2()))));
							}
							
						} else {
							player.sendMessage(ChatColor.RED + "Can't claim here");
						}
						
					}
				}
				
			}
			
		}
		
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(claiming.containsKey(player.getName())) {
			claiming.remove(player.getName());
		}
		
		if(map.contains(player.getName())) {
			map.remove(player.getName());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void factionMap(Player player) {
		
		int block = 0;
		
		for(Faction faction : FactionManager.getManager().getAllFactions()) {
			
			if(faction.getLoc1() != null && faction.getLoc2() != null) {
				int maxx = Math.max(faction.getLoc1().getBlockX(), faction.getLoc2().getBlockX());
				int minx = Math.min(faction.getLoc1().getBlockX(), faction.getLoc2().getBlockX());
				
				int maxz = Math.max(faction.getLoc1().getBlockZ(), faction.getLoc2().getBlockZ());
				int minz = Math.min(faction.getLoc1().getBlockZ(), faction.getLoc2().getBlockZ());
				
				block++;
				
				if(block > 15) {
					block = 0;
				}
				
				for(int i = 4; i < 256; i++) {
					
					Location loc1 = new Location(Bukkit.getWorld("world"), minx, i, minz);
					Location loc2 = new Location(Bukkit.getWorld("world"), maxx, i, maxz);
					Location loc3 = new Location(Bukkit.getWorld("world"), maxx, i, minz);
					Location loc4 = new Location(Bukkit.getWorld("world"), minx, i, maxz);
					
					Block block1 = Bukkit.getWorld("world").getBlockAt(loc1);
					Block block2 = Bukkit.getWorld("world").getBlockAt(loc2);
					Block block3 = Bukkit.getWorld("world").getBlockAt(loc3);
					Block block4 = Bukkit.getWorld("world").getBlockAt(loc4);
					
					if(block1.getType() == Material.AIR) {
						player.sendBlockChange(loc1, Material.getMaterial(95), (byte) block);
					}
					
					if(block2.getType() == Material.AIR) {
						player.sendBlockChange(loc2, Material.getMaterial(95), (byte) block);
					}
					
					if(block3.getType() == Material.AIR) {
						player.sendBlockChange(loc3, Material.getMaterial(95), (byte) block);
					}
					
					if(block4.getType() == Material.AIR) {
						player.sendBlockChange(loc4, Material.getMaterial(95), (byte) block);
					}
					
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeFactionMap(Player player) {
		for(Faction faction : FactionManager.getManager().getAllFactions()) {
			
			if(faction.getLoc1() != null && faction.getLoc2() != null) {
				
				int maxx = Math.max(faction.getLoc1().getBlockX(), faction.getLoc2().getBlockX());
				int minx = Math.min(faction.getLoc1().getBlockX(), faction.getLoc2().getBlockX());
				
				int maxz = Math.max(faction.getLoc1().getBlockZ(), faction.getLoc2().getBlockZ());
				int minz = Math.min(faction.getLoc1().getBlockZ(), faction.getLoc2().getBlockZ());
				
				
				for(int i = 4; i < 256; i++) {
					
					Location loc1 = new Location(Bukkit.getWorld("world"), minx, i, minz);
					Location loc2 = new Location(Bukkit.getWorld("world"), maxx, i, maxz);
					Location loc3 = new Location(Bukkit.getWorld("world"), maxx, i, minz);
					Location loc4 = new Location(Bukkit.getWorld("world"), minx, i, maxz);
					
					Block block1 = Bukkit.getWorld("world").getBlockAt(loc1);
					Block block2 = Bukkit.getWorld("world").getBlockAt(loc2);
					Block block3 = Bukkit.getWorld("world").getBlockAt(loc3);
					Block block4 = Bukkit.getWorld("world").getBlockAt(loc4);
					
					if(block1.getType() == Material.AIR) {
						player.sendBlockChange(loc1, Material.AIR, (byte) 0);
					}
					
					if(block2.getType() == Material.AIR) {
						player.sendBlockChange(loc2, Material.AIR, (byte) 0);
					}
					
					if(block3.getType() == Material.AIR) {
						player.sendBlockChange(loc3, Material.AIR, (byte) 0);
					}
					
					if(block4.getType() == Material.AIR) {
						player.sendBlockChange(loc4, Material.AIR, (byte) 0);
					}
				}
			}
		}
	}
	
	public int claimCost(Location loc1, Location loc2) {
		int cost = 0;
		
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		
		Location costLoc1 = new Location(Bukkit.getWorld("world"), maxX, 0, maxZ);
		Location costLoc2 = new Location(Bukkit.getWorld("world"), minX, 0, maxZ);
		Location costLoc3 = new Location(Bukkit.getWorld("world"), minX, 0, minZ);
		
		int width = Math.abs((int) costLoc1.distance(costLoc2));
		int height = Math.abs((int) costLoc2.distance(costLoc3));
		
		cost = (width * height) * 2;
		
		return cost;
	}
	
	public HashMap<String, Claim> getClaim() {
		return claiming;
	}
	
	public static ArrayList<String> getMap() {
		return map;
	}
}
