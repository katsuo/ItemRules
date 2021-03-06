package com.github.mineGeek.ItemRules.Utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

/**
 * Utility class to help prevent spamming the crap out of a player with the same 
 * message. 
 * TODO: Change the gc to be a scheduled task.
 *
 */
public class PlayerMessenger {


	/**
	 * HashMap of recent messages sent to users
	 */
	private static Map<String, Map<String, Long>> _queue = new HashMap<String, Map<String, Long>>();

	
	/**
	 * Sends a message to the player but prevents spamming it to them
	 * @param player
	 * @param message
	 */
	public static void SendPlayerMessage(Player player, String message ) {
		
		Boolean printMessage = true;

		Long benchMark = System.currentTimeMillis() - Config.spamPlayerMessageTimeout;
		
		Map<String, Long> map = new HashMap<String, Long>();
		
		if ( _queue.containsKey( player.getName() ) ) {
			if ( _queue.get( player.getName() ).containsKey( message ) ) {
				if ( _queue.get(player.getName()).get(message).longValue() > benchMark ) {
					printMessage = false;
				}
				map = _queue.get(player.getName());
			}
		}
		
		map.put( message, System.currentTimeMillis() );
		_queue.put(player.getName(), map );
		
		if ( printMessage == true ) {
			player.sendMessage( message );
		}
		
	}
	
	
	/**
	 * GC on list. Get rid of old stuff.
	 */
	public static void clean() {
		
		try{
			Iterator<Entry<String, Map<String, Long>>> i = _queue.entrySet().iterator();
			
			while ( i.hasNext() ) {
				
				Entry<String, Map<String, Long>> o = i.next();
				
				Iterator<Entry<String, Long>> x = o.getValue().entrySet().iterator();
				
				while ( x.hasNext() ) {
					
					Map.Entry<String, Long> y = x.next();
					
					if ( y.getValue() < System.currentTimeMillis() - Config.spamPlayerMessageTimeout ) {
						_queue.remove( o.getKey() );
					}
					
				}
				
			}
		} catch (Exception e ) {}
		
	}
	
	
	/**
	 * Good guy brings closure
	 */
	public static void close() {
		if ( _queue !=null ) _queue.clear();
	}
}