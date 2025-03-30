package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.commands.AdminCommands.MuteCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        if (MuteCommand.isPlayerMuted(player.getUniqueId())) {
            event.setCancelled(true);
            
            String timeLeft = MuteCommand.getMuteTimeLeft(player.getUniqueId());
            if (timeLeft.equals("Permanent")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("muteMessages.mute.cannotChatPermanent")));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("muteMessages.mute.cannotChat")
                        .replace("%timeLeft%", timeLeft)));
            }
        }
    }
}
