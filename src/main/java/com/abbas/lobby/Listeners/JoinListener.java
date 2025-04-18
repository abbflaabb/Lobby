package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.configuration.file.FileConfiguration;



public class JoinListener implements Listener {

    public JoinListener() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("join.messages")) {
            config.set("join.messages.prefix", "&8[&aTest By Abbas&8] ");
            config.set("join.messages.welcome", "&a&lWelcome &e%player% &a&lto the server!");
            config.set("join.messages.leave", "&c%player% &7has left the server!");
        }

        Config.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();
        String prefix = ColorUtils.translateColorCodes(config.getString("join.messages.prefix"));
        String joinText = ColorUtils.translateColorCodes(config.getString("join.messages.welcome").replace("%player%", player.getName()));
        event.setJoinMessage(prefix + joinText);

        player.getInventory().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        String prefix = ColorUtils.translateColorCodes(config.getString("join.messages.prefix"));
        String quitText = ColorUtils.translateColorCodes(config.getString("join.messages.leave").replace("%player%", player.getName()));
        event.setQuitMessage(prefix + quitText);
    }
}
