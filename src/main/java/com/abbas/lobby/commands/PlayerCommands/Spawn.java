package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    public Spawn() {
        setupConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location spawnLocation = (Location) Config.getConfig().get("spawnLocation");
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                    player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.spawn.teleport")));
                } else {
                    player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.spawn.notSet")));
                }
                return true;
            } else {
                sender.sendMessage("This command can only be run by a player.");
                return false;
            }
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
