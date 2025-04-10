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

            String banId = getBanIdForPlayer(playerName);
            if (banId == null) {
                banId = "Unknown";
            }

            String kickMessage = Config.getConfig().getString("banMessages.advancedBanMessage")
                    .replace("%reason%", reason)
                    .replace("%timeLeft%", timeLeft)
                    .replace("%ban_id%", banId);

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ColorUtils.translateColorCodes(kickMessage));
        }
    }

    private String getBanIdForPlayer(String playerName) {
        if (Config.getConfig().contains("banIds." + playerName.toLowerCase())) {
            return Config.getConfig().getString("banIds." + playerName.toLowerCase());
        }
        return null;
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