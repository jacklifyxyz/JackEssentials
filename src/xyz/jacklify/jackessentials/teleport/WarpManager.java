package xyz.jacklify.jackessentials.teleport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
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
	
	private List<String> warpNames = new CopyOnWriteArrayList<String>();
	private Map<String, Warp> warpList = new ConcurrentHashMap<String, Warp>();
	private Map<Player, String> warps = new ConcurrentHashMap<Player, String>();
	private JackEssentials plugin = null;
	private Properties warpJDb = new Properties();
	private File warpDb = null;
	
	private Logger logger = Logger.getLogger("Minecraft");
	
	public WarpManager(JackEssentials _plugin) {
		this.plugin = _plugin;
		this.warpDb = new File(this.plugin.getDataFolder(), "warps.dat");
		
		if (!this.warpDb.exists()) {
			logger.warning("[JackEssentials] Warp database not found! Generating...");
			try {
				this.warpDb.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "[JackEssentials] Failed to create JackEssentials/warps.dat! An IOException has occurred!", e);
				return;
			}
		}
		
		try {
			loadFile();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "[JackEssentials] Failed to load JackEssentials/warps.dat! An IOException has occurred!", e);
			e.printStackTrace();
		}
		
		Iterator warpKeys = this.warpJDb.entrySet().iterator();
		while (warpKeys.hasNext()) {
			String warpKey = (String)warpKeys.next();
			String warpData = this.warpJDb.getProperty(warpKey);
			if (warpData != null && !warpData.isEmpty()) {
				try {
					String[] data = warpData.split(":");
					if (data.length < 5) {
						logger.log(Level.WARNING, "[JackEssentials] Invalid entry '" + warpKey + "=" + warpData + "' in JackEssentials/warps.dat! Ignoring.");
						continue;
					} else {
					//	Warp ( String _name, String _owner, double _x, double _y, double _z, String _world, boolean _restricted) {
						Warp warp = new Warp (warpKey, data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), data[4], Boolean.parseBoolean(data[5]));
						this.warpList.put(warpKey, warp);
					}
				} catch (Throwable t) {
					logger.log(Level.SEVERE, "[JackEssentials] Failed to parse entry '" + warpKey + "=" + warpData + "' in JackEssentials/warps.dat! An exception has occurred! Ignoring entry..", t);
				}
			}
		}
	}

	private void loadFile() throws IOException {
		FileInputStream fis = new FileInputStream(this.warpDb);
		this.warpJDb.load(fis);
		fis.close();
	}
	
	public void handlePlayerJoin(Player player) {
		String _pwarps = "";
		Iterator warpCollection = this.warps.values().iterator();
		while (warpCollection.hasNext()) {
			Warp warp = (Warp)warpCollection.next();
			
			if (!warp.isRestricted()) {
				_pwarps = _pwarps + ChatColor.GREEN + warp.getName() + ChatColor.RESET + ",";
			} else {
				if (warp.getOwner() == player.getUniqueId().toString()) {
					_pwarps = _pwarps + ChatColor.RED + warp.getName() + ChatColor.RESET + ",";
				}
			}
		}
		
		this.warps.put(player, _pwarps);
	}
	
	public void handlePlayerLeave(Player player) {
		this.warps.remove(player);
	}
	
	public boolean doesWarpExist(String name) {
		return this.warpNames.contains(name);
	}
	
	public Warp getWarp(String name) {
		return this.warpList.get(name);
	}
	
	public boolean createWarp(Player player, Warp warp) {
		try {
			this.warpList.put(warp.getName(), warp);
			this.warpNames.add(warp.getName());
			if (warp.isRestricted()) {
				String data = this.warps.get(player);
				if (data == null) {
					data = "";
				}
				this.warps.put(player, data + ChatColor.RED + warp.getName() + ChatColor.RESET + ";");
			} else {
				Iterator players = this.warps.keySet().iterator();
				
				while (players.hasNext()) {
					Player playerd = (Player)players.next();
					String data = this.warps.get(playerd);
					if (data == null) {
						data = "";
					}
					this.warps.put(playerd, data + ChatColor.GREEN + warp.getName() + ChatColor.RESET + ";");
				}
			}
			this.warpJDb.put(warp.getName(), this.calculateWarpInfo(warp));
			this.updateDB();
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String calculateWarpInfo(Warp warp) {
		return warp.getOwner() + ":" + warp.getX() + ":" + warp.getY() + ":" + warp.getZ() + ":" + warp.getWorld() + ":" + warp.isRestricted();
	}

	public boolean deleteWarp(Warp warpd) {
		try {
			this.warpList.remove(warpd.getName());
			this.warpNames.remove(warpd.getName());
			if (warpd.isRestricted()) {
				Player player = this.plugin.getServer().getPlayer(warpd.getOwner());
				
				if (player != null) {
					this.handlePlayerJoin(player);
				}
			} else {
				Iterator players = this.warps.keySet().iterator();
				
				while (players.hasNext()) {
					Player player = (Player)players.next();
					this.handlePlayerJoin(player);
				}
			}
			this.warpJDb.remove(warpd.getName());
			this.updateDB();
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	private void updateDB() throws IOException {
		FileOutputStream fos = new FileOutputStream(this.warpDb);
		this.warpJDb.store(fos, "JackEssentials Warp Database");
		fos.close();
	}
}
