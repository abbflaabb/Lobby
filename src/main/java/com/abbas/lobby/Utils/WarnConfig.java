package com.abbas.lobby.Utils;
import org.bukkit.configuration.file.FileConfiguration;
public class WarnConfig {
    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("warnMessages")) {
            config.set("warnMessages.warn.success", "&aSuccessfully warned %player% for %reason%");
            config.set("warnMessages.warn.noPermission", "&cYou do not have permission to warn!");
            config.set("warnMessages.warn.usage", "&cUsage: /warn <player> <reason>");
            config.set("warnMessages.warn.playerNotFound", "&cPlayer %player% is not online!");
            config.set("warnMessages.warn.exempt", "&cThis player cannot be warned!");
            config.set("warnMessages.warn.header", "&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            config.set("warnMessages.warn.warningMessage", "&c⚠ You have been warned by &e%sender%");
            config.set("warnMessages.warn.reasonMessage", "&cReason: &e%reason%");
            config.set("warnMessages.warn.staffNotification", "&7[Staff] &e%player% &7was warned by &e%sender% &7for: &e%reason%");
            Config.save();
        }
    }
}
