package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class MuteConfig {
    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("muteMessages")) {
            config.set("muteMessages.mute.success", "&aSuccessfully muted &e%player% &afor &e%duration%. Mute ID: &e%mute_id%");
            config.set("muteMessages.mute.successPermanent", "&aSuccessfully muted &e%player% &apermanently. Mute ID: &e%mute_id%");
            config.set("muteMessages.mute.noPermission", "&cYou don't have permission to use this command!");
            config.set("muteMessages.mute.usage", "&cUsage: /mute <player> [duration] <reason>");
            config.set("muteMessages.mute.playerNotFound", "&cPlayer not found!");
            config.set("muteMessages.mute.exempt", "&cThis player cannot be muted!");
            config.set("muteMessages.mute.alreadyMuted", "&cThis player is already muted!");
            config.set("muteMessages.mute.invalidDuration", "&cInvalid duration format! Use: <number>[s/m/h/d]");

            config.set("muteMessages.mute.playerMuted", "&cYou have been muted for &e%duration%&c. Reason: &e%reason%. Mute ID: &e%mute_id%");
            config.set("muteMessages.mute.playerMutedPermanent", "&cYou have been permanently muted. Reason: &e%reason%. Mute ID: &e%mute_id%");

            config.set("muteMessages.mute.cannotChat", "&cYou cannot chat while muted! Time remaining: &e%timeLeft%");
            config.set("muteMessages.mute.cannotChatPermanent", "&cYou cannot chat while permanently muted!");

            config.set("muteMessages.unmute.success", "&aSuccessfully unmuted &e%player%");
            config.set("muteMessages.unmute.notMuted", "&cThis player is not muted!");
            config.set("muteMessages.unmute.noPermission", "&cYou don't have permission to unmute players!");

            config.set("muteMessages.staff.notification", "&7[Staff] &e%player% &7was muted by &e%muter% &7for: &e%reason%. Mute ID: &e%mute_id%");
            config.set("muteMessages.staff.unmuteNotification", "&7[Staff] &e%player% &7was unmuted by &e%unmuter%");

            Config.save();
        }
    }
}
