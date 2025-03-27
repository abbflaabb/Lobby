package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class UnbanConfig {

    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("unbanMessages")) {
            config.set("unbanMessages.unban.success", "&aSuccessfully unbanned %player%");
            config.set("unbanMessages.unban.noPermission", "&cYou do not have permission to unban!");
            config.set("unbanMessages.unban.usage", "&cUsage: /unban <player>");
            config.set("unbanMessages.unban.notBanned", "&cThat player is not banned.");
            Config.save();
        }
    }
}