package com.abbas.lobby.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MenuGames {

    private static final String MENU_TITLE = ChatColor.DARK_AQUA + "Game Menu";
    private static final int INVENTORY_SIZE = 36;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void openGameMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, INVENTORY_SIZE, MENU_TITLE);

        ItemStack timeItem = new ItemStack(Material.WATCH);
        ItemMeta timeMeta = timeItem.getItemMeta();
        timeMeta.setDisplayName(ChatColor.GOLD + "Server Information");
        List<String> timeLore = new ArrayList<>();
        timeLore.add(ChatColor.YELLOW + dateFormat.format(new Date()));
        timeLore.add(ChatColor.WHITE + "Current User's Login: " + ChatColor.YELLOW + player.getName());
        timeMeta.setLore(timeLore);
        timeItem.setItemMeta(timeMeta);
        menu.setItem(4, timeItem);

        menu.setItem(10, createMenuItem(Material.DIAMOND_SWORD,
                ChatColor.RED + "PvP Arena",
                new String[]{
                        ChatColor.GRAY + "► Click to join PvP Arena",
                        "",
                }));

        menu.setItem(12, createMenuItem(Material.GRASS,
                ChatColor.AQUA + "SkyWars",
                new String[]{
                        ChatColor.GRAY + "► Click to join SkyWars",
                        "",
                }));

        menu.setItem(14, createMenuItem(Material.BED,
                ChatColor.LIGHT_PURPLE + "BedWars",
                new String[]{
                        ChatColor.GRAY + "► Click to join BedWars",
                        "",
                }));

        menu.setItem(16, createMenuItem(Material.CHEST,
                ChatColor.GOLD + "Survival Games",
                new String[]{
                        ChatColor.GRAY + "► Click to join Survival Games",
                        "",
                }));

        menu.setItem(31, createMenuItem(Material.BOOK,
                ChatColor.YELLOW + "Information",
                new String[]{
                        "",
                        ChatColor.GRAY + "Server: " + ChatColor.WHITE + "Game Server #1",
                        ChatColor.GRAY + "Online Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size()
                }));

        menu.setItem(35, createMenuItem(Material.BARRIER,
                ChatColor.RED + "Close Menu",
                new String[]{
                        ChatColor.GRAY + "Click to close the menu",
                        "",
                        ChatColor.WHITE + "Current User's Login: " + ChatColor.YELLOW + player.getName()
                }));
        ItemStack filler = createMenuItem(Material.STAINED_GLASS_PANE, " ", new String[]{});
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, filler);
            }
        }
        player.openInventory(menu);
    }
    private static ItemStack createMenuItem(Material material, String name, String[] lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        List<String> loreList = new ArrayList<>();
        for (String loreLine : lore) {
            loreList.add(loreLine);
        }
        meta.setLore(loreList);

        item.setItemMeta(meta);
        return item;
    }
}
