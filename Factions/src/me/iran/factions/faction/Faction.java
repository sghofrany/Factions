package me.iran.factions.faction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Faction {

	private String name;
	private String leader;
	private String motd;
	
	private List<String> captains;
	private List<String> members;
	private List<String> invites;
	
	private double dtr;
	private double maxDtr;
	
	private boolean frozen;
	private boolean regen;
	private boolean raidable;
	
	private int freezeTime;
	private int balance;
	
	private Location loc1;
	private Location loc2;
	private Location home;
	
	public Faction(String leader, String name) {
		this.leader = leader;
		this.name = name;
		
		motd = "Plugin created by Irantwomiles";
		
		captains = new ArrayList<>();
		members = new ArrayList<>();
		invites = new ArrayList<>();
		
		dtr = 0.0;
		maxDtr = 1.1;
		
		frozen = false;
		regen = true;
		raidable = false;
		
		freezeTime = 0;
		balance = 0;
		
		members.add(leader);
		
	}
	
	/**Team name**/
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**Team leader**/
	
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	public String getLeader() {
		return leader;
	}
	
	/**Team motd**/
	
	public void setMotd(String motd) {
		this.motd = motd;
	}
	
	public String getMotd() {
		return motd;
	}
	
	/**Team captains**/
	
	public void setCaptainList(List<String> captains) {
		this.captains = captains;
	}
	
	public List<String> getCaptainList() {
		return captains;
	}
	
	public void addCaptain(String name) {
		captains.add(name);
	}
	
	public void removeCaptain(String name) {
		captains.remove(name);
	}
	
	/**Team members**/
	
	public void setMemberList(List<String> members) {
		this.members = members;
	}
	
	public List<String> getMemberList() {
		return members;
	}
	
	public void addMember(String name) {
		members.add(name);
	}
	
	public void removeMember(String name) {
		members.remove(name);
	}
	
	/**Team members**/
	
	public void setInviteList(List<String> invites) {
		this.invites = invites;
	}
	
	public List<String> getInvitesList() {
		return invites;
	}
	
	public void addInvite(String name) {
		invites.add(name);
	}
	
	public void removeInvite(String name) {
		invites.remove(name);
	}
	
	/**Team Dtr**/
	
	public void setDtr(double dtr) {
		this.dtr = dtr;
	}
	
	public double getDtr() {
		return dtr;
	}
	
	public void setMaxDtr(double maxDtr) {
		this.maxDtr = maxDtr;
	}
	
	public double getMaxDtr() {
		return maxDtr;
	}
	
	/**Team booleans**/
	
	public boolean isRegen() {
		return regen;
	}
	
	public void setRegen(boolean regen) {
		this.regen = regen;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public boolean isRaidable() {
		return raidable;
	}
	
	public void setRaidable(boolean raidable) {
		this.raidable = raidable;
	}
	
	/**Team Freezetime**/
	
	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}
	
	public int getFreezeTime() {
		return freezeTime;
	}
	
	/**Team balance**/
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getBalance() {
		return balance;
	}
	
	/**Team claim**/
	
	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}
	
	public Location getLoc1() {
		return loc1;
	}
	
	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}
	
	public Location getLoc2() {
		return loc2;
	}
	
	/**Team Home**/
	
	public void setHome(Location home) {
		this.home = home;
	}
	
	public Location getHome() {
		return home;
	}
}
