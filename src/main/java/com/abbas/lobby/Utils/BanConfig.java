package com.abbas.lobby.Utils;

import org.bukkit.configuration.file.FileConfiguration;

public class BanConfig {

    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("banMessages")) {
            config.set("banMessages.success", "&aSuccessfully banned %player% for %reason% (Ban ID: %ban_id%)");
            config.set("banMessages.noPermission", "&cYou do not have permission to ban!");
            config.set("banMessages.usage", "&cUsage: /ban <player> [duration] <reason>");
            config.set("banMessages.playerNotFound", "&cPlayer not found.");
            config.set("banMessages.kickMessage", "§c§lLobbyBan§7» §cBanned for %reason%\n"
                    + "§4§oBanned by CONSOLE\n\n"
                    + "§7It seems like you are using a hacked client, please disable it!\n"
                    + "§cUnban in §f> §e%timeLeft%\n"
                    + "§7Ban ID: §e%ban_id%\n\n"
                    + "§7Unban application in TS or forum\n"
                    + "§eTS-Ip§7 » §ccoming soon\n"
                    + "§eForum§7 » §ccoming soon");
            config.set("banMessages.playerOnly","&c⚠ This command can only be used by players!");
            config.set("banMessages.invalidDuration", "&cInvalid duration format. Use s (seconds), m (minutes), h (hours), or d (days).");
            config.set("banMessages.expiration", "&aBan expires on %date%");
            config.set("banMessages.advancedBanMessage", "§c§lLobbyBan §7» §cBanned for %reason%\n"
                    + "§4§oBanned by CONSOLE\n\n"
                    + "§7It seems like you are using a hacked client, please disable it!\n"
                    + "§cUnban in §f> §e%timeLeft%\n"
                    + "§7Ban ID: §e%ban_id%\n\n"
                    + "§7Unban application in TS or forum\n"
                    + "§eTS-Ip§7 » §ccoming soon\n"
                    + "§eForum§7 » §ccoming soon");
            config.set("banMessages.forumLink", "§eForum§7 » §ccoming soon");

            Config.save();
        }
    }
}
