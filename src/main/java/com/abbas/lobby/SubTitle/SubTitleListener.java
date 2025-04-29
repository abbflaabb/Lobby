package com.abbas.lobby.SubTitle;

import com.abbas.lobby.API.MainAPIS.ISubTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SubTitleListener implements ISubTitle, Listener {
    private final ISubTitle title;
    private final Plugin plugin;

    public SubTitleListener(ISubTitle title, Plugin plugin) {
        this.title = title;
        this.plugin = plugin;
    }

    @Override
    public void sendTitle(Player player, String type) {
        title.sendTitle(player, type);
    }

    @Override
    public void clearTitle(Player player) {
        title.clearTitle(player);
    }

    @Override
    public void sendTitlePacket(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title.sendTitlePacket(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void setupConfig() {
        title.setupConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player != null && player.isOnline()) {
                    sendTitle(player, "welcome");
                }
            }
        }.runTaskLater(plugin, 5L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clearTitle(event.getPlayer());
    }
}
