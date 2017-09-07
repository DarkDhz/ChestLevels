package es.darkhorizon.dev;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class lvls_event implements Listener{
	private final chestlevels plugin;
	public lvls_event(chestlevels plugin) { this.plugin = plugin; }
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock().getType() == Material.CHEST) {
			Location loc = event.getClickedBlock().getLocation();
			if (plugin.Locations.contains(loc.toString())) {
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					event.setCancelled(true);			
					if (plugin.plvls.get(player.getName().toString()) == null) {
						Inventory open_inventory = this.plugin.getInvMenus().openChestInventory(player, 1);
						player.openInventory(open_inventory); 
					} else {
						int lvl = plugin.plvls.get(player.getName().toString());
						Inventory open_inventory = this.plugin.getInvMenus().openChestInventory(player, lvl);
						player.openInventory(open_inventory); 
					}	
				}									
			}
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("prefix").replaceAll("&", "§");
		File db = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "db.yml");	
		FileConfiguration dbfile = YamlConfiguration.loadConfiguration(db);
		File lvl = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "levels.yml");	
		FileConfiguration lvlfile = YamlConfiguration.loadConfiguration(lvl);
		Player player = (Player) event.getWhoClicked(); 
		ItemStack clicked = event.getCurrentItem(); 
		//Inventory inventory = event.getInventory();
		if(event.getCurrentItem() == null){return;}
		if (clicked.hasItemMeta()) {
			if (clicked.getItemMeta().getDisplayName().equals(lfile.getString("chest.upgrade.name").replaceAll("&", "§"))) {
				event.setCancelled(true);
				if (plugin.plvls.get(player.getName().toString()) == null) {
					if (plugin.getEconomy().getBalance(player.getName()) >= dbfile.getInt("levels.2.cost")) {
						plugin.getEconomy().withdrawPlayer(player.getName(), dbfile.getInt("levels.2.cost"));
						player.closeInventory();
						plugin.plvls.put(player.getName().toString(), 2);
						dbfile.set("players." + player.getName().toString(), 2);
						try {dbfile.save(db);} catch (IOException exception) {exception.printStackTrace();}  
						Inventory open_inventory = this.plugin.getInvMenus().openChestInventory(player, plugin.plvls.get(player.getName().toString()));
						player.openInventory(open_inventory); 
						
					} else {player.sendMessage(prefix + lfile.getString("no_money").replaceAll("&", "§"));}						
				} else {
					if (!(plugin.plvls.get(player.getName().toString()) >= lvlfile.getInt("levels.total"))) {
						if (plugin.getEconomy().getBalance(player.getName()) >= dbfile.getInt("levels." + (plugin.plvls.get(player.getName().toString()+1) + ".cost"))) {
							plugin.getEconomy().withdrawPlayer(player.getName(), dbfile.getInt("levels." + (plugin.plvls.get(player.getName().toString()+1) + ".cost")));
							player.closeInventory();
							
							plugin.plvls.replace(player.getName().toString(), plugin.plvls.get(player.getName().toString()), plugin.plvls.get(player.getName().toString())+1);
							dbfile.set("players." + player.getName().toString(), plugin.plvls.get(player.getName().toString()));
							try {dbfile.save(db);} catch (IOException exception) {exception.printStackTrace();} 
							Inventory open_inventory = this.plugin.getInvMenus().openChestInventory(player, plugin.plvls.get(player.getName().toString()));
							player.openInventory(open_inventory); 							
							
						} else {player.sendMessage(prefix + lfile.getString("no_money").replaceAll("&", "§"));}																
					} else {player.sendMessage(prefix + "MAX LEVEL!");}
				}			
			} else if (clicked.getItemMeta().getDisplayName().equals(lfile.getString("chest.max_level.name").replaceAll("&", "§"))) {
				event.setCancelled(true);					
			}
		}	
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("prefix").replaceAll("&", "§");
		File db = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "db.yml");	
		FileConfiguration dbfile = YamlConfiguration.loadConfiguration(db);
		if (plugin.Locations.contains(event.getBlock().getLocation().toString())) {
			if (event.getPlayer().hasPermission("lchest.command.setup") | event.getPlayer().hasPermission("lchest.command.*") | event.getPlayer().hasPermission("lchest.*")) {
				plugin.Locations.remove(event.getBlock().getLocation().toString());
				dbfile.set("locations", plugin.Locations);
				try {
					dbfile.save(db);
				} catch (IOException exception) { exception.printStackTrace(); }
				event.getPlayer().sendMessage(prefix + lfile.getString("lchestcmd.setup.removed").replaceAll("&", "§"));
			} else {event.setCancelled(true);}			
		}						
	}		
}
