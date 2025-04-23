package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
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
            config.set("fly.noPermission", "&cYou don't have permission to use this command!");
            config.set("fly.playerOnly", "&cOnly players can use this command!");
            config.set("fly.enabled", "&aFly mode enabled!");
            config.set("fly.disabled", "&cFly mode disabled!");
            config.set("fly.highEnabled", "&aHigh-Speed Fly mode enabled!");
            config.set("fly.highDisabled", "&cHigh-Speed Fly mode disabled!");
            config.set("fly.normalSpeed", 0.1);
            config.set("fly.highSpeed", 0.5);
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

        float normalSpeed = (float) config.getDouble("fly.normalSpeed", 0.1);
        float highSpeed = (float) config.getDouble("fly.highSpeed", 0.5);
        player.setFlySpeed(!isFlying ? (highMode ? highSpeed : normalSpeed) : 0.1f);

        String messageKey = isFlying
                ? (highMode ? "fly.highDisabled" : "fly.disabled")
                : (highMode ? "fly.highEnabled" : "fly.enabled");

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
                Config.getConfig().getString("fly.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("fly.playerOnly")));
    }
}