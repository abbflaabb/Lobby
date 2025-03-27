package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Scoreobard.ScoreBoardConfig;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.BanConfig;
import com.abbas.lobby.Utils.UnbanConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigs implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("lobby.reloadconfig")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        Config.setup();
        BanConfig.setupConfig();
        UnbanConfig.setupConfig();
        ScoreBoardConfig.setupConfig();
        sender.sendMessage("§aConfiguration reloaded successfully.");
        return true;
    }
}