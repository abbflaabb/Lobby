package com.abbas.lobby.TeleportBow;

import com.abbas.lobby.API.MainAPIS.ITeleportAPI;
import com.abbas.lobby.Lobby;
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

public class TeleportBowListener implements ITeleportAPI, Listener {
    private final Lobby plugin;
    private final ITeleportAPI teleportAPI;

    public TeleportBowListener(Lobby plugin) {
        this.plugin = plugin;
        this.teleportAPI = new TeleportBow();
        this.teleportAPI.setupConfig();
    }

    @Override
    public void setupConfig() {
        teleportAPI.setupConfig();
    }

    @Override
    public boolean isEnabled() {
        return teleportAPI.isEnabled();
    }

    @Override
    public boolean isTeleportBow(ItemStack item) {
        return teleportAPI.isTeleportBow(item);
    }

    @Override
    public double getArrowSpeed() {
        return teleportAPI.getArrowSpeed();
    }

    @Override
    public void giveTeleportBowItems(Player player) {
        teleportAPI.giveTeleportBowItems(player);
    }

    @EventHandler
    public void onBowShoot(ProjectileLaunchEvent event) {
        if (!isEnabled()) return;
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        ItemStack bow = player.getInventory().getItemInHand();

        if (!isTeleportBow(bow)) return;

        Arrow arrow = (Arrow) event.getEntity();
        arrow.setVelocity(arrow.getVelocity().normalize().multiply(getArrowSpeed()));
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
                giveTeleportBowItems(player);
            }
        }.runTaskLater(plugin, 5L);
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (!isEnabled()) return;
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        ItemStack bow = player.getInventory().getItemInHand();

        if (!isTeleportBow(bow)) return;

        player.teleport(event.getEntity().getLocation());
        event.getEntity().remove();
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }
    @Override
    public ItemStack createTeleportBow() {
        return teleportAPI.createTeleportBow();
    }
}