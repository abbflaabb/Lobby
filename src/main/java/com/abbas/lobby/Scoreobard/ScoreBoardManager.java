package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreBoardManager {
    private final Lobby plugin;
    private static final int MAX_LINE_LENGTH = 40;

    public ScoreBoardManager(Lobby plugin) {
        this.plugin = plugin;
        ScoreBoardConfig.setupConfig();
        LuckPermsRank.setup();

    }

    public void setScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("lobby", "dummy");

        String title = PlaceholderAPI.setPlaceholders(player,
                ScoreBoardConfig.getConfig().getString("scoreboard.title"));
        obj.setDisplayName(ColorUtils.translateColorCodes(truncate(title)));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[] lines = ScoreBoardConfig.getConfig().getStringList("scoreboard.lines").toArray(new String[0]);
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

            Score lineScore = obj.getScore(truncatedLine);
            lineScore.setScore(score--);
        }

        player.setScoreboard(board);
    }

    private String truncate(String text) {
        if (text.length() > MAX_LINE_LENGTH) {
            return text.substring(0, MAX_LINE_LENGTH);
        }
        return text;
    }


    private String replacePlaceholders(String line, Player player) {
        return line
                .replace("%online_players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%player_name%", player.getName())
                .replace("%Lobby_rank%", LuckPermsRank.getPlayerRank(player));
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                setScoreboard(player);
            }
        }
    }
}
