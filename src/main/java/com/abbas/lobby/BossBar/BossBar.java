package com.abbas.lobby.BossBar;

import com.abbas.lobby.API.IBossBar;
import com.abbas.lobby.API.ILuckPerms;
import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.Scoreobard.LuckPermsRank;
import com.abbas.lobby.Utils.ColorUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossBar implements IBossBar {
    private final Plugin plugin;
    private final BukkitAudiences adventure;
    private final Placeholders placeholders;
    private final Map<Player, net.kyori.adventure.bossbar.BossBar> playerBossBars;
    private final List<String> messages;
    private int currentIndex;
    private boolean enabled;
    private BukkitRunnable animationTask;

    public BossBar(Plugin plugin) {
        this.plugin = plugin;
        this.adventure = BukkitAudiences.create(plugin);
        this.placeholders = new Placeholders(new LuckPermsRank());
        this.playerBossBars = new HashMap<>();
        this.messages = new ArrayList<>();
        this.currentIndex = 0;
        setupConfig();
        this.enabled = BossBarConfig.getConfig().getBoolean("bossbar.enabled", true);
        if (enabled) {
            startAnimation();
        }
    }
    @Override
    public void setupConfig() {
        BossBarConfig.setup();
        if (!BossBarConfig.getConfig().isSet("bossbar.enabled") ||
                !BossBarConfig.getConfig().isList("bossbar.messages")) {
            List<String> defaultMessages = new ArrayList<>();
            defaultMessages.add("&6Welcome to the server!");
            defaultMessages.add("&bPlayers online: %online_players%");
            defaultMessages.add("&aServer IP: %ip_server%");
            BossBarConfig.getConfig().set("bossbar.messages", defaultMessages);
            BossBarConfig.getConfig().set("bossbar.update-interval", 3);
            BossBarConfig.getConfig().set("bossbar.enabled", true);
            BossBarConfig.save();
        }
        messages.addAll(BossBarConfig.getConfig().getStringList("bossbar.messages"));
    }
    @Override
    public void startAnimation() {
        if (animationTask != null) {
            animationTask.cancel();
        }

        int interval = BossBarConfig.getConfig().getInt("bossbar.update-interval", 3) * 20;
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateBossBars();
            }
        };
        animationTask.runTaskTimer(plugin, 0L, interval);
    }
    @Override
    public void updateBossBars() {
        if (!enabled || messages.isEmpty()) return;
        currentIndex = (currentIndex + 1) % messages.size();
        playerBossBars.forEach((player, bossBar) -> {
            String message = messages.get(currentIndex);
            message = placeholders.replacePlaceholders(message, player);
            Component text = Component.text(ColorUtils.translateColorCodes(message));
            bossBar.name(text);
        });
    }
    @Override
    public void addPlayer(Player player) {
        if (!enabled || messages.isEmpty()) return;
        String message = messages.get(currentIndex);
        message = placeholders.replacePlaceholders(message, player);
        Component text = Component.text(ColorUtils.translateColorCodes(message));
        net.kyori.adventure.bossbar.BossBar bossBar = net.kyori.adventure.bossbar.BossBar.bossBar(
                text,
                1.0f,
                Color.PURPLE,
                Overlay.PROGRESS
        );
        Audience audience = adventure.player(player);
        audience.showBossBar(bossBar);
        playerBossBars.put(player, bossBar);
    }
    @Override
    public void removePlayer(Player player) {
        net.kyori.adventure.bossbar.BossBar bossBar = playerBossBars.remove(player);
        if (bossBar != null) {
            Audience audience = adventure.player(player);
            audience.hideBossBar(bossBar);
        }
    }
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        BossBarConfig.getConfig().set("bossbar.enabled", enabled);
        BossBarConfig.save();

        if (!enabled) {
            if (animationTask != null) {
                animationTask.cancel();
                animationTask = null;
            }
            playerBossBars.forEach((player, bossBar) -> {
                Audience audience = adventure.player(player);
                audience.hideBossBar(bossBar);
            });
            playerBossBars.clear();
        } else {
            startAnimation();
            plugin.getServer().getOnlinePlayers().forEach(this::addPlayer);
        }
    }
    @Override
    public void close() {
        if (animationTask != null) {
            animationTask.cancel();
        }
        if (adventure != null) {
            playerBossBars.forEach((player, bossBar) -> {
                Audience audience = adventure.player(player);
                audience.hideBossBar(bossBar);
            });
            adventure.close();
        }
    }
}