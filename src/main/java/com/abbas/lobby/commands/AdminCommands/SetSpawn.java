package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Location;
import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawn implements ICommandAPI {
    private static final String COMMAND_NAME = "setspawn";
    private static final String PERMISSION_NODE = "lobby.setspawn";

    public SetSpawn() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("messages.setspawn")) {
            config.set(ConfigPath.SETSPAWN_SUCCESS, "&a✔ Spawn location set successfully.");
            config.set(ConfigPath.DROP_NO_PERMISSION, "&c⚠ You don't have permission to set spawn!");
            config.set(ConfigPath.SPAWN_NOT_SET, "&c⚠ This command can only be used by players!");
            config.set(ConfigPath.SPAWN_TELEPORT, "&a✔ Teleported to spawn.");
            config.set(ConfigPath.SPAWN_NOT_SET, "&c⚠ Spawn location is not set.");
            config.set(ConfigPath.RESPAWN_TELEPORT, "&a✔ Teleported to spawn.");
            config.set(ConfigPath.RESPAWN_NOT_SET, "&c⚠ Spawn location is not set.");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!isPlayerOnly()) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        Player player = (Player) sender;
        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        return setSpawnLocation(player);
    }

    private boolean setSpawnLocation(Player player) {
        Location location = player.getLocation();
        Config.getConfig().set(ConfigPath.SPAWN_LOCATION, location);
        Config.save();

        player.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigPath.SETSPAWN_SUCCESS)));
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
        return "Set the server spawn location";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigPath.DROP_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigPath.SPAWN_NOT_SET)));
    }
}
