package com.abbas.lobby.API.EventsAPI;

import org.bukkit.BanEntry;
import org.bukkit.event.player.PlayerLoginEvent;
import java.util.Date;

public interface BanListenerAPI {
    void handlePlayerLogin(PlayerLoginEvent event);
    String getBanIdForPlayer(String playerName);
    String calculateTimeLeft(Date expires);
    BanEntry getBanEntry(String playerName);
    boolean isPlayerBanned(String playerName);
    String formatBanMessage(String reason, String timeLeft, String banId);
}