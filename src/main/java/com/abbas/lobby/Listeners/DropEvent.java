package com.abbas.lobby.Listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import com.abbas.lobby.Utils.ColorUtils;

public class DropEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.hasPermission("lobby.drop")) {
            event.setCancelled(false);
            player.playSound(player.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
            return;
        }

        player.sendMessage(ColorUtils.translateColorCodes("&c⚠ &7You cannot drop items here!"));
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
    }
}


