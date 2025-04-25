package com.abbas.lobby.API.EventsAPI;


import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

public interface DropAPI {
    void handleDrop(PlayerDropItemEvent event);
    void playSuccessEffects(Player player);
    void playDenyEffects(Player player);
    boolean hasDropPermission(Player player);
}