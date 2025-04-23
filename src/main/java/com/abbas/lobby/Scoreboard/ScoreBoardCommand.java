package com.abbas.lobby.Scoreobard;

import com.abbas.lobby.API.IScoreboard;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreBoardCommand implements CommandExecutor {
    private final IScoreboard scoreboard;

    public ScoreBoardCommand(IScoreboard scoreboard) {
        this.scoreboard = scoreboard;
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
            scoreboard.toggleScoreboardState(player.getUniqueId());
            String message = scoreboard.isScoreboardHidden(player.getUniqueId())
                    ? ScoreBoardConfig.getConfig().getString("scoreboard.disabled-message", "&cScoreboard disabled!")
                    : ScoreBoardConfig.getConfig().getString("scoreboard.enabled-message", "&aScoreboard enabled!");
            player.sendMessage(ColorUtils.translateColorCodes(message));
        } catch (Exception e) {
            player.sendMessage(ColorUtils.translateColorCodes("&cError toggling scoreboard"));
            e.printStackTrace();
            return false;
        }

        return true;
    }
}