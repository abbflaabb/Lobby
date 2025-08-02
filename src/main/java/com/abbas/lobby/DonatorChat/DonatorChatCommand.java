package com.abbas.lobby.DonatorChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonatorChatCommand implements CommandExecutor {
    private final DonatorChat donatorChat;

    public DonatorChatCommand(DonatorChat donatorChat) {
        this.donatorChat = donatorChat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                donatorChat.toggleDonatorChat(player);
                break;
            case "info":
                showInfo(player);
                break;
            case "format":
                showFormat(player);
                break;
            case "cooldown":
                showCooldown(player);
                break;
            default:
                showHelp(player);
                break;
        }
        return true;
    }

    private void showInfo(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Donator Chat Info ===");
        showFormat(player);
        showCooldown(player);
    }

    private void showFormat(Player player) {
        String format = donatorChat.getFormat();
        player.sendMessage(ChatColor.YELLOW + "Chat Format: " +
                ChatColor.translateAlternateColorCodes('&', format));
    }

    private void showCooldown(Player player) {
        int cooldown = donatorChat.getCooldown();
        player.sendMessage(ChatColor.YELLOW + "Message Cooldown: " +
                ChatColor.WHITE + cooldown + " seconds");
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Donator Chat Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/dchat toggle " + ChatColor.GRAY + "- Toggle donator chat");
        player.sendMessage(ChatColor.YELLOW + "/dchat info " + ChatColor.GRAY + "- Show chat information");
        player.sendMessage(ChatColor.YELLOW + "/dchat format " + ChatColor.GRAY + "- Show chat format");
        player.sendMessage(ChatColor.YELLOW + "/dchat cooldown " + ChatColor.GRAY + "- Show message cooldown");
    }
}