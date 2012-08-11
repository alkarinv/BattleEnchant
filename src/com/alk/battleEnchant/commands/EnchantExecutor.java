package com.alk.battleEnchant.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.alk.battleEnchant.BattleEnchant;
import com.alk.battleEnchant.PermissionController;
import com.alk.battleEnchant.Util;
import com.alk.battleEnchant.util.InventoryUtil;

/**
 * 
 * @author alkarin
 *
 */
public class EnchantExecutor  {
	
	public boolean handleCommand(Player p, Command cmd, String commandLabel, String[] args) {
		String commandStr = cmd.getName().toLowerCase();
		int length = args.length;
		for (String arg: args){
			if (!arg.matches("[a-zA-Z0-9_/:,]*")) {
				sendMessage(p, "arguments can be only alphanumeric with underscores");
				return true;
			}
		}

		if (commandStr.equalsIgnoreCase("enc")){
			if (length > 0 && args[0].equalsIgnoreCase("list")){
				return enchantList(p,args);
			}
			return enchantItem(p,args);
		}
		
		return true;
	}	


	private boolean enchantItem(Player p, String[] args) {
		if (p !=null && !PermissionController.hasPermission(p, "enchant.admin")){
			return sendMessage(p,"&4You don't have permission for this command");
		}
		if (args.length < 1){
			sendMessage(p,"&e/e all: give all enchantments to the item you are holding ");
			sendMessage(p,"&e/e <enchantment1>[:level] <enchantment2>[:level] ... ");
			sendMessage(p,"&e/e <itemid> <enchantment1>[:level] <enchantment2>[:level] ... ");
			sendMessage(p,"&e/e <player> <itemid> <enchantment1>[:level] <enchantment2>[:level] ... ");
			sendMessage(p,"&e/e list: &fshow which enchantments can be put on the item you're holding");
			sendMessage(p,"&e/e list all: &fshow all enchantments");
			sendMessage(p,"&e/e list <item>: &f show a list of enchantments you can put on this item");
			return sendMessage(p,"&eExample: /e all");
		}
		/// Have they specified an item to enchant
		ItemStack is = InventoryUtil.getItemStack(args[0]);
		if (is != null && is.getTypeId() > 256){
			is = giveEnchantedItem(p,is,minus1Arg(args));
			String iname = InventoryUtil.getCommonName(is);
			if (is.getEnchantments() == null || is.getEnchantments().isEmpty()){
				return sendMessage(p,"&eYou have been given a " + iname);}
			return sendMessage(p,"&eYou now have an enchanted " + iname + " with " + getEnchantmentNames(is));
		}

		EnchantmentWithLevel ewl = getEnchantment(args[0]);
		if (ewl != null){
			return enchantItemInHand(p,args);
		}

		Player pl = findPlayer(args[0]);
		if (pl == null){
			sendMessage(p, "&e" + args[0] +" was not found");
			return sendMessage(p,"&e/e list: &fshow which enchantments can be put on the item you're holding");
		}
		if (args.length < 2){
			return sendMessage(p,"&e/e <player> <itemid> <enchantment1>[:level] <enchantment2>[:level] ... ");			
		}
		is = InventoryUtil.getItemStack(args[1]);
		if (is == null){
			return sendMessage(p,"&eItem " + args[1] + " not found");
		}
		is = giveEnchantedItem(pl,is,minus1Arg(args));
		if (is == null){
			return sendMessage(p,"&eCouldn't enchant item");}
		String name = pl.getName();
		String iname = Material.getMaterial(is.getTypeId()).toString();
		if (is.getEnchantments() == null || is.getEnchantments().isEmpty()){
			sendMessage(pl,"&eYou have been given a " + iname);
			return sendMessage(p,"&eYou sent " + name + " a " + iname);
		}
		sendMessage(pl,"&eYou have been given an enchanted " + iname + " with " + getEnchantmentNames(is));
		return sendMessage(p,"&eYou sent " + name + " an enchanted " + iname + " with " + getEnchantmentNames(is));
	}

	private boolean enchantList(Player p, String[] args) {
		int length = args.length;
		boolean all = length > 1 && args[1].equalsIgnoreCase("all");
		/// Try to get the item first from the command line
		ItemStack is = length > 1 ? InventoryUtil.getItemStack(args[1]) : null;
		if (is == null && !all){ /// now try getting is from the player
			is = p.getItemInHand();}
		if (!all && (is==null || is.getTypeId()==0)){
			return sendMessage(p,"&eYou can't enchant nothing");}
		
		if (!all){
			sendMessage(p,"&eValid Enchantments for " +Material.getMaterial(is.getTypeId()).toString() +":\n");			
		} else {
			sendMessage(p,"&eAll Enchantments:\n");			
		}
		HashMap<Integer,Enchantment> ordered = new HashMap<Integer,Enchantment>(); 
		for (Enchantment enc : Enchantment.values()){
			ordered.put(enc.getId(), enc);
		}
		ArrayList<Integer> ints = new ArrayList<Integer>(ordered.keySet());
		Collections.sort(ints);
		for (Enchantment enc : Enchantment.values()){
			ordered.put(enc.getId(), enc);
		}
		for (Integer eid : ints){
			Enchantment enc = Enchantment.getById(eid);
			if (all || enc.canEnchantItem(is))
				sendMessage(p,"&6"+enc.getName()+"&e(&6" + enc.getId()+"&e)[&8" + enc.getStartLevel()+"&e-&8" + enc.getMaxLevel()+"&e]");
		}


		return true;
	}

