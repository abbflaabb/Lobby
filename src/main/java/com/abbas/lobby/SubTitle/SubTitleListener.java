package com.abbas.lobby.SubTitle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

public class SubTitleListener implements Listener {
    private final SubTitle Title;
    private final Plugin plugin;

    public SubTitleListener(SubTitle title, Plugin plugin) {
        this.Title = title;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Title.sendTitle(player, "welcome");

        new BukkitRunnable() {
            @Override
            public void run() {
                Title.sendCustomTitle(player,
                        "&6&l%Lobby_rank%",
                        "&7Welcome back &b%player_name%",
                        10, 40, 10);
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Title.clearTitle(event.getPlayer());
    }
}
