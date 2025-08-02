package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Listeners.MuteListGUI;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.translateColorCodes("&cThis command can only be used by players."));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("lobby.mutelist")) {
            player.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to use this command."));
            return true;
        }
        MuteListGUI.openMuteList(player, 1);
        return true;
    }
}