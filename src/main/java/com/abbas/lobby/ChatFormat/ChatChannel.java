package com.abbas.lobby.ChatFormat;



public class ChatChannel {
    private final String name;
    private final String displayName;
    private final String color;
    private final int priority;
    private final boolean isPublic;

    public ChatChannel(String name, String displayName, String color, int priority, boolean isPublic) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.priority = priority;
        this.isPublic = isPublic;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public int getPriority() { return priority; }
    public boolean isPublic() { return isPublic; }
}
