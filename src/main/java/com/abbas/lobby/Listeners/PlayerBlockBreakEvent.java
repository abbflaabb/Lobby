package com.abbas.lobby.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
        Player p = event.getPlayer();
        p.sendMessage(ChatColor.RED + "You cannot break blocks here!");
        if (p.hasPermission("lobby.BlockBreak")) {
            event.setCancelled(false);
        }

    }
}
