package com.abbas.lobby.ChatFormat;

public class RankData {
    private final String name;
    private final String prefix;
    private final int priority;
    private final String color;
    private final boolean canBypassCooldown;
    private final boolean canModerate;

    public RankData(String name, String prefix, int priority, String color,
                    boolean canBypassCooldown, boolean canModerate) {
        this.name = name;
        this.prefix = prefix;
        this.priority = priority;
        this.color = color;
        this.canBypassCooldown = canBypassCooldown;
        this.canModerate = canModerate;
    }

    public String getName() { return name; }
    public String getPrefix() { return prefix; }
    public int getPriority() { return priority; }
    public String getColor() { return color; }
    public boolean canBypassCooldown() { return canBypassCooldown; }
    public boolean canModerate() { return canModerate; }
}
