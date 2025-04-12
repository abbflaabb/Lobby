package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

    public SetSpawn() {
        setupConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
                return false;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("lobby.setspawn")) {
                player.sendMessage(ColorUtils.translateColorCodes("&cYou don't have permission to use this command."));
                return false;
            }

            Location location = player.getLocation();
            Config.getConfig().set("spawnLocation", location);
            Config.save();
            player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.setspawn.success")));
            return true;
        }
        return false;
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("messages")) {
            config.set("messages.spawn.teleport", "&aTeleported to spawn.");
            config.set("messages.spawn.notSet", "&cSpawn location is not set.");
            config.set("messages.setspawn.success", "&aSpawn location set successfully.");
            config.set("messages.respawn.teleport", "&aTeleported to spawn.");
            config.set("messages.respawn.notSet", "&cSpawn location is not set.");
            Config.save();
        }
    }
}
