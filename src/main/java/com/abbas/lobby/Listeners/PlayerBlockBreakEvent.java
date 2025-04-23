package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerBlockBreakEvent implements Listener {

    public PlayerBlockBreakEvent() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("blockBreak.messages")) {
            config.set("blockBreak.messages.noPermission", "&c⚠ &7You cannot break blocks here!");
            config.set("blockBreak.messages.permissionMessage", "&cYou have permission to break blocks here!");
            Config.save();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        if (p.isOp()) {
            return;
        }

        event.setCancelled(true);

        String message = ColorUtils.translateColorCodes(config.getString("blockBreak.messages.noPermission"));
        p.sendMessage(message);

        if (p.hasPermission("lobby.BlockBreak")) {
            event.setCancelled(false);

            String permissionMessage = ColorUtils.translateColorCodes(config.getString("blockBreak.messages.permissionMessage"));
            p.sendMessage(permissionMessage);
        }
    }
}
