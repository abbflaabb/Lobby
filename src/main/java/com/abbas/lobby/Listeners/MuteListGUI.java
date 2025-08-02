package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.commands.AdminCommands.MuteCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class MuteListGUI implements Listener {

    private static final String GUI_TITLE = "Muted Players";
    private static final int PAGE_SIZE = 45;

    public static void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("muteMessages.unmute")) {
            config.set(ConfigCommandPath.UNMUTE_NO_PERMISSION, "&c⚠ You do not have permission to unmute players!");
            config.set(ConfigCommandPath.UNMUTE_USAGE, "&c⚠ Usage: /unmute <player>");
            config.set(ConfigCommandPath.UNMUTE_NOT_MUTED, "&c⚠ That player is not muted!");
            config.set(ConfigCommandPath.UNMUTE_NOT_FOUND, "&c⚠ Player not found or is offline!");
            config.set(ConfigCommandPath.UNMUTE_SUCCESS, "&a✔ Successfully unmuted %player%");
            config.set(ConfigCommandPath.UNMUTE_TARGET, "&a✔ You have been unmuted!");
            config.set(ConfigCommandPath.UNMUTE_STAFF_NOTIFICATION, "&7[Staff] &e%player% &7was unmuted by &e%unmuter%");
            Config.save();
        }
    }

    public static void openMuteList(Player viewer, int page) {
        Map<UUID, Long> mutedPlayers = getMutedPlayers();
        List<UUID> uuids = new ArrayList<>(mutedPlayers.keySet());
        int totalPages = (int) Math.ceil(uuids.size() / (double) PAGE_SIZE);
        page = Math.max(1, Math.min(page, totalPages == 0 ? 1 : totalPages));

        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE + " (Page " + page + "/" + totalPages + ")");

        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, uuids.size());

        for (int i = start; i < end; i++) {
            UUID uuid = uuids.get(i);
            OfflinePlayer muted = Bukkit.getOfflinePlayer(uuid);
            String name = muted.getName() != null ? muted.getName() : uuid.toString();
            String reason = MuteCommand.getMuteReason(uuid);
            String timeLeft = MuteCommand.getMuteTimeLeft(uuid);

            ItemStack skull = new ItemStack(Material.SKULL_ITEM);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (muted.getName() != null) {
                skullMeta.setOwner(muted.getName());
            }
            skullMeta.setDisplayName(ColorUtils.translateColorCodes("&c" + name));
            List<String> lore = new ArrayList<>();
            lore.add(ColorUtils.translateColorCodes("&7Reason: &f" + reason));
            lore.add(ColorUtils.translateColorCodes("&7Time Left: &e" + timeLeft));
            lore.add(ColorUtils.translateColorCodes("&7Click to unmute!"));
            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);

            gui.setItem(i - start, skull);
        }

        if (page > 1) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();
            prevMeta.setDisplayName(ColorUtils.translateColorCodes("&aPrevious Page"));
            prev.setItemMeta(prevMeta);
            gui.setItem(45, prev);
        }

        if (page < totalPages) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(ColorUtils.translateColorCodes("&aNext Page"));
            next.setItemMeta(nextMeta);
            gui.setItem(53, next);
        }

        viewer.openInventory(gui);
    }

    @SuppressWarnings("unchecked")
    private static Map<UUID, Long> getMutedPlayers() {
        try {
            java.lang.reflect.Field field = MuteCommand.class.getDeclaredField("mutedPlayers");
            field.setAccessible(true);
            return (Map<UUID, Long>) field.get(null);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(GUI_TITLE)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        int page = 1;

        try {
            int idx1 = title.indexOf("Page ");
            int idx2 = title.indexOf("/", idx1);
            if (idx1 != -1 && idx2 != -1) {
                page = Integer.parseInt(title.substring(idx1 + 5, idx2));
            }
        } catch (Exception ignored) {}

        int clickedSlot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        FileConfiguration config = Config.getConfig();

        if (clickedSlot == 45 && page > 1) {
            openMuteList(player, page - 1);
            return;
        }

        if (clickedSlot == 53) {
            int totalPages = (int) Math.ceil(getMutedPlayers().size() / (double) PAGE_SIZE);
            if (page < totalPages) {
                openMuteList(player, page + 1);
            }
            return;
        }

        if (item != null && item.getType() == Material.SKULL_ITEM && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getDisplayName() != null) {
                String playerName = meta.getDisplayName().replace("§c", "").replace("&c", "");
                OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                if (target != null && MuteCommand.isPlayerMuted(target.getUniqueId())) {
                    if (player.hasPermission("lobby.mute")) {
                        MuteCommand.unmute(target.getUniqueId());
                        player.sendMessage(ColorUtils.translateColorCodes(
                                config.getString(ConfigCommandPath.UNMUTE_SUCCESS)
                                        .replace("%player%", playerName)));
                        openMuteList(player, page);
                    } else {
                        player.sendMessage(ColorUtils.translateColorCodes(
                                config.getString(ConfigCommandPath.UNMUTE_NO_PERMISSION)));
                    }
                } else {
                    player.sendMessage(ColorUtils.translateColorCodes(
                            config.getString(ConfigCommandPath.UNMUTE_NOT_MUTED)));
                }
            }
        }
    }
}