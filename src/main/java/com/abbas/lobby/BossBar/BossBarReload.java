package com.abbas.lobby.BossBar;

import com.abbas.lobby.API.BossBarAPI;
import com.abbas.lobby.API.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class BossBarReload implements ICommandAPI {
    private static final String COMMAND_NAME = "bossbarreload";
    private static final String PERMISSION_NODE = "lobby.bossbar.reload";
    private final Plugin plugin;

    public BossBarReload(Plugin plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("bossbarMessages")) {
            config.set("bossbarMessages.noPermission", "&c⚠ You do not have permission to reload the bossbar!");
            config.set("bossbarMessages.success", "&a✔ BossBar has been reloaded!");
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

        BossBarAPI.reload();
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("bossbarMessages.success")));
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
        return "Reload the BossBar configuration";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("bossbarMessages.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
    }
}