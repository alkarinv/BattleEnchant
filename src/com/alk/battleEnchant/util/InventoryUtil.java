package com.alk.battleEnchant.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.server.Container;
import net.minecraft.server.ContainerPlayer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtil {
	static final String version = "InventoryUtil 2.1.3.5";
	static final boolean DEBUG = false;

	public static class Armor{
		public ArmorLevel level;
		public ArmorType type;
		Armor(ArmorType at, ArmorLevel al){this.level = al; this.type = at;}
	}
	public static class EnchantmentWithLevel{
		public EnchantmentWithLevel(){}
		public EnchantmentWithLevel(boolean all){this.all = all;}
		public Enchantment e;
		public Integer lvl;
		boolean all = false;
		@Override
		public String toString(){return  (e !=null?e.getName():"null")+":" + lvl;}
	}

	public enum ArmorLevel{WOOL,LEATHER,IRON,GOLD,CHAINMAIL,DIAMOND};
	public enum ArmorType{HELM,CHEST,LEGGINGS,BOOTS};

	public static Enchantment getEnchantmentByCommonName(String iname){
		iname = iname.toLowerCase();
		if (iname.contains("smite")) return Enchantment.DAMAGE_UNDEAD;
		if (iname.contains("sharp")) return Enchantment.DAMAGE_ALL;
		if (iname.contains("sharp")) return Enchantment.DAMAGE_ARTHROPODS;
		if (iname.contains("fire") && iname.contains("prot")) return Enchantment.PROTECTION_FIRE;
		if (iname.contains("fire")) return Enchantment.FIRE_ASPECT;
		if (iname.contains("exp") && iname.contains("prot")) return Enchantment.PROTECTION_EXPLOSIONS;
		if (iname.contains("blast") && iname.contains("prot")) return Enchantment.PROTECTION_EXPLOSIONS;
		if (iname.contains("arrow") && iname.contains("prot")) return Enchantment.PROTECTION_PROJECTILE;
		if (iname.contains("proj") && iname.contains("prot")) return Enchantment.PROTECTION_PROJECTILE;
		if (iname.contains("respiration")) return Enchantment.OXYGEN;
		if (iname.contains("fall")) return Enchantment.PROTECTION_FALL;
		if (iname.contains("prot")) return Enchantment.PROTECTION_ENVIRONMENTAL;
		if (iname.contains("respiration")) return Enchantment.OXYGEN;
		if (iname.contains("oxygen")) return Enchantment.OXYGEN;
		if (iname.contains("aqua")) return Enchantment.WATER_WORKER;
		if (iname.contains("arth")) return Enchantment.DAMAGE_ARTHROPODS;
		if (iname.contains("knockback")) return Enchantment.KNOCKBACK;
		if (iname.contains("loot")) return Enchantment.LOOT_BONUS_MOBS;
		if (iname.contains("dig")) return Enchantment.DIG_SPEED;
		if (iname.contains("eff")) return Enchantment.DIG_SPEED;
		if (iname.contains("flame")) return Enchantment.ARROW_FIRE;
		if (iname.contains("power")) return Enchantment.ARROW_DAMAGE;
		if (iname.contains("punch")) return Enchantment.ARROW_KNOCKBACK;
		if (iname.contains("inf")) return Enchantment.ARROW_INFINITE;
		if (iname.contains("silk")) return Enchantment.SILK_TOUCH;
		if (iname.contains("unbreaking")) return Enchantment.DURABILITY;
		if (iname.contains("dura")) return Enchantment.DURABILITY;
		return null;
	}
	public static String getCommonNameByEnchantment(Enchantment enc){
		if (enc.getId() == Enchantment.PROTECTION_ENVIRONMENTAL.getId()){return "Protection";}
		else if (enc.getId() == Enchantment.PROTECTION_FIRE.getId()){return "Fire Protection";}
		else if (enc.getId() == Enchantment.PROTECTION_FALL.getId()){return "Fall Protection";}
		else if (enc.getId() == Enchantment.PROTECTION_EXPLOSIONS.getId()){return "Blast Protection";}
		else if (enc.getId() == Enchantment.PROTECTION_PROJECTILE.getId()){return "Projectile Protection";}
		else if (enc.getId() == Enchantment.OXYGEN.getId()){return "Respiration";}
		else if (enc.getId() == Enchantment.WATER_WORKER.getId()){return "Aqua Affinity";}
		else if (enc.getId() == Enchantment.DAMAGE_ALL.getId()){return "Sharp";}
		else if (enc.getId() == Enchantment.DAMAGE_UNDEAD.getId()){return "Smite";}
		else if (enc.getId() == Enchantment.DAMAGE_ARTHROPODS.getId()){return "Bane of Arthropods";}
		else if (enc.getId() == Enchantment.KNOCKBACK.getId()){return "Knockback";}
		else if (enc.getId() == Enchantment.FIRE_ASPECT.getId()){return "Fire Aspect";}
		else if (enc.getId() == Enchantment.LOOT_BONUS_MOBS.getId()){return "Looting";}
		else if (enc.getId() == Enchantment.DIG_SPEED.getId()){return "Efficiency";}
		else if (enc.getId() == Enchantment.SILK_TOUCH.getId()){return "Silk Touch";}
		else if (enc.getId() == Enchantment.DURABILITY.getId()){return "Unbreaking";}
		else if (enc.getId() == Enchantment.LOOT_BONUS_BLOCKS.getId()){return "Fortune";}
		else if (enc.getId() == Enchantment.ARROW_DAMAGE.getId()){return "Power";}
		else if (enc.getId() == Enchantment.ARROW_KNOCKBACK.getId()){return "Punch";}
		else if (enc.getId() == Enchantment.ARROW_FIRE.getId()){return "Flame";}
		else if (enc.getId() == Enchantment.ARROW_INFINITE.getId()){return "Infinity";}
		else return enc.getName();
	}


	static final HashMap<Material,Armor> armor;
	static {
		armor = new HashMap<Material,Armor>();
		armor.put(Material.WOOL,new Armor(ArmorType.HELM, ArmorLevel.WOOL));
		armor.put(Material.LEATHER_HELMET,new Armor(ArmorType.HELM, ArmorLevel.LEATHER));
		armor.put(Material.IRON_HELMET,new Armor(ArmorType.HELM, ArmorLevel.IRON));
		armor.put(Material.GOLD_HELMET,new Armor(ArmorType.HELM, ArmorLevel.GOLD));
		armor.put(Material.DIAMOND_HELMET,new Armor(ArmorType.HELM, ArmorLevel.DIAMOND));
		armor.put(Material.CHAINMAIL_HELMET,new Armor(ArmorType.HELM, ArmorLevel.CHAINMAIL));

		armor.put(Material.LEATHER_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.LEATHER));
		armor.put(Material.IRON_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.IRON));
		armor.put(Material.GOLD_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.GOLD));
		armor.put(Material.DIAMOND_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.DIAMOND));
		armor.put(Material.CHAINMAIL_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.CHAINMAIL));

		armor.put(Material.LEATHER_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.LEATHER));
		armor.put(Material.IRON_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.IRON));
		armor.put(Material.GOLD_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.GOLD));
		armor.put(Material.DIAMOND_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.DIAMOND));
		armor.put(Material.CHAINMAIL_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.CHAINMAIL));

		armor.put(Material.LEATHER_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.LEATHER));
		armor.put(Material.IRON_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.IRON));
		armor.put(Material.GOLD_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.GOLD));
		armor.put(Material.DIAMOND_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.DIAMOND));
		armor.put(Material.CHAINMAIL_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.CHAINMAIL));
	}

	public static int arrowCount(Player p) {
		return getItemAmount(p.getInventory().getContents(), new ItemStack(Material.ARROW,1));
	}

	public static int getItemAmountFromInventory(Inventory inv, ItemStack is) {
		return getItemAmount(inv.getContents(), is);
	}

	public static boolean hasArmor(Player p) {
		return(	p.getInventory().getBoots().getType() != Material.AIR &&
				p.getInventory().getHelmet().getType() != Material.AIR &&
				p.getInventory().getLeggings().getType() != Material.AIR &&
				p.getInventory().getChestplate().getType() != Material.AIR );
	}

	public static ArmorLevel hasArmorSet(Player p) {
		return hasArmorSet(p.getInventory());
	}
	public static ArmorLevel hasArmorSet(Inventory inv) {
		ArmorLevel armorSet[] = new ArmorLevel[4];
		for (ItemStack is: inv){
			Armor a = armor.get(is);
			if (a == null)
				continue;
			switch (a.type){
			case BOOTS: armorSet[0] = a.level; break;
			case LEGGINGS: armorSet[1] = a.level; break;
			case CHEST: armorSet[2] = a.level; break;
			case HELM: armorSet[3] = a.level; break;
			}
		}
		ArmorLevel lvl = null;
		for (ArmorLevel a: armorSet){
			if (lvl == null)
				lvl = a;
			else if (lvl != a)
				return null;
		}
		return lvl;
	}
	//
	//	private static Material getMaterial(ArmorLevel level) {
	//		switch(level){
	//		case WOOL: return Material.WOOL;
	//		case LEATHER : return Material.LEATHER;
	//		case IRON: return Material.IRON_BOOTS;
	//		case GOLD : return Material.GOLD_BOOTS;
	//		case CHAINMAIL: return Material.CHAINMAIL_BOOTS;
	//		case DIAMOND: return Material.DIAMOND;
	//		default : return null;
	//		}
	//	}

	public static int getItemAmount(ItemStack[] items, ItemStack is){
		int count = 0;
		for (ItemStack item : items) {
			if (item == null) {
				continue;}
			if (item.getType() == is.getType() && ((item.getDurability() == is.getDurability() || item.getDurability() == -1) )) {
				count += item.getAmount();
			}
		}
		return count;
	}

	public static ItemStack getItemStack(String name) {
		if (name == null || name.isEmpty())
			return null;
		name = name.replace(" ", "_");
		name = name.replace(":", ";");

		int dataIndex = name.indexOf(';');
		dataIndex = (dataIndex != -1 ? dataIndex : -1);
		int dataValue = 0;
		if (dataIndex != -1){
			dataValue = (isInt(name.substring(dataIndex + 1)) ? Integer.parseInt(name.substring(dataIndex + 1)) : 0);
			name = name.substring(0,dataIndex);
		}

		dataValue = dataValue < 0 ? 0 : dataValue;
		Material mat = getMat(name);

		if (mat != null && mat != Material.AIR) {
			return new ItemStack(mat, 0, (short) dataValue);
		}
		return null;
	}

	public static boolean isInt(String i) {try {Integer.parseInt(i);return true;} catch (Exception e) {return false;}}
	public static boolean isFloat(String i){try{Float.parseFloat(i);return true;} catch (Exception e){return false;}}

	/// Get the Material
	public static Material getMat(String name) {
		Integer id =null;
		try{ id = Integer.parseInt(name);}catch(Exception e){}
		if (id == null){
			id = getMaterialID(name);}
		return id != null && id >= 0 ? Material.getMaterial(id) : null;
	}

	/// This allows for abbreviations to work, useful for sign etc
	public static int getMaterialID(String name) {
		name = name.toUpperCase();
		/// First try just getting it from the Material Name
		Material mat = Material.getMaterial(name);
		if (mat != null)
			return mat.getId();
		/// Might be an abbreviation, or a more complicated
		int temp = Integer.MAX_VALUE;
		mat = null;
		name = name.replaceAll("\\s+", "").replaceAll("_", "");
		for (Material m : Material.values()) {
			if (m.name().replaceAll("_", "").startsWith(name)) {
				if (m.name().length() < temp) {
					mat = m;
					temp = m.name().length();
				}
			}
		}
		return mat != null ? mat.getId() : -1;
	}

	public static boolean hasItem(Player p, ItemStack itemType) {
		PlayerInventory inv = p.getInventory();
		for (ItemStack is : inv.getContents()){
			if (is != null && is.getType() == itemType.getType()){
				return true;}
		}
		for (ItemStack is : inv.getArmorContents()){
			if (is != null && is.getType() == itemType.getType()){
				return true;}
		}
		return false;
	}

	public static boolean hasAnyItem(Player p) {
		PlayerInventory inv = p.getInventory();
		for (ItemStack is : inv.getContents()){
			if (is != null && is.getType() != Material.AIR){
				//				System.out.println("item=" + is);
				return true;}
		}
		for (ItemStack is : inv.getArmorContents()){
			if (is != null && is.getType() != Material.AIR){
				//				System.out.println("item=" + is);
				return true;}
		}

		EntityHuman eh = ((CraftPlayer)p).getHandle();
		try {
			/// check crafting square
			ContainerPlayer cp = (ContainerPlayer) eh.defaultContainer;
			for (net.minecraft.server.ItemStack is: cp.craftInventory.getContents()){
				if (is != null && is.id != 0)
					return true;
			}
			/// Check for a workbench
			Container container = eh.activeContainer;
			final int size = container.b.size();
			for (int i=0;i< size;i++){
				net.minecraft.server.ItemStack is = container.getSlot(i).getItem();
				if (is != null && is.id != 0)
					return true;
			}

		} catch (Exception e){}
		return false;
	}

	public static void addItemToInventory(Player player, ItemStack itemStack) {
		addItemToInventory(player,itemStack,itemStack.getAmount(),true);
	}

	public static void addItemToInventory(Player player, ItemStack itemStack, int stockAmount) {
		addItemToInventory(player,itemStack,stockAmount,true);
	}
	@SuppressWarnings("deprecation")
	public static void addItemToInventory(Player player, ItemStack itemStack, int stockAmount, boolean update) {
		PlayerInventory inv = player.getInventory();
		Material itemType =itemStack.getType();
		if (armor.containsKey(itemType)){
			/// no item: add to armor slot
			/// item better: add old to inventory, new to armor slot
			/// item notbetter: add to inventory
			ItemStack oldArmor = getArmorSlot(inv,armor.get(itemType));
			boolean empty = (oldArmor == null || oldArmor.getType() == Material.AIR);
			boolean better = empty ? false : armorSlotBetter(armor.get(oldArmor.getType()),armor.get(itemType));
			if (empty || better){
				switch (armor.get(itemType).type){
				case HELM: inv.setHelmet(itemStack); break;
				case CHEST: inv.setChestplate(itemStack); break;
				case LEGGINGS: inv.setLeggings(itemStack); break;
				case BOOTS: inv.setBoots(itemStack); break;
				}
			}
			if (!empty){
				if (better){
					addItemToInventory(inv, oldArmor,oldArmor.getAmount());
				} else {
					addItemToInventory(inv, itemStack,stockAmount);
				}
			}
		} else {
			addItemToInventory(inv, itemStack,stockAmount);
		}
		if (update)
			try { player.updateInventory(); } catch (Exception e){}
	}

	private static boolean armorSlotBetter(Armor oldArmor, Armor newArmor) {
		if (oldArmor == null || newArmor == null) /// technically we could throw an exception.. but nah
			return false;
		return oldArmor.level.ordinal() < newArmor.level.ordinal();
	}

	private static ItemStack getArmorSlot(PlayerInventory inv, Armor armor) {
		switch (armor.type){
		case HELM: return inv.getHelmet();
		case CHEST: return inv.getChestplate();
		case LEGGINGS: return inv.getLeggings();
		case BOOTS:return inv.getBoots();
		}
		return null;
	}

	///Adds item to inventory
	public static void addItemToInventory(Inventory inv, ItemStack is, int left){
		int maxStackSize = is.getType().getMaxStackSize();
		if(left <= maxStackSize){
			is.setAmount(left);
			inv.addItem(is);
			return;
		}

		if(maxStackSize != 64){
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (int i = 0; i < Math.ceil(left / maxStackSize); i++) {
				if (left < maxStackSize) {
					is.setAmount(left);
					items.add(is);
					return;
				}else{
					is.setAmount(maxStackSize);
					items.add(is);
				}
			}
			Object[] iArray = items.toArray();
			for(Object o : iArray){
				inv.addItem((ItemStack) o);
			}
		}else{
			inv.addItem(is);
		}
	}

	public static void closeInventory(Player p) {
		EntityHuman eh = ((CraftPlayer)p).getHandle();
		try{
			if ((eh instanceof EntityPlayer)){
				EntityPlayer ep = (EntityPlayer) eh;
				ep.closeInventory();
			}
		}catch(Exception closeInventoryError){
			/// This almost always throws an NPE, but does its job so ignore
		}
	}

	public static void clearInventory(Player p) {
		try{
			PlayerInventory inv = p.getInventory();
			closeInventory(p);
			EntityHuman eh = ((CraftPlayer)p).getHandle();
			if (inv != null){
				inv.clear();
				inv.setArmorContents(null);
				inv.setItemInHand(null);
				try {
					/// get rid of items inside the crafting square
					ContainerPlayer cp = (ContainerPlayer) eh.defaultContainer;
					int size = cp.craftInventory.getContents().length;

					for (int i=0;i< size;i++){
						cp.craftInventory.setItem(i, null);
					}
					/// Check for a workbench, dispenser
					/// No need for this now, closing inventory before this does the trick
					//				Container container = (Container) eh.activeContainer;
					//				size = container.e.size();
					//				for (int i=0;i< size;i++){
					//					net.minecraft.server.ItemStack is = container.a(i);
					//					if (is != null && is.id != 0){
					//						is.id =0;
					//						is.count =0;
					//					}
					//
					//				}

				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch(Exception ee){
			ee.printStackTrace();
		}
	}

	public static String getCommonName(ItemStack is) {
		int id = is.getTypeId();
		int datavalue = is.getDurability();
		if (datavalue > 0){
			return Material.getMaterial(id).toString() + ":" + datavalue;
		}
		return Material.getMaterial(id).toString();
	}

	@SuppressWarnings("deprecation")
	public static void addItemsToInventory(Player p, List<ItemStack> items) {
		for (ItemStack is : items)
			InventoryUtil.addItemToInventory(p, is.clone(), is.getAmount(), false);
				try { p.updateInventory(); } catch (Exception e){}
	}

	public static ItemStack parseItem(String str) throws Exception{
		//		System.out.println("string = " + str);
		str = str.replaceAll("[}{]", "");
		str = str.replaceAll("=", " ");
		if (DEBUG) System.out.println("item=" + str);
		ItemStack is =null;
		try{
			String split[] = str.split(" ");
			is = InventoryUtil.getItemStack(split[0].trim());
			is.setAmount(Integer.valueOf(split[split.length -1]));
			for (int i = 1; i < split.length-1;i++){
				EnchantmentWithLevel ewl = getEnchantment(split[i].trim());
				try {
					is.addEnchantment(ewl.e, ewl.lvl);
				} catch (IllegalArgumentException iae){
					Logger.getLogger("minecraft").warning(ewl+" can not be applied to the item " + str);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			Logger.getLogger("minecraft").severe("Couldnt parse item=" + str);
			throw new Exception("parse item was bad");
		}
		return is;
	}

	public static EnchantmentWithLevel getEnchantment(String str) {
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

		//        System.out.println("String = <" + str +">   " + lvl);
		try {e = Enchantment.getById(Integer.valueOf(str));} catch (Exception err){}
		if (e == null)
			e = Enchantment.getByName(str);
		if (e == null)
			e = getEnchantmentByCommonName(str);
		if (e == null)
			return null;
		EnchantmentWithLevel ewl = new EnchantmentWithLevel();
		ewl.e = e;
		if (lvl < e.getStartLevel()){lvl = e.getStartLevel();}
		if (lvl > e.getMaxLevel()){lvl = e.getMaxLevel();}
		ewl.lvl = lvl;
		return ewl;
	}

	public static boolean addEnchantments(ItemStack is, String[] args) {
		Map<Enchantment,Integer> encs = new HashMap<Enchantment,Integer>();
		for (String s : args){
			EnchantmentWithLevel ewl = getEnchantment(s);
			if (ewl != null){
				if (ewl.all){
					return addAllEnchantments(is);}
				encs.put(ewl.e, ewl.lvl);
			}
		}
		addEnchantments(is,encs);
		return true;
	}

	public static void addEnchantments(ItemStack is,Map<Enchantment, Integer> enchantments) {
		for (Enchantment e: enchantments.keySet()){
			if (e.canEnchantItem(is)){
				is.addUnsafeEnchantment(e, enchantments.get(e));
			}
		}
	}

	public static boolean addAllEnchantments(ItemStack is) {
		for (Enchantment enc : Enchantment.values()){
			if (enc.canEnchantItem(is)){
				is.addUnsafeEnchantment(enc, enc.getMaxLevel());
			}
		}
		return true;
	}

	/**
	 * For Serializing an item or printing
	 * @param is
	 * @return
	 */
	public static String getItemString(ItemStack is) {
		StringBuilder sb = new StringBuilder();
		sb.append(is.getType().toString() +":"+is.getData().getData()+" ");
		Map<Enchantment,Integer> encs = is.getEnchantments();
		for (Enchantment enc : encs.keySet()){
			sb.append(enc.getName() + ":" + encs.get(enc)+" ");
		}
		sb.append(is.getAmount());
		return sb.toString();
	}

	public static boolean hasEnchantedItem(Player p) {
		PlayerInventory inv = p.getInventory();
		for (ItemStack is : inv.getContents()){
			if (is != null && is.getType() != Material.AIR ){
				Map<Enchantment,Integer> enc = is.getEnchantments();
				if (enc != null && !enc.isEmpty())
					return true;
			}
		}
		for (ItemStack is : inv.getArmorContents()){
			if (is != null && is.getType() != Material.AIR){
				Map<Enchantment,Integer> enc = is.getEnchantments();
				if (enc != null && !enc.isEmpty())
					return true;
			}
		}
		return false;
	}


}
