package xyz.jacklify.jackessentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JackListener implements Listener {

	private JackEssentials plugin;
	
	public JackListener(JackEssentials _plugin) {
		this.plugin = _plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onLogin(PlayerLoginEvent event) {
		
	}
}
