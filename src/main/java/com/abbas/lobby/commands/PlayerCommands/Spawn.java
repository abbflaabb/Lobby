package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
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

        if (!config.isConfigurationSection("spawn")) {
            config.set(ConfigCommandPath.SPAWN_NO_PERMISSION, "&c⚠ You do not have permission to use this command!");
            config.set(ConfigCommandPath.SPAWN_PLAYER_ONLY, "&c⚠ Console cannot use this command!");
            config.set(ConfigCommandPath.SPAWN_TELEPORT, "&aTeleported to spawn.");
            config.set(ConfigCommandPath.SPAWN_NOT_SET, "&c⚠ Spawn location is not set.");
            config.set(ConfigCommandPath.SPAWN_SET_SUCCESS, "&aSpawn location set successfully.");

            Config.save();
        }

        if (!config.contains(ConfigCommandPath.SPAWN_LOCATION)) {
            config.set(ConfigCommandPath.SPAWN_LOCATION, null);
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
        Location spawnLocation = (Location) config.get(ConfigCommandPath.SPAWN_LOCATION);

        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            String message = config.getString(ConfigCommandPath.SPAWN_TELEPORT);
            if (message != null) {
                player.sendMessage(ColorUtils.translateColorCodes(message));
            }
        } else {
            String message = config.getString(ConfigCommandPath.SPAWN_NOT_SET);
            if (message != null) {
                player.sendMessage(ColorUtils.translateColorCodes(message));
            }
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
                Config.getConfig().getString(ConfigCommandPath.SPAWN_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.SPAWN_PLAYER_ONLY)));
    }
}
