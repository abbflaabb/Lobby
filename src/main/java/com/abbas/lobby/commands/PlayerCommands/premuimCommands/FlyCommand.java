package com.abbas.lobby.commands.PlayerCommands.premuimCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    public FlyCommand() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("fly")) {
            config.set("fly.noPermission", "&cYou don't have permission to use this command!");
            config.set("fly.notAPlayer", "&cOnly players can use this command!");
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
        if (!command.getName().equalsIgnoreCase("fly")) return false;

        FileConfiguration config = Config.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes(config.getString("fly.notAPlayer")));
            return true;
        }

        Player player = (Player) sender;
        boolean highMode = args.length > 0 && args[0].equalsIgnoreCase("high");

        if (highMode && !player.hasPermission("lobby.fly.high")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("fly.noPermission")));
            return true;
        }

        if (!highMode && !player.hasPermission("lobby.fly")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("fly.noPermission")));
            return true;
        }

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

        return true;
    }
}
