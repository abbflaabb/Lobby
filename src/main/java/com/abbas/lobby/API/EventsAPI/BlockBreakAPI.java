package com.abbas.lobby.API.EventsAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface BlockBreakAPI {
    void setupConfig();
    void handleBlockBreak(BlockBreakEvent event);
    boolean canBreakBlocks(Player player);
    void sendMessage(Player player, String configPath);
}