package xyz.jacklify.jackessentials.teleport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import xyz.jacklify.jackessentials.PluginConfig;

public class TeleportManager {

	private Map<Player, List<Player>> teleportRequests = new ConcurrentHashMap<Player, List<Player>>();
	private PluginConfig config = null;
	
	public TeleportManager(PluginConfig _config) {
		this.config = _config;
	}
	
	public void playerJoin(Player plyr) {
		this.teleportRequests.put(plyr, new ArrayList<Player>());
	}
	
	public void playerLeave(Player plyr) {
		this.teleportRequests.remove(plyr);
	}
	
	public void sendRequest(Player requester, Player requestee) {
		this.teleportRequests.get(requestee).add(requester);
		requester.sendMessage(Color.AQUA + "Sent teleport request!");
		requestee.sendMessage(requester.getDisplayName() + Color.RED +  " has requested to teleport to you.");
		requestee.sendMessage(Color.RED + "To accept use /tpa or /tpa " + requester.getDisplayName());
		requestee.sendMessage(Color.RED + "To deny use /tpd or /tpd " + requester.getDisplayName());
	}
	
	public void denyRequest(Player denier, Player deniee) {
		this.teleportRequests.get(denier).remove(deniee);
		denier.sendMessage(Color.GREEN + "Request denied successfully.");
		deniee.sendMessage(Color.RED + "Your teleport request to " + denier.getDisplayName() + " has been denied!");
	}
	
	public boolean doesRequestExist(Player requester, Player requestee) {
		return this.teleportRequests.get(requestee).contains(requester);
	}
	
	public void acceptRequest(Player accepter, Player acceptee) {
		this.teleportRequests.get(accepter).remove(acceptee);
		accepter.sendMessage(Color.GREEN + "Accepting teleport request!");
		acceptee.sendMessage(Color.GREEN + "Teleporting!");
		
		if (acceptee.getWorld() != accepter.getWorld()) {
			if (config.crossWorldTeleporting) {
				accepter.teleport(acceptee.getLocation());
				return;
			} else {
				acceptee.sendMessage(Color.RED + "The administrators have disabled cross-world teleportation.");
				accepter.sendMessage(Color.RED + "Unable to teleport player because the admninistrators have disabled cross-world teleportation.");
				return;
			}
		}
		acceptee.teleport(accepter);
	}
	
	public List<Player> getPendingRequests(Player plyr) {
		return this.teleportRequests.get(plyr);
	}
}
