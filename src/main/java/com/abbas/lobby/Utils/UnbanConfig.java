package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class UnbanConfig {

    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("unbanMessages")) {
            config.set("unbanMessages.success", "&a✔ Successfully unbanned %player%");
            config.set("unbanMessages.noPermission", "&c⚠ You do not have permission to unban players!");
            config.set("unbanMessages.usage", "&c⚠ Usage: /unban <player>");
            config.set("unbanMessages.notBanned", "&c⚠ That player is not banned!");
            Config.save();
        }
    }
}
