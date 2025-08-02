/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.abbas.lobby.API.MainAPIS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface ICommandAPI extends CommandExecutor {
    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4);

    public void setupConfig();

    public boolean hasPermission(CommandSender var1);

    public String getPermissionNode();

    public String getCommandName();

    public String getDescription();

    public void sendNoPermissionMessage(CommandSender var1);

    public boolean isPlayerOnly();

    public void sendPlayerOnlyMessage(CommandSender var1);
}

