
package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreBoardListener implements Listener {
    private final ScoreBoardManager boardManager;

    public ScoreBoardListener(Lobby plugin) {
        this.boardManager = new ScoreBoardManager(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.boardManager.setScoreboard(event.getPlayer());
        this.boardManager.updateScoreboard();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.boardManager.updateScoreboard();
    }
}
