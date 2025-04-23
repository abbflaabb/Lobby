package com.abbas.lobby.API;

import org.bukkit.entity.Player;

/**
 * Interface for LuckPerms operations
 */
public interface ILuckPerms {
    /**
     * Initializes the LuckPerms service
     */
    void setup();
    /**
     * Gets the player's rank from LuckPerms
     * @param player The player to get the rank for
     * @return The player's rank or "No Rank" if not found
     */
    String getPlayerRank(Player player);

    /**
     * Gets the player's prefix from LuckPerms
     * @param player The player to get the prefix for
     * @return The player's prefix or null if not found
     */
    String getPlayerPrefix(Player player);
}