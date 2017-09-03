package me.iran.factions.system;

import org.bukkit.Location;

public class SystemClaim {
	private Location loc1;
	private Location loc2;
	
	private SystemFaction faction;
	
	public SystemClaim(Location loc1, Location loc2, SystemFaction faction) {
		this.loc1 = loc1;
		this.loc2 = loc2;
		this.faction = faction;
	}
	
	public Location getLoc1() {
		return loc1;
	}
	
	public Location getLoc2() {
		return loc2;
	}
	
	public SystemFaction getFaction() {
		return faction;
	}
	
	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}
	
	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}
	
	public void setFaction(SystemFaction faction) {
		this.faction = faction;
	}
}
