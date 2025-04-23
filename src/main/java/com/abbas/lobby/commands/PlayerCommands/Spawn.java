package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Spawn implements ICommandAPI {
    private static final String COMMAND_NAME = "spawn";
    private static final String PERMISSION_NODE = "lobby.spawn";

    public Spawn() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("messages.spawn")) {
            config.set("spawn.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("spawn.playerOnly", "&c⚠ Console cannot use this command!");
            config.set("spawn.teleport", "&aTeleported to spawn.");
            config.set("spawn.notSet", "&c⚠ Spawn location is not set.");
            config.set("spawn.setspawn.success", "&aSpawn location set successfully.");
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

        teleportToSpawn((Player) sender);
        return true;
    }

    private void teleportToSpawn(Player player) {
        FileConfiguration config = Config.getConfig();
        Location spawnLocation = (Location) config.get("spawnLocation");

        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.sendMessage(ColorUtils.translateColorCodes(
                    config.getString("spawn.teleport")));
        } else {
            player.sendMessage(ColorUtils.translateColorCodes(
                    config.getString("spawn.notSet")));
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
        return "Teleport to spawn location";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("spawn.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("spawn.playerOnly")));
    }
}