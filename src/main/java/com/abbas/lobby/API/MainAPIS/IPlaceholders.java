package com.abbas.lobby.API.MainAPIS;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface IPlaceholders {
    /**
     /**
     * Replace placeholders in the given text for a specific player
     * @param text The text containing placeholders
     * @param player The player to get values for
     * @return The text with replaced placeholders
     */
    String replacePlaceholders(String text, Player player);


    /**
     * Add a custom placeholder
     * @param placeholder The placeholder string (e.g., "%custom_placeholder%")
     * @param resolver The function to resolve the placeholder value
     */
    void addPlaceholder(String placeholder, Function<Player, String> resolver);

}
