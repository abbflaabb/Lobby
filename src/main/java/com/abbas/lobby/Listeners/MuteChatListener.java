package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.EventsAPI.MuteChatAPI;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.commands.AdminCommands.MuteCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteChatListener implements Listener, MuteChatAPI {

    @EventHandler(priority = EventPriority.LOWEST)
    @Override
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isPlayerMuted(player)) {
            event.setCancelled(true);
            String timeLeft = MuteCommand.getMuteTimeLeft(player.getUniqueId());
            sendMuteMessage(player, timeLeft);
        }
    }

    @Override
    public void sendMuteMessage(Player player, String timeLeft) {
        FileConfiguration config = Config.getConfig();
        if (timeLeft.equals("Permanent")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("muteMessages.cannotChatPermanent")));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("muteMessages.cannotChat")
                            .replace("%timeLeft%", timeLeft)));
        }
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        return MuteCommand.isPlayerMuted(player.getUniqueId());
    }
}