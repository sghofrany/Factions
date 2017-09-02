package me.iran.factions.customlisteners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.iran.factions.faction.Faction;

public class EnterClaimEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    
	private Player player;
	private Faction faction;

	public EnterClaimEvent(Player player, Faction faction) {
		
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

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

}
