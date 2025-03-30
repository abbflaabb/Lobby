package com.abbas.lobby.commands.PlayerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;

public class InformationCommand implements CommandExecutor {

    private static final String PERMISSION_INFO = "lobby.information";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = Config.getConfig();
        
        if (!sender.hasPermission(PERMISSION_INFO)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                config.getString("messages.noPermission")));
            return true;
        }

        sendServerInformation(sender, config);
        return true;
    }

    private void sendServerInformation(CommandSender sender, FileConfiguration config) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━ " + ChatColor.RED + "Server Information" + ChatColor.GRAY + " ━━━━━━━━━━");
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Rules:");
        for (String rule : config.getStringList("information.rules")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
        }
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Social Media:");
        sender.sendMessage(ChatColor.GRAY + "• Discord: " + ChatColor.WHITE + config.getString("information.social.discord"));
        sender.sendMessage(ChatColor.GRAY + "• Website: " + ChatColor.WHITE + config.getString("information.social.website"));
        sender.sendMessage(ChatColor.GRAY + "• Store: " + ChatColor.WHITE + config.getString("information.social.store"));
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Information:");
        sender.sendMessage(ChatColor.GRAY + "• Version: " + ChatColor.WHITE + config.getString("information.server.version"));
        sender.sendMessage(ChatColor.GRAY + "• Game Mode: " + ChatColor.WHITE + config.getString("information.server.gamemode"));
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Staff Contact:");
        sender.sendMessage(ChatColor.GRAY + "• Report players: " + ChatColor.WHITE + "/report <player> <reason>");
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Ranks:");
        for (String rank : config.getStringList("information.ranks")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rank));
        }
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Quick Commands:");
        sender.sendMessage(ChatColor.GRAY + "• " + ChatColor.WHITE + "/spawn - Return to spawn");
        sender.sendMessage(ChatColor.GRAY + "• " + ChatColor.WHITE + "/help - View HelpCommands");
        sender.sendMessage(ChatColor.GRAY + "• " + ChatColor.WHITE + "/rules - View detailed rules" + ColorUtils.translateColorCodes(" - Coming Soon"));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("");
    }
}
