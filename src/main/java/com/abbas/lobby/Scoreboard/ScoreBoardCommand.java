package com.abbas.lobby.Scoreboard;

import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.API.MainAPIS.IScoreboard;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ScoreBoardCommand implements ICommandAPI {
    private static final String COMMAND_NAME = "sb";
    private static final String PERMISSION_NODE = "lobby.scoreboard";
    private final IScoreboard scoreboard;

    public ScoreBoardCommand(IScoreboard scoreboard) {
        this.scoreboard = scoreboard;
        setupConfig();
    }

    @Override
    public void setupConfig() {
        ScoreBoardConfig.setupConfig();
        FileConfiguration config = ScoreBoardConfig.getConfig();
        if (!config.isConfigurationSection("scoreboard")) {
            config.set(ConfigPath.SCOREBOARD_NO_PERMISSION, "&c⚠ You do not have permission to toggle the scoreboard!");
            config.set(ConfigPath.SCOREBOARD_ENABLED_MESSAGE, "&a✔ Scoreboard enabled!");
            config.set(ConfigPath.SCOREBOARD_DISABLED_MESSAGE, "&c✖ Scoreboard disabled!");
            config.set(ConfigPath.SCOREBOARD_ERROR_MESSAGE, "&c⚠ Error toggling scoreboard!");
            config.set(ConfigPath.SCOREBOARD_PLAYER_ONLY, "&c⚠ This command can only be used by players!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!isPlayerOnly()) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        Player player = (Player) sender;
        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        return toggleScoreboard(player);
    }

    private boolean toggleScoreboard(Player player) {
        try {
            scoreboard.toggleScoreboardState(player.getUniqueId());
            String message = scoreboard.isScoreboardHidden(player.getUniqueId())
                    ? ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_DISABLED_MESSAGE)
                    : ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_ENABLED_MESSAGE);
            player.sendMessage(ColorUtils.translateColorCodes(message));
            return true;
        } catch (Exception e) {
            player.sendMessage(ColorUtils.translateColorCodes(
                    ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_ERROR_MESSAGE)));
            e.printStackTrace();
            return false;
        }
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
        return "Toggle the scoreboard display";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                ScoreBoardConfig.getConfig().getString(ConfigPath.SCOREBOARD_PLAYER_ONLY)));
    }
}
