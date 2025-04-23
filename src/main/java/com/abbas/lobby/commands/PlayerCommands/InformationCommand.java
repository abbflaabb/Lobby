package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class InformationCommand implements ICommandAPI {
    private static final String COMMAND_NAME = "information";
    private static final String PERMISSION_NODE = "lobby.information";

    public InformationCommand() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("information")) {
            config.set("information.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("information.playerOnly", "&c⚠ Console cannot use this command!");

            config.set("information.rules", Arrays.asList(
                    "&7• No hacking or exploiting.",
                    "&7• Be respectful to all players.",
                    "&7• No advertising or spamming."
            ));

            config.set("information.social.discord", "https://discord.com/example");
            config.set("information.social.website", "https://example.com");
            config.set("information.social.store", "https://store.example.com");

            config.set("information.server.version", "1.20.1");
            config.set("information.server.gamemode", "Survival");

            config.set("information.staffContact", Arrays.asList(
                    "&7• Contact admins via /discord.",
                    "&7• Report issues in our Discord server.",
                    "&7• For urgent matters, email support@example.com."
            ));

            config.set("information.quickCommands", Arrays.asList(
                    "&7• &b/spawn - &fReturn to spawn",
                    "&7• &b/help - &fView help commands"
            ));

            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        sendServerInformation(sender, Config.getConfig());
        return true;
    }

    private void sendServerInformation(CommandSender sender, FileConfiguration config) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━ " + ChatColor.RED + "Server Information" + ChatColor.GRAY + " ━━━━━━━━━━");
        sender.sendMessage("");

        // Server Rules
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Rules:");
        config.getStringList("information.rules").forEach(rule ->
                sender.sendMessage(ColorUtils.translateColorCodes(rule)));
        sender.sendMessage("");

        // Social Media
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Social Media:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bDiscord: &f" + config.getString("information.social.discord")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bWebsite: &f" + config.getString("information.social.website")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bStore: &f" + config.getString("information.social.store")));
        sender.sendMessage("");

        // Server Info
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Information:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bVersion: &f" + config.getString("information.server.version")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bGame Mode: &f" + config.getString("information.server.gamemode")));
        sender.sendMessage("");

        // Staff Contact
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Staff Contact:");
        config.getStringList("information.staffContact").forEach(contact ->
                sender.sendMessage(ColorUtils.translateColorCodes(contact)));
        sender.sendMessage("");

        // Quick Commands
        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Quick Commands:");
        config.getStringList("information.quickCommands").forEach(command ->
                sender.sendMessage(ColorUtils.translateColorCodes(command)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("");
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
        return "Display server information";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("information.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("information.playerOnly")));
    }
}