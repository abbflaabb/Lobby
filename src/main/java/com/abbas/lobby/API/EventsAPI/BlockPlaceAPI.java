package com.abbas.lobby.API.EventsAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.configuration.file.FileConfiguration;

public interface BlockPlaceAPI {
    void handleBlockPlace(BlockPlaceEvent event);
    void setupConfig();
    void playSuccessEffects(Player player, BlockPlaceEvent event);
    void playDenyEffects(Player player, BlockPlaceEvent event);
}