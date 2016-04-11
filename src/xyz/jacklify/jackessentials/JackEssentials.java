package xyz.jacklify.jackessentials;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.jacklify.jackessentials.teleport.TeleportManager;
import xyz.jacklify.jackessentials.teleport.WarpManager;

public class JackEssentials extends JavaPlugin {

	private Logger logger = Logger.getLogger("Minecraft");
	
	private PluginConfig config;
	private TeleportManager tpManager;
	private WarpManager warpManager;
	
	@Override
	public void onEnable() {
		logger.info("[JackEssentials] Init");
		this.loadConfig();
		this.tpManager = new TeleportManager(this.config);
		this.warpManager = new WarpManager(this);
		this.registerEvents();
	}
	
	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new JackListener(this), this);
	}

	private void loadConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisable() {
		logger.info("[JackEssentials] Disabling");
	}
}
