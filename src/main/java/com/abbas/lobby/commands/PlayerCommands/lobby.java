package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class lobby implements CommandExecutor {

    public lobby() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("lobby")) {
            config.set("lobby.noPermission", "&cYou do not have permission to use this command!");
            config.set("lobby.commandList", Arrays.asList(
                    "&6&l/lobby - &e&lMain command for the lobby",
                    "&6&l/ping - Check your ping",
                    "&6&l/support - Get support from a staff member",
                    "&6&l/information - Get information about the server - &c&lComingSoon",
                    "&6&l/menu - Show GamesMenu"
            ));
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("lobby")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes("&cYou must be a player to use this command!"));
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = Config.getConfig();

        if (!player.hasPermission("lobby.lobby")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("lobby.noPermission")));
            return true;
        }

        List<String> messages = config.getStringList("lobby.commandList");
        for (String message : messages) {
            player.sendMessage(ColorUtils.translateColorCodes(message));
        }

        return true;
    }
}
