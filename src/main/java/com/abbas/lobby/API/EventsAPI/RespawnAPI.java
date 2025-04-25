package com.abbas.lobby.API.EventsAPI;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public interface RespawnAPI {
    void setupConfig();
    void handleRespawn(PlayerRespawnEvent event);
    void handleJoin(PlayerJoinEvent event);
    Location getSpawnLocation();
    void teleportPlayer(Player player, Location location);
    void sendMessage(Player player, String configPath);
}