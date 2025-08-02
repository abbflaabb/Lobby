package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.BanConfig;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Ban implements ICommandAPI {
    private static final String COMMAND_NAME = "ban";
    private static final String PERMISSION_NODE = "lobby.ban";
    private static Map<String, String> banIds = new HashMap<>();

    public Ban() {
        setupConfig();
        loadBanIds();
        startBanExpirationChecker();
    }

    @Override
    public void setupConfig() {
        BanConfig.setupConfig();
        Config.setup();

        if (!Config.getConfig().isConfigurationSection("banMessages")) {
            Config.getConfig().set(ConfigCommandPath.BAN_NO_PERMISSION, "&c⚠ You do not have permission to ban players!");
            Config.getConfig().set(ConfigCommandPath.BAN_USAGE, "&c⚠ Usage: /ban <player> [duration] [reason]");
            Config.getConfig().set(ConfigCommandPath.BAN_PLAYER_NOT_FOUND, "&c⚠ Player not found!");
            Config.getConfig().set(ConfigCommandPath.BAN_SUCCESS, "&a✔ Successfully banned %player% for %reason% (ID: %ban_id%)");
            Config.getConfig().set(ConfigCommandPath.BAN_FORUM_LINK, "&ehttps://example.com/appeal");
            Config.getConfig().set(ConfigCommandPath.BAN_PLAYER_ONLY, "&c⚠ This command can only be used by players!");
            Config.getConfig().set(ConfigCommandPath.BAN_INVALID_DURATION, "&cInvalid duration format. Use s (seconds), m (minutes), h (hours), or d (days).");
            Config.getConfig().set(ConfigCommandPath.BAN_EXPIRATION, "&aBan expires on %date%");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString(ConfigCommandPath.BAN_USAGE)));
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString(ConfigCommandPath.BAN_PLAYER_NOT_FOUND)));
            return true;
        }

        processBan(sender, target, args);
        return true;
    }

    private void processBan(CommandSender sender, Player target, String[] args) {
        String targetName = target.getName();
        long duration = -1;
        String reason = "Hacking";

        if (args.length > 1) {
            try {
                duration = parseDuration(args[1]);
                if (args.length > 2) {
                    reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtils.translateColorCodes(
                        Config.getConfig().getString(ConfigCommandPath.BAN_INVALID_DURATION)));
                return;
            }
        }

        String banId = generateBanId();
        banIds.put(targetName.toLowerCase(), banId);
        saveBanId(targetName.toLowerCase(), banId);

        Date expires = (duration > 0) ? new Date(System.currentTimeMillis() + duration) : null;
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(targetName, reason, expires, sender.getName());

        target.kickPlayer(ColorUtils.translateColorCodes(formatBanMessage(reason, expires, banId)));

        String senderMessage = Config.getConfig().getString(ConfigCommandPath.BAN_SUCCESS)
                .replace("%player%", targetName)
                .replace("%reason%", reason)
                .replace("%ban_id%", banId);
        sender.sendMessage(ColorUtils.translateColorCodes(senderMessage));
    }

    private String generateBanId() {
        return "BAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void saveBanId(String playerName, String banId) {
        Config.getConfig().set(ConfigCommandPath.BAN_IDS + playerName, banId);
        Config.save();
    }

    private void loadBanIds() {
        if (Config.getConfig().isConfigurationSection("banIds")) {
            for (String playerName : Config.getConfig().getConfigurationSection("banIds").getKeys(false)) {
                banIds.put(playerName, Config.getConfig().getString("banIds." + playerName));
            }
        }
    }

    private void removeBanId(String playerName) {
        banIds.remove(playerName.toLowerCase());
        Config.getConfig().set("banIds." + playerName.toLowerCase(), null);
        Config.save();
    }

    private long parseDuration(String durationStr) throws NumberFormatException {
        char timeUnit = durationStr.charAt(durationStr.length() - 1);
        long duration = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
        switch (timeUnit) {
            case 's': return duration * 1000;
            case 'm': return duration * 1000 * 60;
            case 'h': return duration * 1000 * 60 * 60;
            case 'd': return duration * 1000 * 60 * 60 * 24;
            default: throw new NumberFormatException("Invalid time unit");
        }
    }

    private String formatBanMessage(String reason, Date expires, String banId) {
        String timeLeft = (expires != null) ? getTimeLeft(expires) : "Permanent";
        String forumLink = Config.getConfig().getString(ConfigCommandPath.BAN_FORUM_LINK);

        return "§c§lLobbyBan§7» §cBanned for " + reason + "\n"
                + "§4§oBanned by CONSOLE\n\n"
                + "§7It seems like you are using a hacked client, please disable it!\n"
                + "§cUnban in §f> §e" + timeLeft + "\n"
                + "§7Ban ID: §e" + banId + "\n\n"
                + "§7Unban application in TS or forum\n"
                + "§eTS-Ip§7 » §ccoming soon\n"
                + forumLink;
    }

    private static String getTimeLeft(Date expires) {
        if (expires == null) {
            return "Permanent";
        }

        long diff = expires.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

        String expirationMessage = Config.getConfig().getString(ConfigCommandPath.BAN_EXPIRATION)
                .replace("%date%", days + "day(s) " + hours + "h " + minutes + "min and " + seconds + "sec");
        return ColorUtils.translateColorCodes(expirationMessage);
    }

    private void startBanExpirationChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                org.bukkit.BanList banList = Bukkit.getBanList(org.bukkit.BanList.Type.NAME);
                for (BanEntry entry : banList.getBanEntries()) {
                    Date expiration = entry.getExpiration();
                    if (expiration != null && expiration.before(new Date())) {
                        banList.pardon(entry.getTarget());
                        removeBanId(entry.getTarget());
                        Bukkit.broadcastMessage(ColorUtils.translateColorCodes("&aBan for " + entry.getTarget() + " has expired."));
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Lobby"), 0L, 1200L);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermissionNode());
    }

    @Override
    public String getPermissionNode() {
        return PERMISSION_NODE;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription() {
        return "Ban a player from the server";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.BAN_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.BAN_PLAYER_ONLY, "&c⚠ This command can only be used by players!")));
    }
}