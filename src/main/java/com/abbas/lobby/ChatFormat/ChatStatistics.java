package com.abbas.lobby.ChatFormat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatStatistics {
    private long totalMessages;
    private long totalViolations;
    private int messagesThisHour;
    private int violationsThisHour;
    private int activePlayersThisHour;
    private int totalPlayersOnline;
    private long lastHourlyReset;
    private final Set<UUID> activePlayersSet;

    public ChatStatistics() {
        this.totalMessages = 0;
        this.totalViolations = 0;
        this.messagesThisHour = 0;
        this.violationsThisHour = 0;
        this.activePlayersThisHour = 0;
        this.totalPlayersOnline = 0;
        this.lastHourlyReset = System.currentTimeMillis();
        this.activePlayersSet = new HashSet<>();
    }

    public void incrementMessagesCount() {
        totalMessages++;
        messagesThisHour++;
    }

    public void incrementViolationsCount() {
        totalViolations++;
        violationsThisHour++;
    }

    public void addActivePlayer(UUID playerId) {
        if (activePlayersSet.add(playerId)) {
            activePlayersThisHour++;
        }
    }

    public boolean shouldResetHourly() {
        return (System.currentTimeMillis() - lastHourlyReset) >= 3600000; // 1 hour
    }

    public void resetHourlyStats() {
        messagesThisHour = 0;
        violationsThisHour = 0;
        activePlayersThisHour = 0;
        activePlayersSet.clear();
        lastHourlyReset = System.currentTimeMillis();
    }

    // Getters
    public long getTotalMessages() { return totalMessages; }
    public long getTotalViolations() { return totalViolations; }
    public int getMessagesThisHour() { return messagesThisHour; }
    public int getViolationsThisHour() { return violationsThisHour; }
    public int getActivePlayersThisHour() { return activePlayersThisHour; }
    public int getTotalPlayersOnline() { return totalPlayersOnline; }
    public void setTotalPlayersOnline(int count) { this.totalPlayersOnline = count; }
}