package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ICommandAPI;
import com.abbas.lobby.BossBar.BossBarConfig;
import com.abbas.lobby.Scoreobard.ScoreBoardConfig;
import com.abbas.lobby.Utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadConfigs implements ICommandAPI {
    private static final String COMMAND_NAME = "reloadconfig";
    private static final String PERMISSION_NODE = "lobby.reloadconfig";

    @Override
    public void setupConfig() {
        Config.setup();
        if (!Config.getConfig().isConfigurationSection("reloadMessages")) {
            Config.getConfig().set("reloadMessages.noPermission", "&c⚠ You do not have permission to reload configurations!");
            Config.getConfig().set("reloadMessages.success", "&a✔ Configuration reloaded successfully.");
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

        reloadAllConfigs();
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("reloadMessages.success")));
        return true;
    }

    private void reloadAllConfigs() {
        Config.setup();
        BanConfig.setupConfig();
        UnbanConfig.setupConfig();
        ScoreBoardConfig.setupConfig();
        MuteConfig.setupConfig();
        WarnConfig.setupConfig();
        BossBarConfig.reload();
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
                Config.getConfig().getString("reloadMessages.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
    }
}