package com.abbas.lobby.API;

import com.abbas.lobby.BossBar.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BossBarAPI {
    private static IBossBar bossBar;

    public static void initialize(Plugin plugin) {
        if (bossBar == null) {
            bossBar = new BossBar(plugin);
        }
    }

    public static void shutdown() {
        if (bossBar != null) {
            bossBar.close();
            bossBar = null;
        }
    }

    public static void addPlayer(Player player) {
        if (bossBar != null) {
            bossBar.addPlayer(player);
        }
    }

    public static void removePlayer(Player player) {
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }

    public static void setEnabled(boolean enabled) {
        if (bossBar != null) {
            bossBar.setEnabled(enabled);
        }
    }

    public static void reload() {
        if (bossBar != null) {
            bossBar.setupConfig();
            bossBar.startAnimation();
        }
    }
    public static IBossBar getBossBar() {
        return bossBar;
    }
}