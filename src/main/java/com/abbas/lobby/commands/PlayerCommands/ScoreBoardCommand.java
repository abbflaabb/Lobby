package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Scoreobard.ScoreBoardManager;
import com.abbas.lobby.Scoreobard.ScoreBoardConfig;
import com.abbas.lobby.Scoreobard.ScoreBoardListener;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoardCommand implements CommandExecutor {
    private final ScoreBoardManager boardManager;

    public ScoreBoardCommand(ScoreBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes("&cOnly players can use this command!"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("lobby.scoreboard")) {
            player.sendMessage(ColorUtils.translateColorCodes(ScoreBoardConfig.getConfig().getString("scoreboard.No-permission", "&cNo permission")));
            return true;
        }

        try {
            if (ScoreBoardListener.isScoreboardHidden(player.getUniqueId())) {
                boardManager.setScoreboard(player);
                ScoreBoardListener.toggleScoreboardState(player.getUniqueId());
                String enableMessage = ScoreBoardConfig.getConfig().getString("scoreboard.enabled-message", "&aScoreboard enabled!");
                player.sendMessage(ColorUtils.translateColorCodes(enableMessage));
            } else {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                ScoreBoardListener.toggleScoreboardState(player.getUniqueId());
                String disableMessage = ScoreBoardConfig.getConfig().getString("scoreboard.disabled-message", "&cScoreboard disabled!");
                player.sendMessage(ColorUtils.translateColorCodes(disableMessage));
            }
        } catch (Exception e) {
            player.sendMessage(ColorUtils.translateColorCodes("&cError toggling scoreboard"));
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
