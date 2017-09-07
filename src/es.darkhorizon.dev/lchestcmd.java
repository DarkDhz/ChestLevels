package es.darkhorizon.dev;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class lchestcmd implements CommandExecutor {
	private final chestlevels plugin;
	public lchestcmd(chestlevels plugin) { this.plugin = plugin; }
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lchest")) {
			File l = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "lang.yml");	
			FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
			String prefix = lfile.getString("prefix").replaceAll("&", "§");
			File db = new File(Bukkit.getServer().getPluginManager().getPlugin("ChestLevels").getDataFolder(), "db.yml");	
			FileConfiguration dbfile = YamlConfiguration.loadConfiguration(db);
			plugin.getConfig();
			Player p = (Player) sender;
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + lfile.getString("sender").replaceAll("&", "§"));
			} else {
				if(args.length == 0) {
					if (p.hasPermission("lchest.command.help") | p.hasPermission("lchest.command.*") | p.hasPermission("lchest.*")) {
						p.sendMessage("");
						p.sendMessage(lfile.getString("lchestcmd.help.help_cmd").replaceAll("&", "§"));
						if (p.hasPermission("lchest.command.setup") | p.hasPermission("lchest.command.*") | p.hasPermission("lchest.*")) { p.sendMessage(lfile.getString("lchestcmd.help.setup_cmd").replaceAll("&", "§")); }
						p.sendMessage("");
						p.sendMessage("§fPlugin By: §9DarkDhz");
						p.sendMessage("");
					} else { p.sendMessage(prefix + lfile.getString("noperm").replaceAll("&", "§"));}
				} else {
					if(args[0].equalsIgnoreCase("help")) { p.performCommand("lchest"); 																
					} else if(args[0].equalsIgnoreCase("setup")) { 
						if (p.hasPermission("lchest.command.setup") | p.hasPermission("lchest.command.*") | p.hasPermission("lchest.*")) {
							if (args.length > 1) {
								p.sendMessage(prefix + lfile.getString("lchestcmd.setup.usage").replaceAll("&", "§"));
							} else {						
								if (getTargetBlock(p , 5).getType().equals(Material.CHEST)) {
									if (plugin.Locations.contains(getTargetBlock(p , 5).getLocation().toString())) {
										p.sendMessage(prefix + lfile.getString("lchestcmd.setup.already_chest").replaceAll("&", "§")); 
									} else {
										p.sendMessage(prefix + lfile.getString("lchestcmd.setup.success").replaceAll("&", "§"));								
										plugin.Locations.add(getTargetBlock(p , 5).getLocation().toString());
										dbfile.set("locations", plugin.Locations);
										try {
											dbfile.save(db);
										} catch (IOException exception) { exception.printStackTrace(); }
									}								
								} else {p.sendMessage(prefix + lfile.getString("lchestcmd.setup.no_chest").replaceAll("&", "§"));}
							}
						} else { p.sendMessage(prefix + lfile.getString("noperm").replaceAll("&", "§"));}						
					} else {p.performCommand("lchest");}
				}
			}
		}		
		return true;
	}
	public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
}
