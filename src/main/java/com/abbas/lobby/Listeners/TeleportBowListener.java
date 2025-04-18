package com.abbas.lobby.Listeners;

import com.abbas.lobby.Items.TeleportBow;
import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportBowListener implements Listener {
    private final Lobby plugin;

    public TeleportBowListener(Lobby plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        if (!Config.getConfig().contains("teleport-bow")) {
            Config.getConfig().set("teleport-bow.name", "&b&lTeleport Bow");
            Config.getConfig().set("teleport-bow.lore", new String[]{
                    "&7Right click to teleport to",
                    "&7the location you are looking at."
            });
            Config.getConfig().set("teleport-bow.arrow-speed", 2.0);
            Config.getConfig().set("teleport-bow.enabled", true);
            Config.save();
        }
    }

    @EventHandler
    public void onBowShoot(ProjectileLaunchEvent event) {
        if (!Config.getConfig().getBoolean("teleport-bow.enabled", true)) return;
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        ItemStack bow = player.getInventory().getItemInHand();
        if (!bow.hasItemMeta() || !bow.getItemMeta().hasDisplayName()) return;
        String configName = ColorUtils.translateColorCodes(Config.getConfig().getString("teleport-bow.name", "&b&lTeleport Bow"));
        if (!bow.getItemMeta().getDisplayName().equals(configName)) return;
        Arrow arrow = (Arrow) event.getEntity();
        double speed = Math.min(Config.getConfig().getDouble("teleport-bow.arrow-speed", 2.0), 4.0);
        arrow.setVelocity(arrow.getVelocity().normalize().multiply(speed));
        player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (player == null || !player.isOnline()) return;
                player.getInventory().clear();
                player.getInventory().setItem(0, TeleportBow.InventoryBow());
                player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
                player.updateInventory();
            }
        }.runTaskLater(plugin, 5L);
    }
    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (!Config.getConfig().getBoolean("teleport-bow.enabled", true)) return;
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        ItemStack bow = player.getInventory().getItemInHand();
        if (!bow.hasItemMeta() || !bow.getItemMeta().hasDisplayName()) return;
        String configName = ColorUtils.translateColorCodes(Config.getConfig().getString("teleport-bow.name", "&b&lTeleport Bow"));
        if (!bow.getItemMeta().getDisplayName().equals(configName)) return;
        player.teleport(event.getEntity().getLocation());
        event.getEntity().remove();
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }
}
