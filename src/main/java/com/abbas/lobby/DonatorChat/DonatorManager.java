package com.abbas.lobby.DonatorChat;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DonatorManager {
    private final Set<UUID> donators;

    public DonatorManager() {
        this.donators = new HashSet<>();
    }


    public boolean isDonator(Player pl) {
        return donators.contains(pl.getUniqueId());
    }

}
