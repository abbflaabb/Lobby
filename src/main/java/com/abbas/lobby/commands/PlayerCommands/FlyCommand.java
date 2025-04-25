package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FlyCommand implements ICommandAPI {
    private static final String COMMAND_NAME = "fly";
    private static final String PERMISSION_NODE = "lobby.fly";
    private static final String PERMISSION_HIGH = "lobby.fly.high";

    public FlyCommand() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("fly")) {
            config.set(ConfigCommandPath.FLY_NO_PERMISSION, "&cYou don't have permission to use this command!");
            config.set(ConfigCommandPath.FLY_PLAYER_ONLY, "&cOnly players can use this command!");
            config.set(ConfigCommandPath.FLY_ENABLED, "&aFly mode enabled!");
            config.set(ConfigCommandPath.FLY_DISABLED, "&cFly mode disabled!");
            config.set(ConfigCommandPath.FLY_HIGH_ENABLED, "&aHigh-Speed Fly mode enabled!");
            config.set(ConfigCommandPath.FLY_HIGH_DISABLED, "&cHigh-Speed Fly mode disabled!");
            config.set(ConfigCommandPath.FLY_NORMAL_SPEED, 0.1);
            config.set(ConfigCommandPath.FLY_HIGH_SPEED, 0.5);
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

        Player player = (Player) sender;
        boolean highMode = args.length > 0 && args[0].equalsIgnoreCase("high");

        if (highMode && !player.hasPermission(PERMISSION_HIGH)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        if (!highMode && !hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        toggleFlyMode(player, highMode);
        return true;
    }

    private void toggleFlyMode(Player player, boolean highMode) {
        FileConfiguration config = Config.getConfig();
        boolean isFlying = player.getAllowFlight();

        player.setAllowFlight(!isFlying);
        player.setFlying(!isFlying);

        float normalSpeed = (float) config.getDouble(ConfigCommandPath.FLY_NORMAL_SPEED, 0.1);
        float highSpeed = (float) config.getDouble(ConfigCommandPath.FLY_HIGH_SPEED, 0.5);
        player.setFlySpeed(!isFlying ? (highMode ? highSpeed : normalSpeed) : 0.1f);

        String messageKey = isFlying
                ? (highMode ? ConfigCommandPath.FLY_HIGH_DISABLED : ConfigCommandPath.FLY_DISABLED)
                : (highMode ? ConfigCommandPath.FLY_HIGH_ENABLED : ConfigCommandPath.FLY_ENABLED);

        player.sendMessage(ColorUtils.translateColorCodes(config.getString(messageKey)));
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
        return "Toggle flight mode with optional high speed";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.FLY_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.FLY_PLAYER_ONLY)));
    }
}
