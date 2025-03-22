package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Vanish implements CommandExecutor {
    private Lobby plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vanish")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("lobby.vanish")) {
                    if(plugin.vanish_list.contains(player)) {
                        for (Player people: Bukkit.getOnlinePlayers()) {
                            people.showPlayer(player);
                        }
                        plugin.vanish_list.remove(player);
                        player.sendMessage(ColorUtils.translateColorCodes("&cYou are no longer in vanish"));
                    } else if (!plugin.vanish_list.contains(player)) {
                        for (Player people : Bukkit.getOnlinePlayers()) {
                            if (!people.hasPermission("lobby.vanish")) {
                                people.hidePlayer(player);
                            } else {
                                people.showPlayer(player);
                            }
                        }
                        plugin.vanish_list.add(player);
                        player.sendMessage(ColorUtils.translateColorCodes("&aYou are now in vanish"));
                    }
                } else {
                    player.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to run this command"));
                }
            }
            return true;
        }
        return true;
    }
}
