package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoardManager {
    private final Lobby plugin;

    public ScoreBoardManager(Lobby plugin) {
        this.plugin = plugin;
        ScoreBoardConfig.setupConfig();
    }

    public void setScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("lobby", "dummy");
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', ScoreBoardConfig.getConfig().getString("scoreboard.title", "&bYour Server Name")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score space1 = obj.getScore("§1");
        space1.setScore(6);
        Score players = obj.getScore(ChatColor.WHITE + "Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
        players.setScore(5);
        Score space2 = obj.getScore("§2");
        space2.setScore(4);
        String customLine = ChatColor.translateAlternateColorCodes('&', ScoreBoardConfig.getConfig().getString("scoreboard.customLine", "&eCustom Line"));
        Score custom = obj.getScore(customLine);
        custom.setScore(3);
        Score space3 = obj.getScore("§3");
        space3.setScore(2);
        String website = ChatColor.translateAlternateColorCodes('&', ScoreBoardConfig.getConfig().getString("scoreboard.website", "&eyourwebsite.com"));
        Score websiteScore = obj.getScore(website);
        websiteScore.setScore(1);
        player.setScoreboard(board);
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.setScoreboard(player);
        }
    }
}
