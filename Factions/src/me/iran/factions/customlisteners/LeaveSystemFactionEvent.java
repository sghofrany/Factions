package me.iran.factions.customlisteners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.iran.factions.system.SystemFaction;

public class LeaveSystemFactionEvent extends Event {

private static final HandlerList handlers = new HandlerList();
    
	private Player player;
	private SystemFaction faction;

	public LeaveSystemFactionEvent(Player player, SystemFaction faction) {
		
		this.setPlayer(player);
		this.setFaction(faction);
	}
	
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public SystemFaction getFaction() {
		return faction;
	}

	public void setFaction(SystemFaction faction) {
		this.faction = faction;
	}
}
