package com.abbas.lobby.commands.AdminCommand;

import com.abbas.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vanish implements CommandExecutor {

    Lobby plugin;

    public Vanish(Lobby plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vanish")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("lobby.vanish")) {
                    if(plugin.vanish_list.contains(player)) {
                        for (Player people: Bukkit.getOnlinePlayers()) {
                            people.showPlayer(plugin, player);
                        }
                        plugin.vanish_list.remove(player);
                    }
                }
            }
            return true;
        }
        return true;
    }
}
