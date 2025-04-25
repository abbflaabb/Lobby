package com.abbas.lobby.API.EventsAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface JoinListenerAPI {
    void setupConfig();
    void handleJoin(PlayerJoinEvent event);
    void handleQuit(PlayerQuitEvent event);
    void clearPlayerInventory(Player player);
}