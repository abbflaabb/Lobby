package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class UnbanConfig {

    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("unbanMessages")) {
            config.set("unbanMessages.success", "&aSuccessfully unbanned %player%");
            config.set("unbanMessages.noPermission", "&cYou do not have permission to unban!");
            config.set("unbanMessages.usage", "&cUsage: /unban <player>");
            config.set("unbanMessages.notBanned", "&cThat player is not banned.");
            Config.save();
        }
    }
}
