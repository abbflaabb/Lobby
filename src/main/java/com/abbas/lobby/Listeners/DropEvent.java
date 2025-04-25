package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.EventsAPI.DropAPI;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import com.abbas.lobby.Utils.ColorUtils;

public class DropEvent implements Listener, DropAPI {

    public DropEvent() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("drop.messages")) {
            config.set(ConfigPath.DROP_NO_PERMISSION, "&c⚠ &7You cannot drop items here!");
            Config.save();
        }
    }

    @EventHandler
    @Override
    public void handleDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (hasDropPermission(player)) {
            event.setCancelled(false);
            playSuccessEffects(player);
            return;
        }

        playDenyEffects(player);
    }

    @Override
    public void playSuccessEffects(Player player) {
        player.playSound(player.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
    }

    @Override
    public void playDenyEffects(Player player) {
        player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString(ConfigPath.DROP_NO_PERMISSION)));
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
    }

    @Override
    public boolean hasDropPermission(Player player) {
        return player.hasPermission("lobby.drop");
    }
}