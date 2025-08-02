package com.abbas.lobby.PlayerVisibility;

import com.abbas.lobby.API.MainAPIS.IVisibilityAPI;
import com.abbas.lobby.API.MainAPIS.VisibilityAPI;
import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVisibility implements Listener {

    private final Lobby plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final IVisibilityAPI visibilityAPI;

    private final int cooldownSeconds;
    private final boolean globalHideOnJoin;
    private final Material toggleMaterial;
    private final int toggleSlot;

    public PlayerVisibility(Lobby plugin) {
        this.plugin = plugin;
        this.visibilityAPI = new VisibilityAPI();

        VisibilityConfig.setup();
        FileConfiguration config = VisibilityConfig.getConfig();

        this.cooldownSeconds = config.getInt("Settings.cooldown", 3);
        this.globalHideOnJoin = config.getBoolean("Settings.globalHideOnJoin", false);
        this.toggleMaterial = Material.matchMaterial(config.getString("Item.material", "INK_SACK"));
        this.toggleSlot = config.getInt("Item.slot", 4);

        setupDefaultConfig(config);
    }

    private void setupDefaultConfig(FileConfiguration config) {
        if (!config.isSet("Settings.enabled")) config.set("Settings.enabled", true);
        if (!config.isSet("Settings.cooldown")) config.set("Settings.cooldown", 3);
        if (!config.isSet("Settings.globalHideOnJoin")) config.set("Settings.globalHideOnJoin", false);
        if (!config.isSet("Item.material")) config.set("Item.material", "INK_SACK");
        if (!config.isSet("Item.slot")) config.set("Item.slot", 4);
        if (!config.isSet("Messages.hidden")) config.set("Messages.hidden", "&cPlayers are now hidden!");
        if (!config.isSet("Messages.shown")) config.set("Messages.shown", "&aPlayers are now visible!");
        if (!config.isSet("Messages.cooldown"))
            config.set("Messages.cooldown", "&cPlease wait {time} seconds before toggling visibility!");
        if (!config.isSet("Item.nameHidden")) config.set("Item.nameHidden", "&cPlayers Hidden");
        if (!config.isSet("Item.nameVisible")) config.set("Item.nameVisible", "&aPlayers Visible");
        VisibilityConfig.save();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!VisibilityConfig.getConfig().getBoolean("Settings.enabled", true)) return;

        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) return;
                giveToggleItem(player);
                if (globalHideOnJoin) {
                    visibilityAPI.hideAllFrom(player);
                }
            }
        }.runTaskLater(plugin, 5L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!VisibilityConfig.getConfig().getBoolean("Settings.enabled", true)) return;

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item == null || !isToggleItem(item)) return;
        event.setCancelled(true);

        if (isOnCooldown(player)) {
            long timeLeft = getRemainingCooldown(player);
            sendMessage(player, "cooldown", "&cPlease wait {time} seconds before toggling visibility again!", timeLeft);
            return;
        }

        setCooldown(player);
        visibilityAPI.togglePlayerVisibility(player);
        sendToggleMessage(player);
        giveToggleItem(player);
    }

    private void sendToggleMessage(Player player) {
        boolean hidden = visibilityAPI.isPlayerHidingOthers(player);
        String path = hidden ? "hidden" : "shown";
        String defaultMsg = hidden ? "&cPlayers are now hidden!" : "&aPlayers are now visible!";
        sendMessage(player, path, defaultMsg);
    }

    private void giveToggleItem(Player player) {
        boolean hidden = visibilityAPI.isPlayerHidingOthers(player);
        player.getInventory().setItem(toggleSlot, buildToggleItem(hidden));
        player.updateInventory();
    }

    private ItemStack buildToggleItem(boolean hidden) {
        ItemStack item = new ItemStack(toggleMaterial != null ? toggleMaterial : Material.INK_SACK, 1, (short) (hidden ? 8 : 10));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String path = hidden ? "Item.nameHidden" : "Item.nameVisible";
            String fallback = hidden ? "&cPlayers Hidden" : "&aPlayers Visible";
            meta.setDisplayName(ColorUtils.translateColorCodes(VisibilityConfig.getConfig().getString(path, fallback)));
            item.setItemMeta(meta);
        }
        return item;
    }

    private boolean isToggleItem(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;

        String name = meta.getDisplayName();
        String visible = ColorUtils.translateColorCodes(VisibilityConfig.getConfig().getString("Item.nameVisible", "&aPlayers Visible"));
        String hidden = ColorUtils.translateColorCodes(VisibilityConfig.getConfig().getString("Item.nameHidden", "&cPlayers Hidden"));

        return name.equalsIgnoreCase(visible) || name.equalsIgnoreCase(hidden);
    }

    private boolean isOnCooldown(Player player) {
        Long lastUsed = cooldowns.get(player.getUniqueId());
        return lastUsed != null && (System.currentTimeMillis() - lastUsed) < cooldownSeconds * 1000L;
    }

    private long getRemainingCooldown(Player player) {
        Long lastUsed = cooldowns.get(player.getUniqueId());
        if (lastUsed == null) return 0;
        long elapsed = (System.currentTimeMillis() - lastUsed) / 1000L;
        return Math.max(0, cooldownSeconds - elapsed);
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void sendMessage(Player player, String path, String defaultMsg, long timeLeft) {
        if (player == null) return;

        String msg = VisibilityConfig.getConfig().getString("Messages." + path, defaultMsg);
        if (msg != null && msg.trim().length() > 0) {
            try {
                if (plugin.getPlaceholders() != null) {
                    plugin.getPlaceholders().setCooldownTime(timeLeft);
                    msg = plugin.getPlaceholders().replacePlaceholders(msg, player);
                } else {
                    msg = msg.replace("{time}", String.valueOf(timeLeft));
                }
                player.sendMessage(ColorUtils.translateColorCodes(msg));
            } catch (Exception e) {
                player.sendMessage(ColorUtils.translateColorCodes(defaultMsg.replace("{time}", String.valueOf(timeLeft))));
            }
        }
    }

    private void sendMessage(Player player, String path, String defaultMsg) {
        if (player == null) return;

        String msg = VisibilityConfig.getConfig().getString("Messages." + path, defaultMsg);
        if (msg != null && msg.trim().length() > 0) {
            try {
                if (plugin.getPlaceholders() != null) {
                    msg = plugin.getPlaceholders().replacePlaceholders(msg, player);
                }
                player.sendMessage(ColorUtils.translateColorCodes(msg));
            } catch (Exception e) {
                player.sendMessage(ColorUtils.translateColorCodes(defaultMsg));
            }
        }
    }
}