package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class Ping implements ICommandAPI {
    private static final String COMMAND_NAME = "ping";
    private static final String PERMISSION_NODE = "lobby.ping";

    public Ping() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("ping")) {
            config.set("ping.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("ping.playerOnly", "&c⚠ Console cannot use this command!");
            config.set("ping.selfPing", "&7Your ping is: &a%ping%ms");
            config.set("ping.quality.excellent", "&2Excellent");
            config.set("ping.quality.good", "&aGood");
            config.set("ping.quality.moderate", "&eModerate");
            config.set("ping.quality.poor", "&cPoor");
            config.set("ping.quality.bad", "&4Bad");
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

        showPing((Player) sender, Config.getConfig());
        return true;
    }

    private void showPing(Player player, FileConfiguration config) {
        int ping = ((CraftPlayer) player).getHandle().ping;
        String quality = getPingQuality(ping, config);

        String message = config.getString("ping.selfPing")
                .replace("%ping%", String.valueOf(ping))
                + " " + quality;

        player.sendMessage(ColorUtils.translateColorCodes(message));
    }

    private String getPingQuality(int ping, FileConfiguration config) {
        if (ping < 30) return config.getString("ping.quality.excellent");
        if (ping < 60) return config.getString("ping.quality.good");
        if (ping < 100) return config.getString("ping.quality.moderate");
        if (ping < 150) return config.getString("ping.quality.poor");
        return config.getString("ping.quality.bad");
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
        return "Check your connection latency";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("ping.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("ping.playerOnly")));
    }
}