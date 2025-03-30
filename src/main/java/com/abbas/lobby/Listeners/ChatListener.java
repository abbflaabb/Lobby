package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ChatFilterConfig;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class ChatListener implements Listener {

    private final Plugin plugin;

    public ChatListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String message = event.getMessage();

        String punishmentLevel = ChatFilterConfig.checkMessage(message);

        if (punishmentLevel.equals("none")) {
            return;
        }

        event.setCancelled(true);

        String warningMessage = ChatFilterConfig.getPunishmentMessage(punishmentLevel);
        if (warningMessage != null && !warningMessage.isEmpty()) {
            player.sendMessage(ColorUtils.translateColorCodes(warningMessage));
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            executePunishment(player, punishmentLevel);
        });
    }

    private void executePunishment(Player player, String punishmentLevel) {
        String muteDuration = ChatFilterConfig.getMuteDuration(punishmentLevel);
        if (muteDuration != null && !muteDuration.isEmpty()) {
            // If you have a mute command, use it here
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + player.getName() + " " + muteDuration + " Inappropriate language");
        }

        for (String command : ChatFilterConfig.getPunishmentCommands(punishmentLevel)) {
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
