package com.abbas.lobby.API;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ITeleportAPI {
    /**
     * Sets up the default configuration for the teleport bow
     */
    void setupConfig();

    /**
     * Creates and returns a teleport bow item
     * @return The teleport bow ItemStack
     */
    ItemStack createTeleportBow();

    /**
     * Gets the configured arrow speed
     * @return The arrow speed value
     */
    double getArrowSpeed();

    /**
     * Checks if the teleport bow functionality is enabled
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Gives a teleport bow and arrow to a player
     * @param player The player to give the items to
     */
    void giveTeleportBowItems(Player player);

    /**
     * Checks if an item is a valid teleport bow
     * @param item The item to check
     * @return true if the item is a teleport bow, false otherwise
     */
    boolean isTeleportBow(ItemStack item);
}