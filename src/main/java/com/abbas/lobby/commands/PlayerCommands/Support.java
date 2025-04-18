package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class Support implements CommandExecutor {

    public Support() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("support")) {
            config.set("support.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("support.consoleError", "&c⚠ You must be a player to use this command!");
            config.set("support.header", "&6&lMainCommands Support");
            config.set("support.discord", "&6&l/discord - Join Discord Server");
            config.set("support.teamspeak", "&6&l/teamspeak - Join Teamspeak Server - &cBeta and Coming Soon");
            config.set("support.forums", "&6&l/forums - Visit our Forums - &cBeta and Coming Soon");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("support")) return false;

        FileConfiguration config = Config.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes(config.getString("support.consoleError")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.support")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.noPermission")));
            return true;
        }

        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.header")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.discord")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.teamspeak")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.forums")));

        return true;
    }
}
