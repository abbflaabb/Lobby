package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Vanish implements ICommandAPI {
    private static final String COMMAND_NAME = "vanish";
    private static final String PERMISSION_NODE = "lobby.vanish";
    private final Lobby plugin;

    public Vanish(Lobby plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("vanishMessages")) {
            config.set(ConfigCommandPath.VANISH_NO_PERMISSION, "&c⚠ You do not have permission to use vanish!");
            config.set(ConfigCommandPath.VANISH_ENABLED, "&a✔ You are now in vanish");
            config.set(ConfigCommandPath.VANISH_DISABLED, "&c✖ You are no longer in vanish");
            config.set(ConfigCommandPath.VANISH_PLAYER_ONLY, "&c⚠ This command can only be used by players!");
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

        return toggleVanish(player);
    }

    private boolean toggleVanish(Player player) {
        FileConfiguration config = Config.getConfig();

        if (plugin.vanish_list.contains(player)) {
            showPlayerToAll(player);
            plugin.vanish_list.remove(player);
            player.sendMessage(ColorUtils.translateColorCodes(
                    config.getString(ConfigCommandPath.VANISH_DISABLED)));
        } else {
            hidePlayerFromNonStaff(player);
            plugin.vanish_list.add(player);
            player.sendMessage(ColorUtils.translateColorCodes(
                    config.getString(ConfigCommandPath.VANISH_ENABLED)));
        }
        return true;
    }

    private void showPlayerToAll(Player player) {
        for (Player people : Bukkit.getOnlinePlayers()) {
            people.showPlayer(player);
        }
    }
    private void hidePlayerFromNonStaff(Player player) {
        for (Player people : Bukkit.getOnlinePlayers()) {
            if (!people.hasPermission(PERMISSION_NODE)) {
                people.hidePlayer(player);
            } else {
                people.showPlayer(player);
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
        return "Toggle visibility to other players";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.VANISH_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.VANISH_PLAYER_ONLY)));
    }
}
