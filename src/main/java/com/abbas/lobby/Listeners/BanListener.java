package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.EventsAPI.BanListenerAPI;
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

public class BanListener implements Listener, BanListenerAPI {

    @EventHandler
    @Override
    public void handlePlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        BanEntry banEntry = getBanEntry(playerName);

        if (banEntry != null) {
            Date expires = banEntry.getExpiration();
            String timeLeft = calculateTimeLeft(expires);
            String banId = getBanIdForPlayer(playerName);
            String message = formatBanMessage(banEntry.getReason(), timeLeft, banId);
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, message);
        }
    }

    @Override
    public String getBanIdForPlayer(String playerName) {
        if (Config.getConfig().contains("banIds." + playerName.toLowerCase())) {
            return Config.getConfig().getString("banIds." + playerName.toLowerCase());
        }
        return "Unknown";
    }

    @Override
    public String calculateTimeLeft(Date expires) {
        if (expires == null) return "Permanent";

        long diff = expires.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

        return days + "day(s) " + hours + "h " + minutes + "min and " + seconds + "sec";
    }

    @Override
    public BanEntry getBanEntry(String playerName) {
        return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(playerName);
    }

    @Override
    public boolean isPlayerBanned(String playerName) {
        return getBanEntry(playerName) != null;
    }

    @Override
    public String formatBanMessage(String reason, String timeLeft, String banId) {
        return ColorUtils.translateColorCodes(
                Config.getConfig().getString("banMessages.advancedBanMessage")
                        .replace("%reason%", reason)
                        .replace("%timeLeft%", timeLeft)
                        .replace("%ban_id%", banId)
        );
    }
}
