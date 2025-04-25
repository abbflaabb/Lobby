package com.abbas.lobby.API.MainAPIS;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface defining core scoreboard operations for the lobby system
 */
public interface IScoreboard {
    /**
     * Sets the scoreboard for a player
     * @param player The player to set the scoreboard for
     */
    void setScoreboard(Player player);

    /**
     * Updates the scoreboard for all online players
     */
    void updateScoreboard();

    /**
     * Toggles the visibility state of a player's scoreboard
     * @param playerId The UUID of the player
     */
    void toggleScoreboardState(UUID playerId);

    /**
     * Checks if a player's scoreboard is hidden
     * @param playerId The UUID of the player
     * @return true if the scoreboard is hidden, false otherwise
     */
    boolean isScoreboardHidden(UUID playerId);

    /**
     * Gets the player's rank from LuckPerms
     * @param player The player to get the rank for
     * @return The player's rank or "No Rank" if not found
     */
    String getPlayerRank(Player player);
}