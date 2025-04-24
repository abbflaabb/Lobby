package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlace implements Listener {
    public BlockPlace() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("blockPlace.messages")) {
            config.set("blockPlace.messages.noPermission", "&c⚠ &7You cannot place blocks here!");
            Config.save();
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
        Player p = event.getPlayer();

        if (p.hasPermission("lobby.BlockPlace")) {
            event.setCancelled(false);
            p.playSound(p.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
            p.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
            return;
        }


        p.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("blockPlace.messages.noPermission")));
        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        p.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.SMOKE, 0);
    }
}


    @Override
    public void playSuccessEffects(Player player, BlockPlaceEvent event) {
        player.playSound(player.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
        player.playEffect(event.getBlock().getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
    }
