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

public class help implements CommandExecutor {

    public help() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("help")) {
            config.set("help.noPermission", "&cYou do not have permission to use this command!");
            config.set("help.notAPlayer", "&cYou must be a player to use this command!");
            config.set("help.helpList", Arrays.asList(
                    "&6&l/lobby - &e&lMain command for the lobby",
                    "&6&l/ping - Check your ping",
                    "&6&l/support - Get support from a staff member",
                    "&6&l/information - Get information about the server - &c&lBetaCommand",
                    "&6&l/menu - Show GamesMenu"
            ));
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("help")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("help.notAPlayer")));
                return true;
            }

            Player p = (Player) sender;
            if (!p.hasPermission("lobby.help")) {
                p.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("help.noPermission")));
                return true;
            }

            List<String> helpMessages = Config.getConfig().getStringList("help.helpList");
            for (String message : helpMessages) {
                p.sendMessage(ColorUtils.translateColorCodes(message));
            }

            return true;
        }
        return false;
    }
}
