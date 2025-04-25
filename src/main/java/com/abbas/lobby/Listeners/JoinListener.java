package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.EventsAPI.JoinListenerAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.configuration.file.FileConfiguration;

public class JoinListener implements Listener, JoinListenerAPI {

    public JoinListener() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("join")) {
            config.set(ConfigPath.JOIN_PREFIX, "&8[&bLobby&8] ");
            config.set(ConfigPath.JOIN_MESSAGE_WELCOME, "&a&lWelcome &e%player% &a&lto the server!");
            config.set(ConfigPath.JOIN_MESSAGE_LEAVE, "&c%player% &7has left the server!");
        }

        Config.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    @Override
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        String prefix = ColorUtils.translateColorCodes(config.getString(ConfigPath.JOIN_PREFIX, "&8[&bLobby&8] "));
        String joinText = ColorUtils.translateColorCodes(config.getString(ConfigPath.JOIN_MESSAGE_WELCOME)
                .replace("%player%", player.getName()));
        event.setJoinMessage(prefix + joinText);

        clearPlayerInventory(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    @Override
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        String prefix = ColorUtils.translateColorCodes(config.getString(ConfigPath.JOIN_PREFIX, "&8[&bLobby&8] "));
        String quitText = ColorUtils.translateColorCodes(config.getString(ConfigPath.JOIN_MESSAGE_LEAVE)
                .replace("%player%", player.getName()));
        event.setQuitMessage(prefix + quitText);
    }

    @Override
    public void clearPlayerInventory(Player player) {
        player.getInventory().clear();
    }
}