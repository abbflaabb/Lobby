package com.abbas.lobby.Listeners;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BanListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry banEntry = banList.getBanEntry(playerName);

        if (banEntry != null) {
            Date expires = banEntry.getExpiration();
            String reason = banEntry.getReason();
            String timeLeft = (expires != null) ? getTimeLeft(expires) : "Permanent";

            // Get the customizable AdvancedBan message from the configuration
            String kickMessage = Config.getConfig().getString("banMessages.ban.advancedBanMessage")
                    .replace("%reason%", reason)
                    .replace("%timeLeft%", timeLeft);

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ColorUtils.translateColorCodes(kickMessage));
        }
    }

    private String getTimeLeft(Date expires) {
        long diff = expires.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

        return days + "day(s) " + hours + "h " + minutes + "min and " + seconds + "sec";
    }
}
