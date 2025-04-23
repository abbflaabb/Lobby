package com.abbas.lobby.Listeners;


import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class ReSpawnListener implements Listener {

    public ReSpawnListener() {
        setupConfig();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = (Location) Config.getConfig().get("spawnLocation");
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
            event.getPlayer().sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.respawn.teleport")));
        } else {
            event.getPlayer().sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.respawn.notSet")));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location spawnLocation = (Location) Config.getConfig().get("spawnLocation");
        if (spawnLocation != null) {
            event.getPlayer().teleport(spawnLocation);
            event.getPlayer().sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.spawn.teleport")));
        } else {
            event.getPlayer().sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("messages.spawn.notSet")));
        }
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("messages")) {
            config.set("messages.spawn.teleport", "&aTeleported to spawn.");
            config.set("messages.spawn.notSet", "&cSpawn location is not set.");
            config.set("messages.setspawn.success", "&aSpawn location set successfully.");
            config.set("messages.respawn.teleport", "&aTeleported to spawn.");
            config.set("messages.respawn.notSet", "&cSpawn location is not set.");
            Config.save();
        }
    }
}
