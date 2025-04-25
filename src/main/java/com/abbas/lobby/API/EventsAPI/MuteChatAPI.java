package com.abbas.lobby.API.EventsAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface MuteChatAPI {
    void handlePlayerChat(AsyncPlayerChatEvent event);
    void sendMuteMessage(Player player, String timeLeft);
    boolean isPlayerMuted(Player player);
}