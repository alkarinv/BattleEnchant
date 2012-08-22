package com.alk.battleEnchant;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.alk.battleEnchant.commands.EnchantExecutor;

public class BattleEnchant extends JavaPlugin{

	static private String pluginname; 
	static private String version;
	static private BattleEnchant plugin;
	private static Logger log = null;
	
	@Override
	public void onEnable() {
		plugin = this;
		log = Bukkit.getLogger();
		PluginDescriptionFile pdfFile = plugin.getDescription();
		pluginname = pdfFile.getName();
		version = pdfFile.getVersion();
		getCommand("enc").setExecutor(new EnchantExecutor());        		
		info("[" + pluginname + " v" + version +"]"  + " enabled!");		
	}

	@Override
	public void onDisable() {
		
	}
	
	public static void info(String msg){log.info(Util.colorChat(msg));}
	public static void warn(String msg){log.warning(Util.colorChat(msg));}
	public static void err(String msg){log.severe(Util.colorChat(msg));}
}
