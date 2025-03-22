package com.abbas.lobby.menus;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

public class PlayerBungeeTeleport implements Listener {

    private final Inventory gui;

    public PlayerBungeeTeleport() {
        gui = Bukkit.createInventory(null, 36, ColorUtils.translateColorCodes("&3Game Menu"));
        setupItems();
    }

    private void setupItems() {
        createItem(Material.DIAMOND_SWORD, "&cPvP Arena", 10, "pvp", "&7► Click to join PvP Arena");
        createItem(Material.GRASS, "&bSkyWars", 12, "skywars", "&7► Click to join SkyWars");
        createItem(Material.BED, "&dBedWars", 14, "bedwars", "&7► Click to join BedWars");
        createItem(Material.CHEST, "&6Survival Games", 16, "survival", "&7► Click to join Survival Games");

        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < 36; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, filler);
            }
        }
    }

    private void createItem(Material material, String name, int slot, String server, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtils.translateColorCodes(name));
        meta.setLore(Arrays.asList(
                ColorUtils.translateColorCodes(description),
                ColorUtils.translateColorCodes("&7"),
                ColorUtils.translateColorCodes("&7Server: &f" + server)
        ));
        item.setItemMeta(meta);
        gui.setItem(slot, item);
    }

    public void openMenu(Player player) {
        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 1.0F);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ColorUtils.translateColorCodes("&3Game Menu"))) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = clickedItem.getItemMeta().getDisplayName();
        if (itemName.contains("PvP")) {
            connectToServer(player, "pvp");
        } else if (itemName.contains("SkyWars")) {
            connectToServer(player, "skywars");
        } else if (itemName.contains("BedWars")) {
            connectToServer(player, "bedwars");
        } else if (itemName.contains("Survival")) {
            connectToServer(player, "survival");
        }
    }

    private void connectToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendMessage(ColorUtils.translateColorCodes("&aConnecting to " + server + "..."));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            player.sendPluginMessage(player.getServer().getPluginManager().getPlugin("Lobby"), "BungeeCord", b.toByteArray());
        } catch (Exception e) {
            player.sendMessage(ColorUtils.translateColorCodes("&cError connecting to server!"));
            e.printStackTrace();
        }
    }
}
