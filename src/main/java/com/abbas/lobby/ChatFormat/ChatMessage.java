package com.abbas.lobby.ChatFormat;

import java.util.UUID;

public class ChatMessage {
    private final UUID playerId;
    private final String playerName;
    private final String originalMessage;
    private final String processedMessage;
    private final long timestamp;
    private final String world;

    public ChatMessage(UUID playerId, String playerName, String originalMessage,
                       String processedMessage, long timestamp, String world) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.originalMessage = originalMessage;
        this.processedMessage = processedMessage;
        this.timestamp = timestamp;
        this.world = world;
    }

    public UUID getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public String getOriginalMessage() { return originalMessage; }
    public String getProcessedMessage() { return processedMessage; }
    public long getTimestamp() { return timestamp; }
    public String getWorld() { return world; }
}

