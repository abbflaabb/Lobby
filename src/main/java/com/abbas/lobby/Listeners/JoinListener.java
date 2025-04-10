package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.menus.MenuGames;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class JoinListener implements Listener {

    public JoinListener() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("join.messages")) {
            config.set("join.messages.welcome", "&a&lWelcome &e%player% &a&lto the server!");
            config.set("join.messages.leave", "&c%player% &7has left the server!");
        }

        if (!config.isConfigurationSection("compass")) {
            config.set("compass.name", "&b&lGame Menu &7(Right Click)");
            config.set("compass.lore", Arrays.asList(
                    "&7Right click to open the game menu",
                    "&6Stay in the lobby to explore"
            ));
            Config.save();
        }

        Config.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        String joinText = ColorUtils.translateColorCodes(config.getString("join.messages.welcome").replace("%player%", player.getName()));
        event.setJoinMessage(joinText);

        player.getInventory().clear();
        giveMenuCompass(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        String quitText = ColorUtils.translateColorCodes(config.getString("join.messages.leave").replace("%player%", player.getName()));
        event.setQuitMessage(quitText);
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.COMPASS &&
                (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                    item.getItemMeta().getDisplayName().equals(ColorUtils.translateColorCodes("&b&lGame Menu &7(Right Click)"))) {
                event.setCancelled(true);
                MenuGames.openGameMenu(player);
            }
        }
    }

    private void giveMenuCompass(Player player) {
        FileConfiguration config = Config.getConfig();

        String compassName = ColorUtils.translateColorCodes(config.getString("compass.name"));
        List<String> compassLore = config.getStringList("compass.lore");

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(compassName);

        meta.setLore(ColorUtils.translateColorCodes(compassLore));

        compass.setItemMeta(meta);

        player.getInventory().setItem(4, compass);
        player.updateInventory();
    }
}
