package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Scoreboard.ScoreBoardConfig;
import com.abbas.lobby.Utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadConfigs implements ICommandAPI {
    private static final String COMMAND_NAME = "reloadconfig";
    private static final String PERMISSION_NODE = "lobby.reloadconfig";

    public ReloadConfigs() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("adminMessages.reload")) {
            config.set(ConfigCommandPath.ADMIN_RELOAD_NO_PERMISSION, "&c⚠ You do not have permission to reload configurations!");
            config.set(ConfigCommandPath.ADMIN_RELOAD_SUCCESS, "&a✔ Configuration reloaded successfully.");
            config.set(ConfigCommandPath.ADMIN_RELOAD_ERROR, "&c⚠ Error reloading configurations!");
            Config.save();
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        return reloadConfigurations(sender);
    }

    private boolean reloadConfigurations(CommandSender sender) {
        try {
            Config.setup();
            BanConfig.setupConfig();
            UnbanConfig.setupConfig();
            ScoreBoardConfig.setupConfig();
            MuteConfig.setupConfig();
            WarnConfig.setupConfig();

            sender.sendMessage(ColorUtils.translateColorCodes(
                    Config.getConfig().getString(ConfigCommandPath.ADMIN_RELOAD_SUCCESS)));
            return true;
        } catch (Exception e) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    Config.getConfig().getString(ConfigCommandPath.ADMIN_RELOAD_ERROR)));
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermissionNode());
    }

    @Override
    public String getPermissionNode() {
        return PERMISSION_NODE;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription() {
        return "Reload all plugin configurations";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.ADMIN_RELOAD_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
    }
}
