package com.abbas.lobby.commands.PlayerCommands.premuimCommands;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.ColorUtils;
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

        if (!config.isConfigurationSection("fly-messages")) {
            config.set("fly-messages.noPermission", "&cYou do not have permission to use this command!");
            config.set("fly-messages.flyEnabled", "&aFly mode enabled!");
            config.set("fly-messages.flyDisabled", "&cFly mode disabled!");
            config.set("fly-messages.flyHighEnabled", "&aHigh-Speed Fly mode enabled!");
            config.set("fly-messages.flyHighDisabled", "&cHigh-Speed Fly mode disabled!");
            config.set("fly-messages.normalSpeed", 0.1);
            config.set("fly-messages.highSpeed", 0.5);
        }


        Config.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes("&cOnly players can use this command!"));
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = Config.getConfig();

        boolean highMode = args.length > 0 && args[0].equalsIgnoreCase("high");

        if (highMode && !player.hasPermission("lobby.fly.high")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("fly-messages.noPermission", "&cYou do not have permission!")));
            return true;
        }
        if (!highMode && !player.hasPermission("lobby.fly")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("fly-messages.noPermission", "&cYou do not have permission!")));
            return true;
        }

        boolean isFlying = player.getAllowFlight();
        player.setAllowFlight(!isFlying);
        player.setFlying(!isFlying);

        float normalSpeed = (float) config.getDouble("fly-messages.normalSpeed", 0.1);
        float highSpeed = (float) config.getDouble("fly-messages.highSpeed", 0.5);
        player.setFlySpeed(isFlying ? 0.1f : (highMode ? highSpeed : normalSpeed));

        String messageKey = isFlying
                ? (highMode ? "fly-messages.flyHighDisabled" : "fly-messages.flyDisabled")
                : (highMode ? "fly-messages.flyHighEnabled" : "fly-messages.flyEnabled");

        player.sendMessage(ColorUtils.translateColorCodes(config.getString(messageKey, "&aFly mode updated!")));

        return true;
    }
}
