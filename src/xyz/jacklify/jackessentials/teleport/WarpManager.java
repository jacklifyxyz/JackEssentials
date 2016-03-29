package xyz.jacklify.jackessentials.teleport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import xyz.jacklify.jackessentials.JackEssentials;
import xyz.jacklify.jackessentials.PluginConfig;

public class WarpManager {
	class Warp {
		private String name, owner, world;
		private double x, y, z;
		private boolean restricted;
		
		Warp ( String _name, String _owner, double _x, double _y, double _z, String _world, boolean _restricted) {
			this.name = _name;
			this.owner = _owner;
			this.world = _world;
			this.x = _x;
			this.y = _y;
			this.z = _z;
			this.restricted = _restricted;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getOwner() {
			return this.owner;
		}
		
		public String getWorld() {
			return this.world;
		}
		
		public double getX() {
			return this.x;
		}
		
		public double getY() {
			return this.y;
		}
		
		public double getZ() {
			return this.z;
		}
		
		public boolean isRestricted() {
			return this.restricted;
		}
	}
	
	private Map<String, Warp> warpList = new ConcurrentHashMap<String, Warp>();
	private Map<Player, List<Warp>> warps = new ConcurrentHashMap<Player, List<Warp>>();
	private JackEssentials plugin = null;
	
	private Logger logger = Logger.getLogger("Minecraft");
	
	public WarpManager(JackEssentials _plugin) {
		this.plugin = _plugin;
		
		File warpDb = new File(this.plugin.getDataFolder(), "warps.dat");
		
		if (!warpDb.exists()) {
			logger.warning("[JackEssentials] Warp database not found! Generating...");
			try {
				warpDb.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "[JackEssentials] Failed to create JackEssentials/warps.dat! An IOException has occurred!", e);
				return;
			}
		}
		
		loadFile(warpDb);
	}

	private void loadFile(File warpDb) {
		// TODO Auto-generated method stub
		
	}
}
