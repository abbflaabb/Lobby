package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
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

public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String joinText = ColorUtils.translateColorCodes("&a&lWelcome &e" + player.getName() + " &a&lto the server!");
        event.setJoinMessage(joinText);
        player.getInventory().clear();
        giveMenuCompass(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitText = ColorUtils.translateColorCodes("&c" + player.getName() + " &7has left the server!");
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
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ColorUtils.translateColorCodes("&b&lGame Menu &7(Right Click)"));
        compass.setItemMeta(meta);

        player.getInventory().setItem(4, compass);
        player.updateInventory();
    }
}
