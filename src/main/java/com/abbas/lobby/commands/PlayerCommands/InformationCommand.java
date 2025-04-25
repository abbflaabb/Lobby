package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
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
            config.set(ConfigCommandPath.INFO_NO_PERMISSION, "&c⚠ You do not have permission to use this command!");
            config.set(ConfigCommandPath.INFO_PLAYER_ONLY, "&c⚠ Console cannot use this command!");

            config.set(ConfigCommandPath.INFO_RULES, Arrays.asList(
                    "&7• No hacking or exploiting.",
                    "&7• Be respectful to all players.",
                    "&7• No advertising or spamming."
            ));

            config.set(ConfigCommandPath.INFO_SOCIAL_DISCORD, "https://discord.com/example");
            config.set(ConfigCommandPath.INFO_SOCIAL_WEBSITE, "https://example.com");
            config.set(ConfigCommandPath.INFO_SOCIAL_STORE, "https://store.example.com");

            config.set(ConfigCommandPath.INFO_SERVER_VERSION, "1.20.1");
            config.set(ConfigCommandPath.INFO_SERVER_GAMEMODE, "Survival");

            config.set(ConfigCommandPath.INFO_STAFF_CONTACT, Arrays.asList(
                    "&7• Contact admins via /discord.",
                    "&7• Report issues in our Discord server.",
                    "&7• For urgent matters, email support@example.com."
            ));

            config.set(ConfigCommandPath.INFO_QUICK_COMMANDS, Arrays.asList(
                    "&7• &b/spawn - &fReturn to spawn",
                    "&7• &b/help - &fView help commands"
            ));

            Config.save();
        }
    }

    private void sendServerInformation(CommandSender sender, FileConfiguration config) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━ " + ChatColor.RED + "Server Information" + ChatColor.GRAY + " ━━━━━━━━━━");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Rules:");
        config.getStringList(ConfigCommandPath.INFO_RULES).forEach(rule ->
                sender.sendMessage(ColorUtils.translateColorCodes(rule)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Social Media:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bDiscord: &f" + config.getString(ConfigCommandPath.INFO_SOCIAL_DISCORD)));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bWebsite: &f" + config.getString(ConfigCommandPath.INFO_SOCIAL_WEBSITE)));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bStore: &f" + config.getString(ConfigCommandPath.INFO_SOCIAL_STORE)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Information:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bVersion: &f" + config.getString(ConfigCommandPath.INFO_SERVER_VERSION)));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bGame Mode: &f" + config.getString(ConfigCommandPath.INFO_SERVER_GAMEMODE)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Staff Contact:");
        config.getStringList(ConfigCommandPath.INFO_STAFF_CONTACT).forEach(contact ->
                sender.sendMessage(ColorUtils.translateColorCodes(contact)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Quick Commands:");
        config.getStringList(ConfigCommandPath.INFO_QUICK_COMMANDS).forEach(command ->
                sender.sendMessage(ColorUtils.translateColorCodes(command)));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("");
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
                Config.getConfig().getString(ConfigCommandPath.INFO_NO_PERMISSION)));
    }
    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.INFO_PLAYER_ONLY)));
    }
}
