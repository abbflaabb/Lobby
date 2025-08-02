package com.abbas.lobby.Scoreboard;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.Utils.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ScoreBoardConfig {
    private static FileConfiguration config;

    public static void setupConfig() {
        Config.setup();
        config = Config.getConfig();

        if (!config.isConfigurationSection("scoreboard")) {
            config.set(ConfigPath.SCOREBOARD_TITLE, "&e&lLOBBY");
            config.set(ConfigPath.SCOREBOARD_NO_PERMISSION, "&c⚠ You do not have permission to toggle the scoreboard!");
            config.set(ConfigPath.SCOREBOARD_ENABLED_MESSAGE, "&a✔ Scoreboard enabled!");
            config.set(ConfigPath.SCOREBOARD_DISABLED_MESSAGE, "&c✖ Scoreboard disabled!");
            config.set(ConfigPath.SCOREBOARD_ERROR_MESSAGE, "&c⚠ Error toggling scoreboard!");
            config.set(ConfigPath.SCOREBOARD_PLAYER_ONLY, "&c⚠ This command can only be used by players!");
            config.set(ConfigPath.SCOREBOARD_LINES, new String[]{
                    "&8&m------------------------",
                    "&fOnline Players: &a%online_players%",
                    "&fYour Name: &a%player_name%",
                    "",
                    "&fRank: &a%Lobby_rank%",
                    "&fBalance: &6%vault_eco_balance_formatted%",
                    "",
                    "&fDate: &e%Date_time%",
                    "&e%ip_server%",
                    "&8&m------------------------"
            });
            config.set(ConfigPath.SCOREBOARD_UPDATE_INTERVAL, 20);
            Config.save();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
