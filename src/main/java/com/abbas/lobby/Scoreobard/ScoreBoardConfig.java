package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Utils.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ScoreBoardConfig {
    private static FileConfiguration config;

    public static void setupConfig() {
        Config.setup();
        config = Config.getConfig();
        if (!config.isConfigurationSection("scoreboard")) {
            config.set("scoreboard.title", "&e&lLOBBY");
            config.set("scoreboard.enabled-message", "&aScoreboard enabled!");
            config.set("scoreboard.disabled-message", "&cScoreboard disabled!");
            config.set("scoreboard.No-permission", "&cNo permission");
            config.set("scoreboard.lines", new String[]{
                    "&8&m------------------------",
                    "&fOnline Players: &a%online_players%",
                    "&fYour Name: &a%player_name%",
                    "",
                    "&fRank: &a%Lobby_rank%",
                    "&fBalance: &6%vault_eco_balance_formatted%",
                    "",
                    "&e%ip_server%",
                    "&8&m------------------------"
            });
            config.set("scoreboard.update-interval", 20);
            Config.save();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
