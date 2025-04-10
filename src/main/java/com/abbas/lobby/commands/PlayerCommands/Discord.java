package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class Discord implements CommandExecutor {

    public Discord() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("discord")) {
            config.set("discord.message-1", "&3&l &9&lDiscord");
            config.set("discord.message-2", "&3&l Join our discord server");
            config.set("discord.message-3", "&3&l www.discord.com/example.com");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("discord")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                FileConfiguration config = Config.getConfig();
                String message1 = config.getString("discord.message-1");
                String message2 = config.getString("discord.message-2");
                String message3 = config.getString("discord.message-3");

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message1));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message2));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message3));
            }
            return true;
        }
        return false;
    }
}
