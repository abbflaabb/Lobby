package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class Ping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,  String[] args) {
        if (command.getName().equalsIgnoreCase("ping")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int ping = ((CraftPlayer) player).getHandle().ping;

                player.sendMessage(ColorUtils.translateColorCodes("Your ping is: " + ping + "ms"));
            }
        }
        return true;
    }
}
