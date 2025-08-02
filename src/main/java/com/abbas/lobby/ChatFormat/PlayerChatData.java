package com.abbas.lobby.ChatFormat;

import java.util.*;

public class PlayerChatData {
    private final UUID playerId;
    private long lastMessageTime;
    private long lastSeen;
    private int messageCount;
    private int violationCount;
    private boolean muted;
    private long muteExpiry;
    private String muteReason;
    private String currentChannel;
    private final List<Long> messageTimestamps;
    private final Map<String, Integer> violations;

    public PlayerChatData(UUID playerId) {
        this.playerId = playerId;
        this.lastMessageTime = 0;
        this.lastSeen = 0;
        this.messageCount = 0;
        this.violationCount = 0;
        this.muted = false;
        this.muteExpiry = 0;
        this.muteReason = null;
        this.currentChannel = "global";
        this.messageTimestamps = new ArrayList<>();
        this.violations = new HashMap<>();
    }

    public UUID getPlayerId() { return playerId; }
    public long getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(long time) { this.lastMessageTime = time; }
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long time) { this.lastSeen = time; }
    public int getMessageCount() { return messageCount; }
    public void incrementMessageCount() { this.messageCount++; }
    public int getViolationCount() { return violationCount; }
    public void addViolation(String type) {
        this.violationCount++;
        violations.put(type, violations.getOrDefault(type, 0) + 1);
    }
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public long getMuteExpiry() { return muteExpiry; }
    public void setMuteExpiry(long expiry) { this.muteExpiry = expiry; }
    public String getMuteReason() { return muteReason; }
    public void setMuteReason(String reason) { this.muteReason = reason; }
    public String getCurrentChannel() { return currentChannel; }
    public void setCurrentChannel(String channel) { this.currentChannel = channel; }
    public List<Long> getMessageTimestamps() { return messageTimestamps; }
    public Map<String, Integer> getViolations() { return violations; }
}
