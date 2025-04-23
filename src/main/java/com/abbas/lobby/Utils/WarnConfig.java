package com.abbas.lobby.Utils;
import org.bukkit.configuration.file.FileConfiguration;
public class WarnConfig {
    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("warnMessages")) {
            config.set("warnMessages.warn.noPermission", "&c⚠ You do not have permission to warn players!");
            config.set("warnMessages.warn.usage", "&c⚠ Usage: /warn <player> <reason>");
            config.set("warnMessages.warn.playerNotFound", "&c⚠ Player %player% not found!");
            config.set("warnMessages.warn.exempt", "&c⚠ You cannot warn this player!");
            config.set("warnMessages.warn.header", "&7&m---------------------");
            config.set("warnMessages.warn.warningMessage", "&c⚠ You have been warned by &e%sender%");
            config.set("warnMessages.warn.reasonMessage", "&7Reason: &f%reason%");
            config.set("warnMessages.warn.success", "&a✔ Successfully warned %player% for: %reason%");
            config.set("warnMessages.warn.staffNotification", "&7[Staff] &e%player% &7was warned by &e%sender% &7for: &f%reason%");
            Config.save();
        }
    }
}