	@SuppressWarnings("deprecation")
	private ItemStack giveEnchantedItem(Player p, ItemStack is, String[] args) {
		ItemStack item = is.clone();
		if (item.getAmount() == 0) 
			item.setAmount(1);
		addEnchantments(item,args);
		p.getInventory().addItem(item);
		p.updateInventory();
		return item;
	}
	
	private String getEnchantmentNames(ItemStack is) {
		if (is == null){
			return "none";}
		Map<Enchantment, Integer> enchantments = is.getEnchantments();
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Enchantment enc : enchantments.keySet()){
			if (!first) sb.append(", ");
			sb.append(enc.getName()+":" + enchantments.get(enc));
			first = false;
		}
		return sb.toString();
	}

	private boolean enchantItemInHand(Player p, String[] args) {
		PlayerInventory pi = p.getInventory();

		ItemStack is = pi.getItemInHand();
		if (is == null || is.getTypeId() == 0){
			return sendMessage(p,"&eYou have nothing in hand!");
		}
		addEnchantments(is,args);
		return sendMessage(p,"&eAdded enchantments: " +  getEnchantmentNames(is));
	}

	
	private void addEnchantments(ItemStack is,Map<Enchantment, Integer> enchantments) {
		for (Enchantment e: enchantments.keySet()){
			if (e.canEnchantItem(is)){
				is.addUnsafeEnchantment(e, enchantments.get(e));
			}
		}
	}


	private boolean addEnchantments(ItemStack is, String[] args) {
		Map<Enchantment,Integer> encs = new HashMap<Enchantment,Integer>();
		for (String s : args){
			EnchantmentWithLevel ewl = getEnchantment(s);
			if (ewl != null){
				if (ewl.all){
					return InventoryUtil.addAllEnchantments(is);
				}					
				encs.put(ewl.e, ewl.lvl);
			}
		}
		addEnchantments(is,encs);
		return true;
	}


	private String[] minus1Arg(String[] args) {
		String rargs[] = new String[args.length -1];
		for (int i=1;i<args.length;i++){
			rargs[i-1] = args[i];
		}
		return rargs;
	}
	
	public class EnchantmentWithLevel{
		public EnchantmentWithLevel(){}
		public EnchantmentWithLevel(boolean all){this.all = all;}
		public Enchantment e;
		public Integer lvl;
		boolean all = false;
	}
	
	private EnchantmentWithLevel getEnchantment(String str) {
		if (str.equalsIgnoreCase("all")){
			return new EnchantmentWithLevel(true);
		}
		Enchantment e = null;
		str = str.replaceAll(",", ":");
        int index = str.indexOf(':');
        index = (index != -1 ? index : -1);
        int lvl = -1;
        if (index != -1){
        	try {lvl = Integer.parseInt(str.substring(index + 1)); } catch (Exception err){}
            str = str.substring(0,index);
        } 
		try {e = Enchantment.getById(Integer.valueOf(str));} catch (Exception err){}
		if (e == null)
			e = Enchantment.getByName(str);
		if (e == null)
			e = InventoryUtil.getEnchantmentByCommonName(str);
		if (e == null)
			return null;
		EnchantmentWithLevel ewl = new EnchantmentWithLevel();
		ewl.e = e;
		if (lvl < e.getStartLevel()){lvl = e.getStartLevel();}
		if (lvl > e.getMaxLevel()){lvl = e.getMaxLevel();}
		ewl.lvl = lvl;
		return ewl;
	}

	public static boolean sendMessage(Player p, String msg){
		if (msg == null)
			return false;
		if (p == null){
			BattleEnchant.info(msg);
		} else {
			p.sendMessage(Util.colorChat(msg));
		}
		return true;
	}

	private Player findPlayer(String name) {
		Server server =Bukkit.getServer();
		Player lastPlayer = server.getPlayer(name);
		if (lastPlayer != null) 
			return lastPlayer;

        Player[] online = server.getOnlinePlayers();
        for (Player player : online) {
            final String playerName = player.getName();
            if (playerName.equalsIgnoreCase(name)) {
                lastPlayer = player;
                break;
            }
            if (playerName.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                if (lastPlayer != null) {
                    return null;}
                lastPlayer = player;
            }
        }

        return lastPlayer;
	}

}
