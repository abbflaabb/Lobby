package com.abbas.lobby.API.MainAPIS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface ICommandAPI extends CommandExecutor {
    boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    void setupConfig();

    boolean hasPermission(CommandSender sender);

    String getPermissionNode();

    String getCommandName();

    String getDescription();

    void sendNoPermissionMessage(CommandSender sender);

    boolean isPlayerOnly();

    void sendPlayerOnlyMessage(CommandSender sender);
}