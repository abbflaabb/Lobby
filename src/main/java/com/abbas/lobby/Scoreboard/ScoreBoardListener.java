package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ScoreBoardListener implements Listener {
    private final ScoreBoardManager boardManager;
    private static final Set<UUID> hiddenScoreboards = new HashSet<>();

    public ScoreBoardListener(Lobby plugin) {
        this.boardManager = new ScoreBoardManager(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (hiddenScoreboards.contains(player.getUniqueId())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        } else {
            this.boardManager.setScoreboard(player);
        }

        this.boardManager.updateScoreboard();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Scoreboard mainBoard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (player.getScoreboard().equals(mainBoard)) {
            hiddenScoreboards.add(player.getUniqueId());
        } else {
            hiddenScoreboards.remove(player.getUniqueId());
        }

        this.boardManager.updateScoreboard();
    }

    public static void toggleScoreboardState(UUID playerId) {
        if (hiddenScoreboards.contains(playerId)) {
            hiddenScoreboards.remove(playerId);
        } else {
            hiddenScoreboards.add(playerId);
        }
    }

    public static boolean isScoreboardHidden(UUID playerId) {
        return hiddenScoreboards.contains(playerId);
    }
}
