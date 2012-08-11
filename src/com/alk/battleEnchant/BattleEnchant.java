package com.alk.battleEnchant;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.alk.battleEnchant.commands.EnchantExecutor;

public class BattleEnchant extends JavaPlugin{

	static private String pluginname; 
	static private String version;
	static private BattleEnchant plugin;
	private static Logger log = null;
	private EnchantExecutor commandController = new EnchantExecutor();
	
	@Override
	public void onEnable() {
		plugin = this;
		log = Bukkit.getLogger();
		PluginDescriptionFile pdfFile = plugin.getDescription();
		pluginname = pdfFile.getName();
		version = pdfFile.getVersion();
		File dir = this.getDataFolder();
        if (!dir.exists()){
        	dir.mkdirs();}
        
		ConfigController cc = new ConfigController();
        cc.setConfig(Util.load(getClass().getResourceAsStream("/default_files/config.yml"),dir.getPath() +"/config.yml"));
		
		PermissionController.loadPermissionsPlugin();

		info("[" + pluginname + " v" + version +"]"  + " enabled!");		
	}

	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}

		return commandController.handleCommand( player,cmd,commandLabel, args);
	}


	public static void info(String msg){log.info(Util.colorChat(msg));}
	public static void warn(String msg){log.warning(Util.colorChat(msg));}
	public static void err(String msg){log.severe(Util.colorChat(msg));}
}
