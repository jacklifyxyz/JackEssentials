package xyz.jacklify.jackessentials;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.jacklify.jackessentials.teleport.TeleportManager;
import xyz.jacklify.jackessentials.teleport.WarpManager;

public class JackEssentials extends JavaPlugin {

	private Logger logger = Logger.getLogger("Minecraft");
	
	private TeleportManager tpManager;
	private WarpManager warpManager;
	
	@Override
	public void onEnable() {
		logger.info("[JackEssentials] Init");
	}
	
	@Override
	public void onDisable() {
		logger.info("[JackEssentials] Disabling");
	}
}
