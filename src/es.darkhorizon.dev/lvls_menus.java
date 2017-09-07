package es.darkhorizon.dev;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class lvls_menus {
	private final chestlevels plugin;
	public lvls_menus(chestlevels plugin) { this.plugin = plugin; }
	public Inventory openChestInventory (Player p, Integer level) {
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("prefix").replaceAll("&", "§");
		File lvl = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "levels.yml");	
		FileConfiguration lvlfile = YamlConfiguration.loadConfiguration(lvl);
		Inventory inv = Bukkit.createInventory(p, 9 * 6, lfile.getString("chest.title").replaceAll("&", "§").replaceAll("%level%", level.toString()) );	
		if (level == 1)  {
			for ( String it : lvlfile.getStringList("levels.1.items") ) {
				String itArgs[] = it.split(";");										
				int slot = Integer.parseInt(itArgs[0]);
				int qt = Integer.parseInt(itArgs[2]);
				if (slot==45 | slot==46 | slot==47 | slot==48 | slot==49 | slot==50 | slot==51 | slot==52 | slot==53) {
					p.sendMessage(prefix + "§cError, last row can't be used!");
				} else if (qt>=65 | qt<=0) {
					p.sendMessage(prefix + "§cError, invalid item quantity!");
				} else {
					ItemStack item = parseString(itArgs[1]);				
					item.setAmount(qt);
					inv.setItem(slot, item);
				}				
			}
			ArrayList<String> Lore = new ArrayList<String>();
			ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(lfile.getString("chest.upgrade.name").replaceAll("&", "§"));  
			Lore.clear();
			for (String line : lfile.getStringList("chest.upgrade.lore")) {
				Lore.add(line.replaceAll("&", "§").replaceAll("%cost%", lvlfile.getString("levels.2.upgrade_cost")));
			}
			meta.setLore(Lore);
			item.setItemMeta(meta);	
			inv.setItem(49, item);			
		} else {
			for ( String it : lvlfile.getStringList("levels." + plugin.plvls.get(p.getName().toString()) + ".items") ) {
				String itArgs[] = it.split(";");										
				int slot = Integer.parseInt(itArgs[0]);
				int qt = Integer.parseInt(itArgs[2]);
				if (slot==45 | slot==46 | slot==47 | slot==48 | slot==49 | slot==50 | slot==51 | slot==52 | slot==53) {
					p.sendMessage(prefix + "§cError, last row can't be used!");
				} else if (qt>=65 | qt<=0) {
					p.sendMessage(prefix + "§cError, invalid item quantity!");
				} else {
					ItemStack item = parseString(itArgs[1]);				
					item.setAmount(qt);
					inv.setItem(slot, item);
				}				
			}
			if (plugin.plvls.get(p.getName().toString()) >= lvlfile.getInt("levels.total")) {
				ArrayList<String> Lore = new ArrayList<String>();
				ItemStack item = new ItemStack(Material.WOOL, 1, (short) 14);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(lfile.getString("chest.max_level.name").replaceAll("&", "§")); 
				Lore.clear();
				for (String line : lfile.getStringList("chest.max_level.lore")) {
					Lore.add(line.replaceAll("&", "§"));
				}								
				meta.setLore(Lore);
				item.setItemMeta(meta);	 
				inv.setItem(49, item);
			} else {
				ArrayList<String> Lore = new ArrayList<String>();
				ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(lfile.getString("chest.upgrade.name").replaceAll("&", "§")); 
				Lore.clear();
				for (String line : lfile.getStringList("chest.upgrade.lore")) {
					Lore.add(line.replaceAll("&", "§").replaceAll("%cost%", lvlfile.getString("levels." + plugin.plvls.get(p.getName().toString()) + ".upgrade_cost")));
				}
				meta.setLore(Lore);
				item.setItemMeta(meta);	
				inv.setItem(49, item);
			}		
		}
		return inv; 		
	}
	public ItemStack getHead(Player p) {
		ItemStack item = new ItemStack((Material.SKULL_ITEM), 1, (short) 3);
		SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(p.getName());	
		return item;
	}
	public ItemStack parseString(String itemId) {
	    String[] parts = itemId.split(":");
	    int matId = Integer.parseInt(parts[0]);
	    if (parts.length == 2) {
	        short data = Short.parseShort(parts[1]);
	        return new ItemStack(Material.getMaterial(matId), 1, data);
	    }
	    return new ItemStack(Material.getMaterial(matId));
	}
}
