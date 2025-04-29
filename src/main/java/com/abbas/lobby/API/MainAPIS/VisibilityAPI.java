package com.abbas.lobby.API.MainAPIS;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VisibilityAPI implements IVisibilityAPI {

    private static final Set<UUID> hiddenPlayers = new HashSet<>();

    protected static void setHidden(UUID uuid, boolean hidden) {
        if (hidden) {
            hiddenPlayers.add(uuid);
        } else {
            hiddenPlayers.remove(uuid);
        }
    }

    protected static boolean isHidden(UUID uuid) {
        return hiddenPlayers.contains(uuid);
    }

    @Override
    public boolean isPlayerHidingOthers(Player player) {
        return hiddenPlayers.contains(player.getUniqueId());
    }

    @Override
    public void togglePlayerVisibility(Player player) {
        UUID uuid = player.getUniqueId();
        boolean currentlyHidden = hiddenPlayers.contains(uuid);

        if (currentlyHidden) {
            showAllTo(player);
        } else {
            hideAllFrom(player);
        }
    }

    @Override
    public void hideAllFrom(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(player)) {
                player.hidePlayer(other);
            }
        }
        hiddenPlayers.add(player.getUniqueId());
    }

    @Override
    public void showAllTo(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(player)) {
                player.showPlayer(other);
            }
        }
        hiddenPlayers.remove(player.getUniqueId());
    }
}
