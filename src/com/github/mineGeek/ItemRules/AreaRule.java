package com.github.mineGeek.ItemRules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Events.onAreaRuleEntrance;
import com.github.mineGeek.ItemRules.Events.onAreaRuleExit;
import com.github.mineGeek.ItemRules.Rules.Rule;
import com.github.mineGeek.ItemRules.Rules.Rules;
import com.github.mineGeek.ItemRules.Store.Players;


/**
 * A rule that is triggered by player moving into/out of area
 *
 */
public class AreaRule {

	/**
	 * Area object for cuboid regions that this rule applies to
	 */
	private Area area;
	
	
	
	
	/**
	 * List of rules that will be added to a player moving into the cuboid
	 */
	private List<Rule> enterRules = new ArrayList<Rule>();
	
	
	
	
	/**
	 * List of rules that will be removed from a player when they move OUT of area
	 */
	private List<Rule> exitRules = new ArrayList<Rule>();
	
	
	
	
	/**
	 * A unique tag for this rule
	 */
	private String tag;
	
	
	
	
	/**
	 * Return unique areaRule tag
	 * @return
	 */
	public String getTag() {
		return this.tag;
	}
	
	
	
	
	/**
	 * Set unique tag for areaRule
	 * @param value
	 */
	public void setTag( String value ) {
		this.tag = value;
	}
	
	
	
	
	
	/**
	 * Return Area object for areaRule
	 * @return
	 */
	public Area getArea() {
		return this.area;
	}
	
	
	
	
	/**
	 * Sets the Area object that defines area
	 * @param area
	 */
	public void setArea( Area area) {
		this.area = area;
	}
	
	
	
	
	
	/**
	 * Sets the list of rule names that will manually be added
	 * to player when they walk into the area
	 * @param names
	 */
	public void setEntranceRules( List<String> names ) {
		
		if ( names.size() > 0 ) {
			for ( String x: names ) {
				this.enterRules.add( Rules.getRule(x) );
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Sets list of rule names that were manually added, but will be
	 * removed when a player exits the area
	 * @param names
	 */
	public void setExitRules( List<String> names ) {
		
		if ( names.size() > 0 ) {
			for ( String x: names ) {
				this.exitRules.add( Rules.getRule(x) );
			}
		}
		
	}	
	
	
	
	
	
	/**
	 * returns list of rules that apply to a player entering area
	 * @return
	 */
	public List<Rule> getEntranceRules() {
		return this.enterRules;
	}
	
	
	
	
	
	/**
	 * returns list of rules that apply to a player exiting the area
	 * @return
	 */
	public List<Rule> getExitRules() {
		return this.exitRules;
	}
	
	
	
	
	/**
	 * Processes and applies rules that are manually added when a 
	 * player enters the area
	 * @param player
	 */
	public void applyEntranceRules( Player player ) {
		
		if ( !this.enterRules.isEmpty() ) {
			
			onAreaRuleEntrance event = new onAreaRuleEntrance( this, Players.get(player) );
			Config.server.getPluginManager().callEvent(event);
			
			if ( !event.isCancelled() ) {
				for ( Rule x : this.enterRules ) {
					API.addRuleToPlayer(player, x.getTag() );
				}
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Process and removes any manually added rules that a player
	 * had added from entering an area
	 * @param player
	 */
	public void applyExitRules( Player player ) {
		
		if ( !this.exitRules.isEmpty() ) {
			
			onAreaRuleExit event = new onAreaRuleExit( this, Players.get(player) );
			Config.server.getPluginManager().callEvent(event);
			
			if ( !event.isCancelled() ) {			
				for ( Rule x : this.exitRules ) {
					API.removeRuleFromPlayer( player, x.getTag() );
				}
			}
		}
		
	}
	
	
	public void close() {
		
		this.area.close();
		this.area = null;
		this.enterRules.clear();
		this.exitRules.clear();
		this.tag = null;
		
	}
	
	
	
}
