package com.abbas.lobby.API.MainAPIS;

import org.bukkit.entity.Player;

public interface ISubTitle {
    /**
     * Sets up the default configuration for titles
     */
    void setupConfig();

    /**
     * Sends a title and subtitle to a player
     *
     * @param player The player to send the title to
     * @param type The type of title from configuration
     */
    void sendTitle(Player player, String type);

    /**
     * Clears any active title from the player's screen
     *
     * @param player The player to clear the title from
     */
    void clearTitle(Player player);

    void sendTitlePacket(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
}