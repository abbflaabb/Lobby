package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(false);
        Player p = event.getPlayer();

        if (p.hasPermission("lobby.BlockPlace")) {
            event.setCancelled(true);
            p.playSound(p.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
            p.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
            return;
        }


        p.sendMessage(ColorUtils.translateColorCodes("&c⚠ &7You cannot place blocks here!"));
        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        p.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.SMOKE, 0);
    }
}


