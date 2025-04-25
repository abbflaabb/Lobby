package com.abbas.lobby.Scoreboard;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.MainAPIS.ILuckPerms;
import com.abbas.lobby.API.MainAPIS.IScoreboard;
import com.abbas.lobby.Lobby;
import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.Utils.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class ScoreBoardManager implements IScoreboard {
    private final Lobby plugin;
    private static final int MAX_LINE_LENGTH = 40;
    private final Placeholders placeholders;
    private final ILuckPerms luckPerms;

    public ScoreBoardManager(Lobby plugin) {
        this.plugin = plugin;
        this.luckPerms = new LuckPermsRank();
        ScoreBoardConfig.setupConfig();
        this.luckPerms.setup();
        this.placeholders = new Placeholders(this.luckPerms);
    }

    @Override
    public void setScoreboard(Player player) {
        if (isScoreboardHidden(player.getUniqueId())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            return;
        }

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("lobby", "dummy");

        String title = PlaceholderAPI.setPlaceholders(player,
                ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_TITLE));
        obj.setDisplayName(ColorUtils.translateColorCodes(truncate(title)));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        setScoreboardLines(player, board, obj);
    }

    private void setScoreboardLines(Player player, Scoreboard board, Objective obj) {
        String[] lines = ScoreBoardConfig.getConfig().getStringList(ConfigPath.SCOREBOARD_LINES).toArray(new String[0]);
        int score = lines.length;

        for (String line : lines) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
            line = replacePlaceholders(line, player);
            String coloredLine = ColorUtils.translateColorCodes(line);
            String truncatedLine = truncate(coloredLine);

            while (board.getEntries().contains(truncatedLine)) {
                truncatedLine += "§r";
                if (truncatedLine.length() > MAX_LINE_LENGTH) {
                    truncatedLine = truncatedLine.substring(0, MAX_LINE_LENGTH);
                }
            }

            obj.getScore(truncatedLine).setScore(score--);
        }

        player.setScoreboard(board);
    }

    @Override
    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                setScoreboard(player);
            }
        }
    }

    @Override
    public void toggleScoreboardState(UUID playerId) {
        ScoreBoardListener.toggleScoreboardState(playerId);
        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.isOnline()) {
            setScoreboard(player);
        }
    }

    @Override
    public boolean isScoreboardHidden(UUID playerId) {
        return ScoreBoardListener.isScoreboardHidden(playerId);
    }

    @Override
    public String getPlayerRank(Player player) {
        return luckPerms.getPlayerRank(player);
    }

    private String truncate(String text) {
        return text.length() > MAX_LINE_LENGTH ? text.substring(0, MAX_LINE_LENGTH) : text;
    }

    private String replacePlaceholders(String line, Player player) {
        String replaced = line
                .replace("%online_players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%player_name%", player.getName())
                .replace("%Lobby_rank%", getPlayerRank(player))
                .replace("%ip_server%", Bukkit.getServer().getIp());

        return placeholders.replacePlaceholders(replaced, player);
    }
}
