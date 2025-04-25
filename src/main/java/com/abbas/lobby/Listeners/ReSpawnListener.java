package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.EventsAPI.RespawnAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ReSpawnListener implements Listener, RespawnAPI {

    public ReSpawnListener() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("messages")) {
            config.set(ConfigPath.SPAWN_TELEPORT, "&aTeleported to spawn.");
            config.set(ConfigPath.SPAWN_NOT_SET, "&cSpawn location is not set.");
            config.set(ConfigPath.SETSPAWN_SUCCESS, "&aSpawn location set successfully.");
            config.set(ConfigPath.RESPAWN_TELEPORT, "&aTeleported to spawn.");
            config.set(ConfigPath.RESPAWN_NOT_SET, "&cSpawn location is not set.");
            Config.save();
        }
    }

    @EventHandler
    @Override
    public void handleRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = getSpawnLocation();
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
            sendMessage(event.getPlayer(), ConfigPath.RESPAWN_TELEPORT);
        } else {
            sendMessage(event.getPlayer(), ConfigPath.RESPAWN_NOT_SET);
        }
    }

    @EventHandler
    @Override
    public void handleJoin(PlayerJoinEvent event) {
        Location spawnLocation = getSpawnLocation();
        if (spawnLocation != null) {
            teleportPlayer(event.getPlayer(), spawnLocation);
            sendMessage(event.getPlayer(), ConfigPath.SPAWN_TELEPORT);
        } else {
            sendMessage(event.getPlayer(), ConfigPath.SPAWN_NOT_SET);
        }
    }

    @Override
    public Location getSpawnLocation() {
        return (Location) Config.getConfig().get(ConfigPath.SPAWN_LOCATION);
    }

    @Override
    public void teleportPlayer(Player player, Location location) {
        player.teleport(location);
    }

    @Override
    public void sendMessage(Player player, String configPath) {
        player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString(configPath)));
    }
}