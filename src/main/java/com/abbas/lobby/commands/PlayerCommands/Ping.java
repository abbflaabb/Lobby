package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class Ping implements CommandExecutor {

    public Ping() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("ping")) {
            config.set("ping.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("ping.consoleError", "&c⚠ Console cannot use this command!");
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
        if (!command.getName().equalsIgnoreCase("ping")) return false;

        FileConfiguration config = Config.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes(config.getString("ping.consoleError")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.ping")) {
            player.sendMessage(ColorUtils.translateColorCodes(config.getString("ping.noPermission")));
            return true;
        }

        showPing(player, config);
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
}
