package com.abbas.lobby.BossBar;

import com.abbas.lobby.API.BossBarAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BossBarListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BossBarAPI.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BossBarAPI.removePlayer(event.getPlayer());
    }
}