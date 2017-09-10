package es.darkhorizon.dev;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class chestlevels extends JavaPlugin implements Listener{
	private static Economy economy = null;	
	private static chestlevels plugin;
	private lvls_menus invmenu;
	public static chestlevels getPlugin() {return plugin;}
	ArrayList<String> Locations = new ArrayList<String>();
	HashMap<String, Integer> plvls = new HashMap<String, Integer>();
	String plversion = "1.0.1";
	@Override
	public void onEnable() {
		plugin = this;
		setupEconomy();
		Bukkit.getPluginManager().registerEvents(new lvls_event(this), this);
		Bukkit.getPluginCommand("lchest").setExecutor(new lchestcmd(this));
		Bukkit.getPluginManager().registerEvents(this, this);
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();	
		getConfig().set("version", plversion);
		
		saveConfig();
		reloadConfig();	
		console.sendMessage("§8§l§m===========================================================================");
		console.sendMessage("");
		console.sendMessage("§8[§d§lCHESTLEVELS§r§8] §7You're running the Version §b§l" + plversion + "§r §7of ChestLevels!");	
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {console.sendMessage("§8[§d§lCHESTLEVELS§r§8] §7Plugin linked with §eVault§r§7!");}	
		console.sendMessage("");
		console.sendMessage("§8§l§m===========================================================================");	
		
		File db = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "db.yml");	
		FileConfiguration dbfile = YamlConfiguration.loadConfiguration(db);
		if (!db.exists()) { try {			
			dbfile.createSection("locations");		
			dbfile.createSection("players");
			dbfile.save(db);					
		} catch (IOException exception) { exception.printStackTrace(); } } 
		
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		if (!l.exists()) { try {			
			lfile.set("prefix", "&8[&d&lCHESTLEVELS&r&8] &r");	
			lfile.set("noperm", "&cNo Permission!");
			lfile.set("sender", "&cOnly for players!");
			lfile.set("no_money", "&cYou don't have enought money!");
			lfile.set("lchestcmd.help.help_cmd", "&f&l»&r &9/lchest help &7Show this info.");	
			lfile.set("lchestcmd.help.setup_cmd", "&f&l»&r &9/lchest setup &7Setups a custom lvlchest.");
			lfile.set("lchestcmd.setup.usage", "&7Correct usage: &f/lchest setup");
			lfile.set("lchestcmd.setup.no_chest", "&cYou're not looking to a chest!");
			lfile.set("lchestcmd.setup.already_chest", "&cActually this chest is been managed by ChestLevels!");
			lfile.set("lchestcmd.setup.success", "&cNow this chest is managed by ChestLevels!");
			lfile.set("lchestcmd.setup.removed", "&cChest removed!");
			lfile.set("chest.title", "&9&lCHEST&r &7&lLVL.&r&b&l%level%");
			
			lfile.set("chest.upgrade.name", "&e&lUpgrade Chest!");
			ArrayList<String> lvl1 = new ArrayList<String>();
			lvl1.clear();			
			lvl1.add("&8");
			lvl1.add("&7Upgrade your chest to next level for:");
			lvl1.add("&e%cost%");
			lvl1.add("&8");
			lfile.set("chest.upgrade.lore", lvl1);
			
			lfile.set("chest.max_level.name", "&c&lMax Level!");
			lvl1 = new ArrayList<String>();
			lvl1.clear();			
			lvl1.add("&8");
			lvl1.add("&7You can't upgrade your chest!");
			lvl1.add("&8");
			lfile.set("chest.max_level.lore", lvl1);
			
			
			lfile.save(l);					
		} catch (IOException exception) { exception.printStackTrace(); } } 
		
		File lvl = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "levels.yml");	
		FileConfiguration lvlfile = YamlConfiguration.loadConfiguration(lvl);
		if (!lvl.exists()) { try {			
			lvlfile.createSection("levels");	
			lvlfile.set("levels.total", 2);
			
			ArrayList<String> lvl1 = new ArrayList<String>();
			lvl1.clear();
			lvl1.add("4;306;1");
			lvl1.add("13;307;1");
			lvl1.add("22;308;1");
			lvl1.add("31;309;1");
			lvl1.add("12;267;1");
			lvl1.add("14;364;16");
			lvlfile.set("levels.1.items", lvl1);
			
			lvl1 = new ArrayList<String>();
			lvlfile.set("levels.2.upgrade_cost", 1000.0);
			lvl1.clear();			
			lvl1.add("1;322:1;64");
			lvlfile.set("levels.2.items", lvl1);
			
			lvlfile.save(lvl);					
		} catch (IOException exception) { exception.printStackTrace(); } } 
		for (String loc : dbfile.getStringList("locations")) {
			Locations.add(loc);			
		}
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			if (dbfile.getString("players." + p.getName().toString()) != null) {plugin.plvls.put(p.getName().toString(), dbfile.getInt("players." + p.getName().toString()));}		
		}
		this.invmenu = new lvls_menus(this);
	}
	@Override
	public void onDisable() {getLogger().info("[CHESTLEVELS] Plugin Disabled!");}	
	public static Economy getEconomy() {return economy;}	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	public lvls_menus getInvMenus() {
		return this.invmenu; 
	}
}
