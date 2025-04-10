package com.abbas.lobby.menus;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class MenuGamesListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = event.getView().getTitle();

        if (!inventoryTitle.equals(ChatColor.DARK_AQUA + "Game Menu")) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()
                || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (itemName.equals(ChatColor.RED + "PvP Arena")) {
            connectToServer(player, "pvp", "PvP Arena");
        } else if (itemName.equals(ChatColor.AQUA + "SkyWars")) {
            connectToServer(player, "skywars", "SkyWars");
        } else if (itemName.equals(ChatColor.LIGHT_PURPLE + "BedWars")) {
            connectToServer(player, "bedwars", "BedWars");
        } else if (itemName.equals(ChatColor.GOLD + "Survival Games")) {
            connectToServer(player, "survival", "Survival Games");
        } else if (itemName.equals(ChatColor.RED + "Close Menu")) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 0.5F);
        } else if (itemName.equals(ChatColor.GOLD + "Server Information") ||
                itemName.equals(ChatColor.YELLOW + "Information")) {
            showInfo(player);
        }
    }

    private void connectToServer(Player player, String serverName, String displayName) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(serverName);

            player.closeInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            player.sendMessage(ChatColor.GREEN + "Connecting to " + displayName + "...");

            player.sendPluginMessage(player.getServer().getPluginManager().getPlugin("Lobby"), "BungeeCord", b.toByteArray());
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error connecting to server!");
            e.printStackTrace();
        }
    }
    private void showInfo(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.5F, 1.0F);
        player.sendMessage("");
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.equals(ChatColor.DARK_AQUA + "Game Menu")) {
            Player player = (Player) event.getPlayer();
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 0.5F, 1.0F);
        }
    }
}
