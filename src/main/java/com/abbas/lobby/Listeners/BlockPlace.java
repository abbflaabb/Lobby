package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.EventsAPI.BlockPlaceAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener, BlockPlaceAPI {

    public BlockPlace() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("blockPlace.messages")) {
            config.set(ConfigPath.BLOCK_PLACE_NO_PERMISSION, "&c⚠ &7You cannot place blocks here!");
            Config.save();
        }
    }

    @EventHandler
    @Override
    public void handleBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.hasPermission("lobby.BlockPlace")) {
            event.setCancelled(false);
            playSuccessEffects(player, event);
            return;
        }

        playDenyEffects(player, event);
    }

    @Override
    public void playSuccessEffects(Player player, BlockPlaceEvent event) {
        player.playSound(player.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
        player.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
    }

    @Override
    public void playDenyEffects(Player player, BlockPlaceEvent event) {
        player.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString(ConfigPath.BLOCK_PLACE_NO_PERMISSION)));
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        player.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.SMOKE, 0);
    }
}