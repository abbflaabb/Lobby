package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class MuteConfig {
    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("muteMessages")) {
            config.set("muteMessages.success", "&aSuccessfully muted &e%player% &afor &e%duration%. Mute ID: &e%mute_id%");
            config.set("muteMessages.successPermanent", "&aSuccessfully muted &e%player% &apermanently. Mute ID: &e%mute_id%");
            config.set("muteMessages.noPermission", "&cYou don't have permission to use this command!");
            config.set("muteMessages.usage", "&cUsage: /mute <player> [duration] <reason>");
            config.set("muteMessages.playerNotFound", "&cPlayer not found!");
            config.set("muteMessages.exempt", "&cThis player cannot be muted!");
            config.set("muteMessages.alreadyMuted", "&cThis player is already muted!");
            config.set("muteMessages.invalidDuration", "&cInvalid duration format! Use: <number>[s/m/h/d]");

            config.set("muteMessages.playerMuted", "&cYou have been muted for &e%duration%&c. Reason: &e%reason%. Mute ID: &e%mute_id%");
            config.set("muteMessages.playerMutedPermanent", "&cYou have been permanently muted. Reason: &e%reason%. Mute ID: &e%mute_id%");

            config.set("muteMessages.cannotChat", "&cYou cannot chat while muted! Time remaining: &e%timeLeft%");
            config.set("muteMessages.cannotChatPermanent", "&cYou cannot chat while permanently muted!");

            config.set("muteMessages.unmute.success", "&aSuccessfully unmuted &e%player%");
            config.set("muteMessages.unmute.notMuted", "&cThis player is not muted!");
            config.set("muteMessages.unmute.noPermission", "&cYou don't have permission to unmute players!");

            config.set("muteMessages.staff.notification", "&7[Staff] &e%player% &7was muted by &e%muter% &7for: &e%reason%. Mute ID: &e%mute_id%");
            config.set("muteMessages.staff.unmuteNotification", "&7[Staff] &e%player% &7was unmuted by &e%unmuter%");

            Config.save();
        }
    }
}
