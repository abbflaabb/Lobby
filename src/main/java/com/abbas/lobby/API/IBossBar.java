package com.abbas.lobby.API;

import org.bukkit.entity.Player;

public interface IBossBar {
    void setupConfig();
    void startAnimation();
    void updateBossBars();
    void addPlayer(Player player);
    void removePlayer(Player player);
    void setEnabled(boolean enabled);
    void close();
}