package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String JoinText = ColorUtils.translateColorCodes("Welcome " + player.getName() + "to the server!");
        event.setJoinMessage(JoinText);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String quitText = ColorUtils.translateColorCodes(player.getName() + "has left the server!");
        event.setQuitMessage(quitText);
    }
}
