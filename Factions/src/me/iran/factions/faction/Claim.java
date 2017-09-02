package me.iran.factions.faction;

import org.bukkit.Location;

public class Claim {

	private Location loc1;
	private Location loc2;
	
	private Faction faction;
	
	public Claim(Location loc1, Location loc2, Faction faction) {
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
	
	public Faction getFaction() {
		return faction;
	}
	
	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}
	
	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}
	
	public void setFaction(Faction faction) {
		this.faction = faction;
	}
}
