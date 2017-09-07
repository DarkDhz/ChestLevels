package es.darkhorizon.dev;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
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
	String plversion = "1.0";
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
		
		File lvl = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "levels.yml");	
		FileConfiguration lvlfile = YamlConfiguration.loadConfiguration(lvl);
		if (!lvl.exists()) { try {			
			lvlfile.createSection("levels");	
			lvlfile.set("levels.total", 1);
			ArrayList<String> lvl1 = new ArrayList<String>();
			lvl1.add("0;2;64");
			lvl1.add("1;4;64");
			lvlfile.set("levels.1.items", lvl1);
			lvlfile.save(lvl);					
		} catch (IOException exception) { exception.printStackTrace(); } } 
		
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
