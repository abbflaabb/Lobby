package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Utils.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ScoreBoardConfig {

    private static FileConfiguration config;

    public static void setupConfig() {
        Config.setup();
        config = Config.getConfig();
        if (!config.isConfigurationSection("scoreboard")) {
            config.set("scoreboard.title", "&bYour Server Name");
            config.set("scoreboard.customLine", "&eCustom Line");
            config.set("scoreboard.website", "&eyourwebsite.com");
            config.set("scoreboard.updateInterval", 20);
            Config.save();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
