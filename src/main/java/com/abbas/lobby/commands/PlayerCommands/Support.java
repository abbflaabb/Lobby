package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Support implements ICommandAPI {
    private static final String COMMAND_NAME = "support";
    private static final String PERMISSION_NODE = "lobby.support";

    public Support() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("support")) {
            config.set(ConfigCommandPath.SUPPORT_NO_PERMISSION, "&c⚠ You do not have permission to use this command!");
            config.set(ConfigCommandPath.SUPPORT_PLAYER_ONLY, "&c⚠ Console cannot use this command!");
            config.set(ConfigCommandPath.SUPPORT_TITLE, "&6&lMainCommands Support");
            config.set(ConfigCommandPath.SUPPORT_DISCORD, "&6&l/discord - Join Discord Server");
            config.set(ConfigCommandPath.SUPPORT_TEAMSPEAK, "&6&l/teamspeak - Join Teamspeak Server - &cBeta and Coming Soon");
            config.set(ConfigCommandPath.SUPPORT_FORUMS, "&6&l/forums - Visit our Forums - &cBeta and Coming Soon");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (isPlayerOnly() && !(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        displaySupportInfo((Player) sender);
        return true;
    }

    private void displaySupportInfo(Player player) {
        FileConfiguration config = Config.getConfig();
        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.SUPPORT_TITLE)));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.SUPPORT_DISCORD)));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.SUPPORT_TEAMSPEAK)));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.SUPPORT_FORUMS)));
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
        return "Display support information";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.SUPPORT_NO_PERMISSION)));
    }
    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.SUPPORT_PLAYER_ONLY)));
    }
}
