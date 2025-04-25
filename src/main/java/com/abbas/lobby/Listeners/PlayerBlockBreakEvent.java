package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.EventsAPI.BlockBreakAPI;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerBlockBreakEvent implements Listener, BlockBreakAPI {

    public PlayerBlockBreakEvent() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("blockBreak.messages")) {
            config.set(ConfigPath.BLOCK_BREAK_NO_PERMISSION, "&c⚠ &7You cannot break blocks here!");
            Config.save();
        }
    }

    @EventHandler
    @Override
    public void handleBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            return;
        }

        event.setCancelled(true);
        sendMessage(player, ConfigPath.BLOCK_BREAK_NO_PERMISSION);

        if (canBreakBlocks(player)) {
            event.setCancelled(false);
        }
    }

    @Override
    public boolean canBreakBlocks(Player player) {
        return player.hasPermission("lobby.BlockBreak");
    }

    @Override
    public void sendMessage(Player player, String configPath) {
        String message = ColorUtils.translateColorCodes(Config.getConfig().getString(configPath));
        player.sendMessage(message);
    }
}