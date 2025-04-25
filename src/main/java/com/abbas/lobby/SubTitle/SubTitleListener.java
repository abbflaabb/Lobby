package com.abbas.lobby.SubTitle;

import com.abbas.lobby.API.MainAPIS.ISubTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class SubTitleListener implements Listener {
    private final ISubTitle Title;
    private final Plugin plugin;

    public SubTitleListener(ISubTitle title, Plugin plugin) {
        this.Title = title;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Title.sendTitle(player, "welcome");
        
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Title.clearTitle(event.getPlayer());
    }
}
