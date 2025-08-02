package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class MuteConfig {
    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("muteMessages")) {
            config.set("muteMessages.noPermission", "&c⚠ You do not have permission to mute players!");
            config.set("muteMessages.usage", "&c⚠ Usage: /mute <player> [duration] [reason]");
            config.set("muteMessages.playerNotFound", "&c⚠ Player not found!");
            config.set("muteMessages.exempt", "&c⚠ This player cannot be muted!");
            config.set("muteMessages.alreadyMuted", "&c⚠ This player is already muted!");
            config.set("muteMessages.invalidDuration", "&c⚠ Invalid duration format! Use s/m/h/d");
            config.set("muteMessages.playerMuted", "&c⚠ You have been muted for %duration%\n&7Reason: &f%reason%");
            config.set("muteMessages.playerMutedPermanent", "&c⚠ You have been permanently muted!\n&7Reason: &f%reason%");
            config.set("muteMessages.success", "&a✔ Successfully muted %player% for %duration%");
            config.set("muteMessages.successPermanent", "&a✔ Successfully muted %player% permanently");


            config.set("muteMessages.cannotChat", "&cYou cannot chat while muted! Time remaining: &e%timeLeft%");
            config.set("muteMessages.cannotChatPermanent", "&cYou cannot chat while permanently muted!");


            config.set("muteMessages.unmute.noPermission", "&c⚠ You do not have permission to unmute players!");
            config.set("muteMessages.unmute.usage", "&c⚠ Usage: /unmute <player>");
            config.set("muteMessages.unmute.notMuted", "&c⚠ That player is not muted!");
            config.set("muteMessages.unmute.notFound", "&c⚠ Player not found or is offline!");
            config.set("muteMessages.unmute.success", "&a✔ Successfully unmuted %player%");
            config.set("muteMessages.unmute.target", "&a✔ You have been unmuted!");
            config.set("muteMessages.staff.unmuteNotification", "&7[Staff] &e%player% &7was unmuted by &e%unmuter%");

            config.set("muteMessages.staff.notification", "&7[Staff] &e%player% &7was muted by &e%muter% &7for: &e%reason%.");

            Config.save();
        }
    }
}
