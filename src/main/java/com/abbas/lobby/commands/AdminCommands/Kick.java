package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements ICommandAPI {
    private static final String COMMAND_NAME = "kick";
    private static final String PERMISSION_NODE = "lobby.kick";

    public Kick() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        if (!Config.getConfig().isConfigurationSection("kickMessages")) {
            Config.getConfig().set(ConfigCommandPath.KICK_NO_PERMISSION, "&c⚠ You do not have permission to kick players!");
            Config.getConfig().set(ConfigCommandPath.KICK_USAGE, "&c⚠ Usage: /kick <player> [reason]");
            Config.getConfig().set(ConfigCommandPath.KICK_PLAYER_NOT_FOUND, "&c⚠ Player not online!");
            Config.getConfig().set(ConfigCommandPath.KICK_SUCCESS, "&a✔ Successfully kicked %player% for: %reason%");
            Config.getConfig().set(ConfigCommandPath.KICK_DEFAULT_REASON, "You have been kicked from the server!");
            Config.getConfig().set(ConfigCommandPath.KICK_FORMAT, "&c⚠ You were kicked!\n&7Reason: &f%reason%");
            Config.getConfig().set(ConfigCommandPath.KICK_PLAYER_ONLY, "&c⚠ This command can only be used by players!");
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

        if (args.length < 1) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    Config.getConfig().getString(ConfigCommandPath.KICK_USAGE)));
            return true;
        }

        return processKick(sender, args);
    }

    private boolean processKick(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    Config.getConfig().getString(ConfigCommandPath.KICK_PLAYER_NOT_FOUND)));
            return true;
        }

        String reason = Config.getConfig().getString(ConfigCommandPath.KICK_DEFAULT_REASON);
        if (args.length > 1) {
            reason = String.join(" ", args).substring(args[0].length()).trim();
        }

        String kickMessage = Config.getConfig().getString(ConfigCommandPath.KICK_FORMAT)
                .replace("%reason%", reason);
        target.kickPlayer(ColorUtils.translateColorCodes(kickMessage));

        String successMessage = Config.getConfig().getString(ConfigCommandPath.KICK_SUCCESS)
                .replace("%player%", target.getName())
                .replace("%reason%", reason);
        sender.sendMessage(ColorUtils.translateColorCodes(successMessage));

        return true;
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
        return "Kick a player from the server";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.KICK_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.KICK_PLAYER_ONLY)));
    }
}
