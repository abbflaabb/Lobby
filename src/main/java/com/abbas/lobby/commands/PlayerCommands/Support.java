package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
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
            config.set("support.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("support.playerOnly", "&c⚠ Console cannot use this command!");
            config.set("support.title", "&6&lMainCommands Support");
            config.set("support.discord", "&6&l/discord - Join Discord Server");
            config.set("support.teamspeak", "&6&l/teamspeak - Join Teamspeak Server - &cBeta and Coming Soon");
            config.set("support.forums", "&6&l/forums - Visit our Forums - &cBeta and Coming Soon");
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
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.title")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.discord")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.teamspeak")));
        player.sendMessage(ColorUtils.translateColorCodes(config.getString("support.forums")));
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
                Config.getConfig().getString("support.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("support.playerOnly")));
    }
}