package me.iran.factions.system;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class SystemFaction {

	private String name;
	
	private String motd;
	
	private Location loc1;
	private Location loc2;
	
	private Location home;
	
	private boolean deathban;
	
	private ChatColor color;

	public SystemFaction (String name) {
		
		this.name = name;
		
		this.setMotd("Plugin created by Irantwomiles");
		
		this.deathban = true;
		
		setColor(ChatColor.AQUA);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLoc1() {
		return loc1;
	}

	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}

	public Location getLoc2() {
		return loc2;
	}

	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}

	public Location getHome() {
		return home;
	}

	public void setHome(Location home) {
		this.home = home;
	}

	public boolean isDeathban() {
		return deathban;
	}

	public void setDeathban(boolean deathban) {
		this.deathban = deathban;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public String getMotd() {
		return motd;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}
	
}
