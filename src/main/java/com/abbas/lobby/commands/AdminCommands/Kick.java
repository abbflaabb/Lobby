package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ICommandAPI;
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
            Config.getConfig().set("kickMessages.noPermission", "&c⚠ You do not have permission to kick players!");
            Config.getConfig().set("kickMessages.usage", "&c⚠ Usage: /kick <player> [reason]");
            Config.getConfig().set("kickMessages.playerNotFound", "&c⚠ Player not online!");
            Config.getConfig().set("kickMessages.success", "&a✔ Successfully kicked %player% for: %reason%");
            Config.getConfig().set("kickMessages.defaultReason", "You have been kicked from the server!");
            Config.getConfig().set("kickMessages.kickFormat", "&c⚠ You were kicked!\n&7Reason: &f%reason%");
            Config.getConfig().set("kickMessages.playerOnly", "&c⚠ This command can only be used by players!");
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
                    Config.getConfig().getString("kickMessages.usage")));
            return true;
        }

        return processKick(sender, args);
    }

    private boolean processKick(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    Config.getConfig().getString("kickMessages.playerNotFound")));
            return true;
        }

        String reason = Config.getConfig().getString("kickMessages.defaultReason");
        if (args.length > 1) {
            reason = String.join(" ", args).substring(args[0].length()).trim();
        }

        String kickMessage = Config.getConfig().getString("kickMessages.kickFormat")
                .replace("%reason%", reason);
        target.kickPlayer(ColorUtils.translateColorCodes(kickMessage));

        String successMessage = Config.getConfig().getString("kickMessages.success")
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
                Config.getConfig().getString("kickMessages.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("kickMessages.playerOnly")));
    }
}